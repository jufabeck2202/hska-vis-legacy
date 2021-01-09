package de.hska.productcomposite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpHeaders;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import de.hska.productcomposite.model.Category;
import de.hska.productcomposite.model.Product;


@Component
public class ProductCompositeClient {
	// über ribbon
	private static String PRODUCTS_URI = "http://products-core-service/products";
	private static String CATEGORIES_URI = "http://category-core-service/categories";
	
	//über docker url
	//private static String PRODUCTS_URI = "http://product-core:8012/products";
	//private static String CATEGORIES_URI = "http://category-core:8003/categories";
	
	private final Map<Long, Double> productPriceCache = new LinkedHashMap<Long, Double>();
	private final Map<Long, String> productDescCache = new LinkedHashMap<Long, String>();
	private final Map<Long, Product> productCache = new LinkedHashMap<Long, Product>();
	
	@Autowired
	private RestTemplate restTemplate;
	
	private String accessToken = null;
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	@HystrixCommand(fallbackMethod = "getProductsCache", 
			commandProperties = { @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2") })
	public Product[] getProducts(String description, String minPrice, String maxPrice) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PRODUCTS_URI);
		if(description != null) {
			builder.queryParam("searchDescription", description);
		}
		if(minPrice != null) {
			builder.queryParam("minPrice", minPrice);
		}
		if(maxPrice != null) {
			builder.queryParam("maxPrice", maxPrice);
		}
		String uri = builder.toUriString();

		ResponseEntity<Product[]> prod = restTemplate.getForEntity(uri, Product[].class);
		for(Product p : prod.getBody()) {
			ResponseEntity<Category> c = restTemplate.getForEntity(CATEGORIES_URI.concat("/"+p.getCategoryId()), Category.class);
			p.setCategory(c.getBody());
			productCache.putIfAbsent(p.getId(), p);
			
			productDescCache.putIfAbsent(p.getId(), p.getDetails());
			productPriceCache.putIfAbsent(p.getId(), p.getPrice());
		}
		accessToken = null;
		return prod.getBody();
	}
	

	
	public Product[] getProductsCache(String description, String minPrice, String maxPrice) {
		List<Product> list = new ArrayList<>();
		if(description == null && minPrice == null && maxPrice == null) {
			 Product[] array = productCache.values().toArray(new Product[productCache.values().size()]);
			 return array;
		}
		if(description != null && productDescCache.containsValue(description)) {
			for(Long key : getKeysByValue(productDescCache, description)) {
				Product p = productCache.get(key);
				if(minPrice != null) {
					Double min = Double.parseDouble(minPrice);
					if(p.getPrice() < min) continue;
				}
				if(maxPrice != null) {
					Double max = Double.parseDouble(maxPrice);
					if(p.getPrice() > max) continue;
				}
				list.add(p);
			}
		} else if(minPrice != null && maxPrice != null) {
			Double min = Double.parseDouble(minPrice);
			Double max = Double.parseDouble(maxPrice);
			Map<Long, Product> resmap = productCache.entrySet() 
	          .stream() 
	          .filter(map -> map.getKey().doubleValue() > min && map.getKey().doubleValue() < max) 
	          .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
			for(Long key : resmap.keySet()) {
				list.add(productCache.get(key));
			}
		} else {
			if(minPrice != null) {
				Double min = Double.parseDouble(minPrice);
				Map<Long, Product> resmap = productCache.entrySet() 
						.stream() 
				        .filter(map -> map.getKey().doubleValue() > min) 
				        .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
				for(Long key : resmap.keySet()) {
					list.add(productCache.get(key));
				}
			}
			if(maxPrice != null) {
				Double max = Double.parseDouble(maxPrice);
				Map<Long, Product> resmap = productCache.entrySet() 
						.stream() 
				        .filter(map -> map.getKey().doubleValue() < max) 
				        .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
				for(Long key : resmap.keySet()) {
					list.add(productCache.get(key));
				}
			}
		}
		return list.toArray(new Product[list.size()]);
	}
	
	private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
	    Set<T> keys = new HashSet<T>();
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}
	
	public void deleteCategory(Long id) {
		ResponseEntity<Category> c = restTemplate.getForEntity(CATEGORIES_URI.concat("/"+id), Category.class);
		if(c.getBody().getProductIds() != null && !c.getBody().getProductIds().isEmpty()) {
			for(String pid : c.getBody().getProductIds().split(",")) {
				restTemplate.delete(PRODUCTS_URI.concat("/"+pid));
			}
		}
		restTemplate.delete(CATEGORIES_URI.concat("/"+id));
		accessToken = null;
	}
	
	public Product createProduct(Product prod) {
		long categoryId = prod.getCategoryId();
		ResponseEntity<Category> c = restTemplate.getForEntity(CATEGORIES_URI.concat("/"+categoryId), Category.class);
		Category category = c.getBody();
		if (category == null) {return null;}

		Product newProduct = restTemplate.postForObject(PRODUCTS_URI, prod, Product.class);
		category.setProductIds(category.getProductIds().concat((category.getProductIds().isEmpty()? "":","))+newProduct.getId().toString());
		
		//update Category
		restTemplate.put(CATEGORIES_URI.concat("/"+categoryId), category);
		accessToken = null;
		return newProduct;
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
	   RestTemplate restTemplate = new RestTemplate();
	   restTemplate.getInterceptors().add(new OAuthInterceptor());
	   return restTemplate;
	}
	class OAuthInterceptor implements ClientHttpRequestInterceptor
	{

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException
		{
			if (accessToken == null)
			{
				//throw new IOException("Token not set");
				System.out.println("##################### Token not set! ###################");
			}
			else
			{
				System.out.println("##################### Token found: " + accessToken);
				HttpHeaders headers = request.getHeaders();
				headers.add(HttpHeaders.AUTHORIZATION, "bearer " + accessToken);
			}

			return execution.execute(request, body);
		}
	}
}

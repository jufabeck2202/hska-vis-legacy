package de.hska.productcomposite;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hska.productcomposite.model.Product;

@RestController
public class ProductCompositeController {
	@Autowired
	ProductCompositeClient client;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<?> search(@RequestParam(name="searchDescription", required=false) String description, @RequestParam(name="minPrice", required=false) String minPrice,
			@RequestParam(name="maxPrice", required=false) String maxPrice, Authentication auth) {
		if (auth == null){
			System.out.println("No Token");
			return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
		}
		Collection<?extends GrantedAuthority> granted = auth.getAuthorities();

		System.out.println("Authorities:");
		System.out.println(Arrays.toString(granted.toArray()));
		if(auth != null && auth.getDetails() instanceof OAuth2AuthenticationDetails)
		{
			client.setAccessToken(((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue());
		}
		
		Product[] prod = client.getProducts(description, minPrice, maxPrice);
		return new ResponseEntity<Product[]>(prod, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> search(@PathVariable Long id, Authentication auth) {
		Collection<?extends GrantedAuthority> granted = auth.getAuthorities();
		System.out.println("Authorities:");
		System.out.println(Arrays.toString(granted.toArray()));
		if(auth != null && auth.getDetails() instanceof OAuth2AuthenticationDetails)
		{
			client.setAccessToken(((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue());
		}
		client.deleteCategory(id);
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<?> addProduct(@RequestBody Product product, Authentication auth) {
		Collection<?extends GrantedAuthority> granted = auth.getAuthorities();
		System.out.println("Authorities:");
		System.out.println(Arrays.toString(granted.toArray()));
		if(auth != null && auth.getDetails() instanceof OAuth2AuthenticationDetails)
		{
			client.setAccessToken(((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue());
		}
		System.out.print("Adding new Product");
		Product prod = client.createProduct(product);
		if (prod == null){
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Product>(prod, HttpStatus.CREATED);
	}
	
	
}


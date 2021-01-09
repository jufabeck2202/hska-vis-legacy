package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class AddProductAction extends ActionSupport {

	private static final long serialVersionUID = 39979991339088L;

	private String name = null;
	private String price = null;
	private int categoryId = 0;
	private String details = null;
	private List<Category> categories;
	
	private final String ADD_PRODUCT_URL = "http://zuul-server:8081/productscomposite-service/products";
	private final String GET_CATEGORIES_URL = "http://zuul-server:8081/category-core-service/categories";

	public String execute() throws Exception {
		System.out.println("AddProduct");
		String result = "input";
		OAuth2RestTemplate oAuth2RestTemplate = OAuth2Config.getTemplate();
		Map<String, Object> session = ActionContext.getContext().getSession();
		User user = (User) session.get("webshop_user");
		//check if user exists and roletype is admin
		if(user != null && (user.getRoletype().equalsIgnoreCase("admin"))) {
			try {
				Product product = new Product(name, Double.parseDouble(price), Long.valueOf(categoryId),
						details);
				System.out.println("1");
				Product response = oAuth2RestTemplate.postForObject(ADD_PRODUCT_URL, product, Product.class);
				System.out.println("2");
				System.out.print(response.getName());
				int productId = Math.toIntExact(response.getId());
				System.out.println("3");
				System.out.println(productId);


				if (productId > 0) {
					result = "success";
				}
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				throw e;
			}
			
		}

		return result;
	}

	@Override
	public void validate() {
		OAuth2RestTemplate oAuth2RestTemplate = OAuth2Config.getTemplate();
		Category[] arr = oAuth2RestTemplate.getForEntity(GET_CATEGORIES_URL,Category[].class).getBody();
		if(arr != null) {
			this.setCategories(Arrays.asList(arr));
		} else {
			this.setCategories(Collections.emptyList());
		}
		if (getName() == null || getName().length() == 0) {
			addActionError(getText("error.product.name.required"));
		}

		if (String.valueOf(getPrice()).length() > 0) {
			if (!getPrice().matches("[0-9]+(.[0-9][0-9]?)?")
					|| Double.parseDouble(getPrice()) < 0.0) {
				addActionError(getText("error.product.price.regex"));
			}
		} else {
			addActionError(getText("error.product.price.required"));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}

package hska.iwi.eShopMaster.controller;

import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
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

public class ListAllProductsAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -94109228677381902L;
	
	User user;
	private List<Product> products;
	
	private final String PRODUCTS_URL = "http://zuul-server:8081/productscomposite-service/products";
	
	public String execute() throws Exception{
		String result = "input";
		OAuth2RestTemplate oAuth2RestTemplate = OAuth2Config.getTemplate();
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		user = (User) session.get("webshop_user");
		System.out.println(user.getUsername()+"tries to get in");
		if(user != null){
			System.out.println("list all products!");
			try {
				
				Product[] arr = oAuth2RestTemplate.getForEntity(PRODUCTS_URL,Product[].class).getBody();
				if(arr != null) {
					this.products = Arrays.asList(arr);
					System.out.println("Products");
				} else {
					this.products = Collections.emptyList();
					System.out.println("empty");

				}
			} catch (Exception e) {
				System.out.println(e);
			}

			result = "success";
		}
		
		return result;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}

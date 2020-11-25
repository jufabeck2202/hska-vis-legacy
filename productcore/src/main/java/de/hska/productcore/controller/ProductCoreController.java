package de.hska.productcore.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hska.productcore.model.Product;
import de.hska.productcore.model.ProductRepo;

@RestController
public class ProductCoreController {
	@Autowired
	private ProductRepo repo;
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Product>> getProducts(@RequestParam(name="searchDescription", required=false) String description,
			@RequestParam(name="minPrice", required=false) String minPrice, @RequestParam(name="maxPrice", required=false) String maxPrice) {
		Iterable<Product> all;
		if(description == null && minPrice == null && maxPrice == null) {
			all = repo.findAll();
		} else {
			Double maxPriceDouble = maxPrice != null ? Double.parseDouble(maxPrice) : null;
			Double minPriceDouble = minPrice != null ? Double.parseDouble(minPrice) : null;
			all = repo.findByDetailsAndPriceLessThanAndPriceGreaterThan(description, maxPriceDouble, minPriceDouble);
		}
		return new ResponseEntity<Iterable<Product>>(all, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		product = repo.save(product);
		return new ResponseEntity<Product>(product, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
		Product product = repo.findById(productId).orElse(null);
		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product newProduct) {
		// problem with id: If we dont supply id it generates an error
		Product oldProduct = repo.findById(productId).orElse(null);
		if(oldProduct != null && newProduct != null && oldProduct.getId() == newProduct.getId()) {
			System.out.println("Has been update");
			repo.save(newProduct);
		}
		return new ResponseEntity<Product>(newProduct, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {
		repo.deleteById(productId);
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
}
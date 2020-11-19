package de.hska.categorycore.controller;

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

import de.hska.categorycore.model.Category;
import de.hska.categorycore.model.CategoryRepo;

@RestController
public class CategoryCoreController {

	@Autowired
	private CategoryRepo repo;
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Category>> getCategories(@RequestParam(name="name", required=false) String name) {
		Iterable<Category> allPolls;
		if(name != null && !name.isEmpty()) {
			allPolls = Arrays.asList(repo.findByName(name).orElse(null));
		} else {
			allPolls = repo.findAll();
		}
		return new ResponseEntity<Iterable<Category>>(allPolls, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<?> addCategory(@RequestBody Category category) {
		category = repo.save(category);
		return new ResponseEntity<Object>(null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) {
		Category category = repo.findById(categoryId).orElse(null);
		return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
		//Category user = repo.findById(categoryId).orElse(null);
		repo.deleteById(categoryId);
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	

	
}

package de.hska.categorycore.model;

import java.util.Optional;

public interface CategoryRepo extends org.springframework.data.repository.CrudRepository<Category, Long> {
	Optional<Category> findByName(String name);
}
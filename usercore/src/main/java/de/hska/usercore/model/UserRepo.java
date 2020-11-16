package de.hska.usercore.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
	List<User> findByUsername(String username);
}
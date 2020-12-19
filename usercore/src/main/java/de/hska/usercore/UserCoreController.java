package de.hska.usercore;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.hska.usercore.model.User;
import de.hska.usercore.model.UserRepo;


@RestController
public class UserCoreController {
	@Autowired
	private UserRepo repo;
	@Autowired
	private PasswordEncoder encoder;
	
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		user = repo.save(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	
	
	@RequestMapping(value = "/users/name/{name}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByName(@PathVariable String name) {
		List<User> userList = repo.findByUsername(name);
		if(userList.size() >= 1) {
			return new ResponseEntity<User>(userList.get(0), HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(new User(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		User user = repo.findById(userId).orElse(null);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
		User userlocal = repo.findById(userId).orElse(null);
		if(user != null && userlocal != null && userlocal.getId()  == user.getId()) {
			repo.save(user);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
		repo.deleteById(userId);
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
}

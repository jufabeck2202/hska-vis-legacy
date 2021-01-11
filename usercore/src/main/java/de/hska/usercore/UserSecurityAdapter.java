package de.hska.usercore;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import de.hska.usercore.model.User;
import de.hska.usercore.model.UserRepo;

@Service
public class UserSecurityAdapter implements UserDetailsService {

	@Autowired
	private UserRepo repo;
	/**
	 * Search User repo for user by username. Used by oauth2 server to determine if user exists.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		User user = repo.findByUsername(username).get(0);
		
		
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());

		List<SimpleGrantedAuthority> list = new ArrayList<>();
		list.add(new SimpleGrantedAuthority(user.getRoletype().toUpperCase()));
		return new org.springframework.security.core.userdetails
				.User(user.getUsername(), user.getPassword(),
	            list);
	}

}
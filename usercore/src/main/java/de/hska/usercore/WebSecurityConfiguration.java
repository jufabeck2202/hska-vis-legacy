package de.hska.usercore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.netflix.ribbon.proxy.annotation.Http;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(-20)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private UserSecurityService userDetailsService;
	/*
	 * Override this method to expose the AuthenticationManager from 
	 * configure(AuthenticationManagerBuilder) to be exposed as a Bean. 
	 * 
	 */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    // you shouldn't use plain text
    @Bean
    public PasswordEncoder encoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/users");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
			.requestMatchers()
			.antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
            .and()
            .formLogin().loginPage("/login").permitAll().failureUrl("/login?error")
            .and()
            .authorizeRequests().anyRequest().authenticated();
    }
}
package hska.iwi.eShopMaster.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import hska.iwi.eShopMaster.model.database.dataobjects.User;

public class RegisterAction extends ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = 3655805600703279195L;
	private String username;
	private String password1;
	private String password2;
	private String firstname;
	private String lastname;

	private static RestTemplate restTemplate = generateRestTemplate();

	private static final String USERS_URL = "http://user-core:8001/users/"; // "http://zuul:8081/users";

	private static RestTemplate generateRestTemplate() {

		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter();
		jsonHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		restTemplate.getMessageConverters().add(jsonHttpMessageConverter);
		return restTemplate;
	}

	@Override
	public String execute() throws Exception {

		// Return string:
		String result = "input";

		// this.role = userManager.getRoleByLevel(1); // 1 -> regular User, 2-> Admin

		User user = new User(username, firstname, lastname, password1, "admin", 1);
		System.out.println(username);
		System.out.println(firstname);
		System.out.println(lastname);
		System.out.println(password1);
		System.out.println(user.toString());
		try {

			restTemplate.postForEntity(USERS_URL, user, User.class).getBody();
			addActionMessage(username+" registered, please login");
			addActionError("user registered, please login");
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.put("message", firstname+" registered, please login");
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;

	}

	@Override
	public void validate() {
		if (getFirstname().length() == 0) {
			addActionError(getText("error.firstname.required"));
		}
		if (getLastname().length() == 0) {
			addActionError(getText("error.lastname.required"));
		}
		if (getUsername().length() == 0) {
			addActionError(getText("error.username.required"));
		}
		if (getPassword1().length() == 0) {
			addActionError(getText("error.password.required"));
		}
		if (getPassword2().length() == 0) {
			addActionError(getText("error.password.required"));
		}

		if (!getPassword1().equals(getPassword2())) {
			addActionError(getText("error.password.notEqual"));
		}
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getUsername() {
		return (this.username);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword1() {
		return (this.password1);
	}

	public void setPassword1(String password) {
		this.password1 = password;
	}

	public String getPassword2() {
		return (this.password2);
	}

	public void setPassword2(String password) {
		this.password2 = password;
	}

}

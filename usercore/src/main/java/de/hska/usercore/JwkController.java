package de.hska.usercore;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joe Grandja
 */
@RestController
public class JwkController {

	@Autowired
	private JWKSet jwkSet;

	@GetMapping(value = "/oauth2/keys", produces = "application/json; charset=UTF-8")
	public String keys() {
		System.out.println("------------getting oauth keys");
		return this.jwkSet.toString();
	}
}
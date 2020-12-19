package de.hska.usercore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder encoder;
	

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// client detail service In memory, Client braucht clientid, client secrete und grand types des clients.
		clients.inMemory()
			.withClient("webshop").secret(encoder.encode("secret"))
			.authorizedGrantTypes("authorization_code", "password", "client_credentials", "refresh_token")
			//scopes hier anders, und es fehlt das redirect
			.scopes("read", "write", "trust");
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//auch anders konfiguriert, kein AcessToken Converter
		//legt nicht sicherheitsrelevante dienste fest.
	    endpoints.authorizationCodeServices(authorizationCodeServices())
	            .authenticationManager(authenticationManager)
	            .tokenStore(tokenStore())
	            .approvalStoreDisabled();
	}
	
	@Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
	    security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
	}
}
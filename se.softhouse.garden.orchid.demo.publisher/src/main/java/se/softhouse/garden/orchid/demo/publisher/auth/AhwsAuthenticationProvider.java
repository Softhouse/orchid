package se.softhouse.garden.orchid.demo.publisher.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

public class AhwsAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = (String) authentication.getPrincipal();

		if (username == null) {
			throw new BadCredentialsException("No username provided");
		}

		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException("No password provided");
		}

		String password = authentication.getCredentials().toString();

		// Login

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		return new AhwsAuthenticationToken(username, password, authorities);
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}

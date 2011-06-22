package se.softhouse.garden.orchid.demo.publisher.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class AhwsAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public AhwsAuthenticationToken(Object principal, Object credentials) {
		this(principal, credentials, null);
	}

	public AhwsAuthenticationToken(Object principal, Object credentials, Collection<GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}

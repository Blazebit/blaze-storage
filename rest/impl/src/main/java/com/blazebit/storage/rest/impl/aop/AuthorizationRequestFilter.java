package com.blazebit.storage.rest.impl.aop;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import com.blazebit.storage.core.model.security.Role;

public class AuthorizationRequestFilter implements ContainerRequestFilter {
	
	@Inject
	private KeycloakSecurityContext securityContext;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		AccessToken token = securityContext.getToken();
		if (token == null || !token.getRealmAccess().getRoles().contains(Role.USER)) {
			throw new WebApplicationException("User cannot access the resource.", Response.Status.UNAUTHORIZED);
		}
	}
}
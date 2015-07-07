package com.blazebit.storage.rest.impl.producer;

import java.security.Principal;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

@ApplicationScoped
public class KeycloakSecurityContextProducer {

	@Produces
	@RequestScoped
	public KeycloakSecurityContext produceSecurityContext(HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		if(userPrincipal == null || !(userPrincipal instanceof KeycloakPrincipal)){
			return emptySecurityContext();
		}

		KeycloakSecurityContext context = ((KeycloakPrincipal<?>) userPrincipal).getKeycloakSecurityContext();
		if (context == null) {
			return emptySecurityContext();
		}
		
		return context;
	}
	
	private KeycloakSecurityContext emptySecurityContext() {
		return new KeycloakSecurityContext();
	}
}

package com.blazebit.storage.modules.authentication.keycloak;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

public class KeycloakRequestAuthenticator implements RequestAuthenticator {

	@Override
	public String getUserId(HttpServletRequest request) {
		IDToken token = AdminClient.getIDToken(request);
		
		if (token == null) {
			return null;
		}
		
		return token.getId();
	}

	@Override
	public Set<String> getUserRoles(HttpServletRequest request, Set<String> allRoles) {
		KeycloakSecurityContext context = getSecurityContext(request);
		return context.getToken().getRealmAccess().getRoles();
	}
	
	public KeycloakSecurityContext getSecurityContext(HttpServletRequest request) {
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

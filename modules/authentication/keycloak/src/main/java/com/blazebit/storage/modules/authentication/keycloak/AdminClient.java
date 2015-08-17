package com.blazebit.storage.modules.authentication.keycloak;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.IDToken;

public class AdminClient {
	
	public static IDToken getIDToken(HttpServletRequest req) {
    	if (req.getUserPrincipal() == null) {
    		return null;
    	}
    	KeycloakSecurityContext session = ((KeycloakPrincipal<?>) req.getUserPrincipal()).getKeycloakSecurityContext();
    	if (session == null) {
        	return null;
        }
    	return getIDToken(req, session);
    }

	private static IDToken getIDToken(HttpServletRequest req, KeycloakSecurityContext session){
		String authorizationHeader = req.getHeader(HttpHeaders.AUTHORIZATION);
        
    	IDToken token;
    	if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
    		token = session.getToken();
    	} else {
    		token = session.getIdToken();
    	}
    	
    	return token;
	}
	
}
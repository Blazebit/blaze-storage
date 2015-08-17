package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class BearerAuthFilter implements ClientRequestFilter, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String tokenString;
    private final TokenManager tokenManager;

    public BearerAuthFilter(String tokenString) {
        this.tokenString = tokenString;
        this.tokenManager = null;
    }

    public BearerAuthFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.tokenString = null;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String authHeader = "Bearer " + (tokenManager != null ? tokenManager.getAccessTokenString() : tokenString);

        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
    }

}
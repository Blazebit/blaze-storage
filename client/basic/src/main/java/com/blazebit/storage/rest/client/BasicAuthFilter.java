package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.Serializable;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.glassfish.jersey.internal.util.Base64;

public class BasicAuthFilter implements ClientRequestFilter, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String username;
    private final String password;

    public BasicAuthFilter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String pair = username + ":" + password;
        String authHeader = "Basic " + Base64.encodeAsString(pair.getBytes());
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
    }
    
}
package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.resource.BasicAuthFilter;
import org.keycloak.admin.client.token.TokenService;
import org.keycloak.representations.AccessTokenResponse;

public class TokenManager implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private AccessTokenResponse currentToken;
    private Date expirationTime;
    private final Config config;
    private final transient Client client;

    public TokenManager(Config config, Client client){
        this.config = config;
        this.client = client;
    }

    public TokenManager(AccessTokenResponse currentToken, Config config, Client client){
        this.config = config;
        this.client = client;
        if (currentToken != null) {
        	defineCurrentToken(currentToken);
        }
    }

    public String getAccessTokenString(){
        return getAccessToken().getToken();
    }

    public AccessTokenResponse getAccessToken(){
        if (currentToken == null) {
            grantToken();
        } else if(tokenExpired()) {
            refreshToken();
        }
        return currentToken;
    }

    public AccessTokenResponse grantToken(){
        WebTarget target = client.target(config.getServerUrl());

        Form form = new Form()
                .param("grant_type", "password")
                .param("username", config.getUsername())
                .param("password", config.getPassword());

        if (config.isPublicClient()) {
            form.param("client_id", config.getClientId());
        } else {
            target.register(new BasicAuthFilter(config.getClientId(), config.getClientSecret()));
        }

        TokenService tokenService = WebResourceFactory.newResource(TokenService.class, target);

        AccessTokenResponse response = tokenService.grantToken(config.getRealm(), form.asMap());

        defineCurrentToken(response);
        return response;
    }

    public AccessTokenResponse refreshToken(){
    	WebTarget target = client.target(config.getServerUrl());

        Form form = new Form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", currentToken.getRefreshToken());

        if (config.isPublicClient()) {
            form.param("client_id", config.getClientId());
        } else {
            target.register(new BasicAuthFilter(config.getClientId(), config.getClientSecret()));
        }

        TokenService tokenService = WebResourceFactory.newResource(TokenService.class, target);

        try {
            AccessTokenResponse response = tokenService.refreshToken(config.getRealm(), form.asMap());
            defineCurrentToken(response);
            return response;
        } catch (BadRequestException e) {
            return grantToken();
        }
    }

    private void setExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, (int) currentToken.getExpiresIn());
        expirationTime = cal.getTime();
    }

    private boolean tokenExpired() {
        return new Date().after(expirationTime);
    }

    private void defineCurrentToken(AccessTokenResponse accessTokenResponse){
        currentToken = accessTokenResponse;
        setExpirationTime();
    }
    
    // SERIALIZATION
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        set("client", ClientBuilder.newClient());
    }
    
    private void set(String fieldName, Object value) {
    	try {
	    	Field field = BlazeStorageClient.class.getDeclaredField(fieldName);
	    	field.setAccessible(true);
	    	field.set(this, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

}

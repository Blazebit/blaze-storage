package com.blazebit.storage.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.keycloak.admin.client.Config;
import org.keycloak.representations.AccessTokenResponse;

public class KeycloakBlazeStorage {

	public static BlazeStorage getInstance(String serverUrl, AccessTokenResponse accessToken, Config keycloakConfig) {
		Client client = ClientBuilder.newClient();
		TokenManager tokenManager = new TokenManager(accessToken, keycloakConfig, client);
		return BlazeStorageClient.getInstance(serverUrl, new BearerAuthFilter(tokenManager));
	}
	
	public static BlazeStorage getInstance(String serverUrl, String tokenString) {
		return BlazeStorageClient.getInstance(serverUrl, new BearerAuthFilter(tokenString));
	}

	public static BlazeStorage getInstance(String serverUrl, String keycloakServerUrl, String realm, String username, String password, String clientId, String clientSecret) {
		return getInstance(serverUrl, null, new Config(keycloakServerUrl, realm, username, password, clientId, clientSecret));
	}
	
	public static BlazeStorage getInstance(String serverUrl, String keycloakServerUrl, String realm, String username, String password, String clientId) {
		return getInstance(serverUrl, null, new Config(keycloakServerUrl, realm, username, password, clientId, null));
	}
}

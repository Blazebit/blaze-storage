package com.blazebit.storage.rest.client;

public class BasicBlazeStorage {

	public static BlazeStorage getInstance(String serverUrl, String username, String password) {
		return BlazeStorageClient.getInstance(serverUrl, new BasicAuthFilter(username, password));
	}
}

package com.blazebit.storage.core.config.wildfly.initializer;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class InitializerSingleton {

	private static final Logger LOG = Logger.getLogger(InitializerSingleton.class.getName());
	
	@PostConstruct
	public void init() {
		// TODO: warm up REST endpoints
		// Initialize some rest resources
//		Client client = ClientBuilder.newClient();
	}
}
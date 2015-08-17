package com.blazebit.storage.rest.impl.context;

import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.UserDataAccess;
import com.blazebit.storage.core.api.UserService;
import com.blazebit.storage.core.api.context.UserContext;
import com.blazebit.storage.core.model.jpa.User;
import com.blazebit.storage.core.model.security.Role;
import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

@ApplicationScoped
public class UserContextProducer {
	
	private static final Logger LOG = Logger.getLogger(UserContextProducer.class.getName());
	
	@Inject
	private RequestAuthenticator requestAuthenticator;
	@Inject
	private UserDataAccess userDataAccess;
	@Inject
	private UserService userService;

	@Produces
	@RequestScoped
	public UserContext produceUserContext(HttpServletRequest request) {
		User user = null;
		String userId = requestAuthenticator.getUserId(request);
		Set<String> userRoles = requestAuthenticator.getUserRoles(request, Role.ROLES);
		
		if (userId == null) {
			throw new RuntimeException("The request was invoked with a user that has no userId!");
		}

		user = userDataAccess.findById(userId);
		
		if (user == null) {
			user = new User(userId);
			
			try {
				userService.create(user);
				LOG.fine("Created user with id: " + user.getId() + "'");
			} catch (StorageException e) {
				// if parallel requests are processed for the same user, another thread might have already created the user
				User existingUser = userDataAccess.findById(userId);
				if (existingUser == null) {
					throw new RuntimeException("User with id [" + user.getId() + "] unexpectedly absent");
				}
				
				user = existingUser;
			}
		}
			
		return new UserContextImpl(user.getId(), userRoles, Locale.ENGLISH);
	}
}
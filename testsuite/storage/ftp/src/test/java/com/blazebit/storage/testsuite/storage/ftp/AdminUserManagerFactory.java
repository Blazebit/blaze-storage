package com.blazebit.storage.testsuite.storage.ftp;

import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UserManagerFactory;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;

public class AdminUserManagerFactory implements UserManagerFactory, UserManager {
	
	private final String homeDirectory;

	public AdminUserManagerFactory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	@Override
	public User authenticate(Authentication auth) throws AuthenticationFailedException {
		if (auth instanceof UsernamePasswordAuthentication) {
			UsernamePasswordAuthentication a = ((UsernamePasswordAuthentication) auth);
			
			if ("admin".equals(a.getUsername()) && "admin".equals(a.getPassword())) {
				return getUserByName("admin");
			}
		}
		
		throw new AuthenticationFailedException();
	}

	@Override
	public void delete(String arg0) throws FtpException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	@Override
	public boolean doesExist(String name) throws FtpException {
		return "admin".equals(name);
	}

	@Override
	public String getAdminName() throws FtpException {
		return "admin";
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		return new String[] { "admin" };
	}

	@Override
	public User getUserByName(String name) {
		if (!"admin".equals(name)) {
			return null;
		}
		
		BaseUser user = new BaseUser();
		user.setName(name);
		user.setPassword(name);
		user.setEnabled(true);
		user.setHomeDirectory(homeDirectory);
		List<Authority> authorities = new ArrayList<Authority>();
		authorities.add(new Authority() {
			
			@Override
			public boolean canAuthorize(AuthorizationRequest request) {
				return true;
			}
			
			@Override
			public AuthorizationRequest authorize(AuthorizationRequest request) {
				return request;
			}
		});
		
		user.setAuthorities(authorities);
		return user;
	}

	@Override
	public boolean isAdmin(String name) throws FtpException {
		return "admin".equals(name);
	}

	@Override
	public void save(User user) throws FtpException {
		throw new UnsupportedOperationException("Not implemented!");
	}

	@Override
	public UserManager createUserManager() {
		return this;
	}

}

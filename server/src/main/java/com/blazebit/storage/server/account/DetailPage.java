package com.blazebit.storage.server.account;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountRepresentation;

@Named
@RequestScoped
public class DetailPage {
	
	private static final Logger LOG = Logger.getLogger(DetailPage.class.getName());

	@Inject
	private BlazeStorage storage;
	@Inject
	private FacesContext facesContext;
	
	private String key;
	private AccountRepresentation account;
	
	public String viewAction() {
		try {
			if (key != null || key.isEmpty()) {
				account = storage.accounts().get(key).get();
				return "";
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty key!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load user"));
			LOG.log(Level.SEVERE, "Could not load user " + key, ex);
			return null;
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public AccountRepresentation getAccount() {
		return account;
	}

	public void setAccount(AccountRepresentation account) {
		this.account = account;
	}
	
}

package com.blazebit.storage.server.account;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class AccountAddPage extends AccountBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AccountAddPage.class.getName());

	public String add() {
		try {
			if (key != null || key.isEmpty()) {
				put();
				return "/account/detail.xhtml?key=" + key + "&faces-redirect=true";
			}

			String message = "Invalid empty key!";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			return null;
		} catch (RuntimeException ex) {
			String message = "Could not add account";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			LOG.log(Level.SEVERE, "Could not add account " + key, ex);
			return null;
		}
	}
	
}

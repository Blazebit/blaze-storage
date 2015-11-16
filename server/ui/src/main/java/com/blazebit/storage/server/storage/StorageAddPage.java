package com.blazebit.storage.server.storage;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class StorageAddPage extends StorageBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StorageAddPage.class.getName());
	
	public String add() {
		try {
			if (name != null || name.isEmpty()) {
				put();
				return "/storage/detail.xhtml?account=" + accountKey + "&name=" + name + "&faces-redirect=true";
			}

			String message = "Invalid empty name!";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			return null;
		} catch (RuntimeException ex) {
			String message = "Could not add storage";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			LOG.log(Level.SEVERE, "Could not add storage " + name, ex);
			return null;
		}
	}

}

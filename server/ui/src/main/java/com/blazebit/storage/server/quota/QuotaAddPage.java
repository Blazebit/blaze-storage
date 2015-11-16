package com.blazebit.storage.server.quota;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class QuotaAddPage extends QuotaBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(QuotaAddPage.class.getName());
	
	public String add() {
		try {
			if (id != null || id.isEmpty()) {
				put();
				return "/quota/detail.xhtml?id=" + id + "&faces-redirect=true";
			}

			String message = "Invalid empty id!";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			return null;
		} catch (RuntimeException ex) {
			String message = "Could not add storage quota model";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			LOG.log(Level.SEVERE, "Could not add storage quota model " + id, ex);
			return null;
		}
	}
	
}

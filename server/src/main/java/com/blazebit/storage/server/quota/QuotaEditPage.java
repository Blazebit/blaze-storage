package com.blazebit.storage.server.quota;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class QuotaEditPage extends QuotaAddPage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(QuotaEditPage.class.getName());
	
	public String update() {
		try {
			put();
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not update storage quota model"));
			LOG.log(Level.SEVERE, "Could not update storage quota model " + id, ex);
			return null;
		}
	}
	
}

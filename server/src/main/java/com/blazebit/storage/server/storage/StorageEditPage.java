package com.blazebit.storage.server.storage;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class StorageEditPage extends StorageAddPage {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StorageEditPage.class.getName());
	
	public String update() {
		try {
			put();
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not update storage"));
			LOG.log(Level.SEVERE, "Could not update storage " + name, ex);
			return null;
		}
	}
}

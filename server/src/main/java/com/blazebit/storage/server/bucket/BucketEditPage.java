package com.blazebit.storage.server.bucket;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class BucketEditPage extends BucketAddPage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketEditPage.class.getName());
	
	public String update() {
		try {
			put();
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not update bucket"));
			LOG.log(Level.SEVERE, "Could not update bucket " + name, ex);
			return null;
		}
	}
}

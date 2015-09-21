package com.blazebit.storage.server.object;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class BucketObjectEditPage extends BucketObjectAddPage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectEditPage.class.getName());
	
	@Override
	protected void init() {
		if (bucket != null) {
			onAccountChanged();
		}
	}
	
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

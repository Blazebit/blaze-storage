package com.blazebit.storage.server.faces;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesContextProducer {

	@Produces
	@RequestScoped
	public FacesContext getInstance() {
		return FacesContext.getCurrentInstance();
	}
}

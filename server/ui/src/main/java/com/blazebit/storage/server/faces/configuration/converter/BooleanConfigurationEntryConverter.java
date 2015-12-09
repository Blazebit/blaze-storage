package com.blazebit.storage.server.faces.configuration.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.BooleanConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("booleanConfigurationEntryConverter")
public class BooleanConfigurationEntryConverter extends BooleanConverter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Boolean booleanValue = (Boolean) super.getAsObject(context, component, value);
		
		if (booleanValue == null) {
			return null;
		}
		
		return booleanValue.toString();
	}

}

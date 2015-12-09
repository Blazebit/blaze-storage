package com.blazebit.storage.server.faces.configuration.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.IntegerConverter;

@FacesConverter("integerConfigurationEntryConverter")
public class IntegerConfigurationEntryConverter extends IntegerConverter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Integer integerValue = (Integer) super.getAsObject(context, component, value);
		
		if (integerValue == null) {
			return null;
		}
		
		return integerValue.toString();
	}

}

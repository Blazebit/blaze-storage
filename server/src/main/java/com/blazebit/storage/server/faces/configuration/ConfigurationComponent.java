package com.blazebit.storage.server.faces.configuration;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;

@FacesComponent("configurationComponent")
public class ConfigurationComponent extends UINamingContainer {

    private UIData table;
    
	public void addTag() {
		getValue().getConfigurationEntries().add(new ConfigurationEntry());
	}

    public void removeTag() {
        getValue().getConfigurationEntries().remove(table.getRowData());
    }
    
    protected ConfigurationHolder getValue() {
    	return (ConfigurationHolder) getAttributes().get("value");
    }

	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}
	
}

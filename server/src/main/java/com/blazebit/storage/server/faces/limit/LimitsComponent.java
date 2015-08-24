package com.blazebit.storage.server.faces.limit;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;

@FacesComponent("limitsComponent")
public class LimitsComponent extends UINamingContainer {

    private UIData table;
    
	public void addLimit() {
		getValue().getLimitEntries().add(new LimitEntry());
	}

    public void removeLimit() {
        getValue().getLimitEntries().remove(table.getRowData());
    }
    
    protected LimitsHolder getValue() {
    	return (LimitsHolder) getAttributes().get("value");
    }

	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}
	
}

package com.blazebit.storage.server.faces;

import java.util.List;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;

@FacesComponent("tagsComponent")
public class TagsComponent extends UINamingContainer {

    private UIData table;
    
	public void addTag() {
		((List) getAttributes().get("value")).add(new TagEntry());
	}

    public void removeTag() {
        ((List) getAttributes().get("value")).remove(table.getRowData());
    }

	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}
	
}

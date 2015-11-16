package com.blazebit.storage.server.faces.tag;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;

@FacesComponent("tagsComponent")
public class TagsComponent extends UINamingContainer {

    private UIData table;
    
	public void addTag() {
		getValue().getTagEntries().add(new TagEntry());
	}

    public void removeTag() {
        getValue().getTagEntries().remove(table.getRowData());
    }
    
    protected TagsHolder getValue() {
    	return (TagsHolder) getAttributes().get("value");
    }

	public UIData getTable() {
		return table;
	}

	public void setTable(UIData table) {
		this.table = table;
	}
	
}

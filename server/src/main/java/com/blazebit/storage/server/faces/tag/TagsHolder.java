package com.blazebit.storage.server.faces.tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class TagsHolder {

	protected List<TagEntry> tagEntries = new ArrayList<>();
	
	public Map<String, String> getTags() {
		Map<String, String> tags = new LinkedHashMap<>(tagEntries.size());
		FacesContext context = FacesContext.getCurrentInstance();
		boolean valid = true;
		
		for (TagEntry entry : tagEntries) {
			if (entry.getKey() == null || entry.getKey().isEmpty()) {
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					// TODO: I18N messages
					String validatorMessageString = "Entry with empty key found for value: " + entry.getValue();
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
	                context.addMessage(null, message);
	                valid = false;
				}
				
				continue;
			}
			if (tags.put(entry.getKey(), entry.getValue()) != null) {
				// TODO: I18N messages
				String validatorMessageString = "Duplicate key found: " + entry.getKey();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
                context.addMessage(null, message);
                valid = false;
			}
		}
		
		if (!valid) {
			throw new RuntimeException("Invalid tags!");
		}
		
		return tags;
	}
	
	public void setTags(Map<String, String> tags) {
		tagEntries = new ArrayList<>(tags.size());
		for (Map.Entry<String, String> entry : tags.entrySet()) {
			tagEntries.add(new TagEntry(entry.getKey(), entry.getValue()));
		}
	}

	public List<TagEntry> getTagEntries() {
		return tagEntries;
	}

	public void setTagEntries(List<TagEntry> tagEntries) {
		this.tagEntries = tagEntries;
	}
}

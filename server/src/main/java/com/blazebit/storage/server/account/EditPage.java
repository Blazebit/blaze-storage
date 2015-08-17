package com.blazebit.storage.server.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdateRepresentation;
import com.blazebit.storage.server.faces.TagEntry;

@Named
@ViewScoped
public class EditPage implements Serializable {
	
	private static final Logger LOG = Logger.getLogger(EditPage.class.getName());

	@Inject
	private BlazeStorage storage;
	@Inject
	private FacesContext facesContext;
	
	private String key;
	private List<TagEntry> tagEntries = new ArrayList<>();
	private AccountUpdateRepresentation account;
	
	public String viewAction() {
		try {
			if (key != null || key.isEmpty()) {
				account = storage.accounts().get(key).get();
				return "";
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty key!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load user"));
			LOG.log(Level.SEVERE, "Could not load user " + key, ex);
			return null;
		}
	}
	
	public String update() {
		try {
			account.setTags(getTags());
			storage.accounts().get(key).put(account);
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not update user"));
			LOG.log(Level.SEVERE, "Could not update user " + key, ex);
			return null;
		}
	}
	
	private Map<String, String> getTags() {
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public AccountUpdateRepresentation getAccount() {
		return account;
	}

	public void setAccount(AccountUpdateRepresentation account) {
		this.account = account;
	}

	public List<TagEntry> getTagEntries() {
		return tagEntries;
	}

	public void setTagEntries(List<TagEntry> tagEntries) {
		this.tagEntries = tagEntries;
	}
	
}

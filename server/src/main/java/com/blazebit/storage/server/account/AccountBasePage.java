package com.blazebit.storage.server.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountUpdateRepresentation;
import com.blazebit.storage.server.faces.tag.TagEntry;
import com.blazebit.storage.server.faces.tag.TagsHolder;
import com.blazebit.storage.server.storage.StorageBasePage;

public class AccountBasePage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StorageBasePage.class.getName());

	@Inject
	protected BlazeStorage client;
	@Inject
	protected FacesContext facesContext;
	
	protected String key;
	protected TagsHolder tagsHolder = new TagsHolder();
	protected AccountUpdateRepresentation account = new AccountUpdateRepresentation();

	public String viewAction() {
		try {
			if (key != null || key.isEmpty()) {
				account = client.accounts().get(key).get();
				if (account == null) {
					tagsHolder.setTagEntries(new ArrayList<TagEntry>());
					facesContext.addMessage(null, new FacesMessage("No account found for key " + key));
					return null;
				} else {
					tagsHolder.setTags(account.getTags());
					return "";
				}
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty key!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load account"));
			LOG.log(Level.SEVERE, "Could not load account " + key, ex);
			return null;
		}
	}
	
	public void put() {
		AccountUpdateRepresentation newAccount = new AccountUpdateRepresentation();
		newAccount.setName(account.getName());
		newAccount.setTags(tagsHolder.getTags());
		client.accounts().get(key).put(newAccount);
		account = newAccount;
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

	public TagsHolder getTagsHolder() {
		return tagsHolder;
	}
}

package com.blazebit.storage.server.object;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.blazebit.storage.rest.model.StorageListElementRepresentation;

@Named
@ViewScoped
public class BucketObjectAddPage extends BucketObjectBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectAddPage.class.getName());

	private List<SelectItem> storageItems;

	public String add() {
		try {
			if (name != null || name.isEmpty()) {
				put();
				return "/bucket/detail.xhtml?account=" + account + "&name=" + name + "&faces-redirect=true";
			}

			String message = "Invalid empty name!";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			return null;
		} catch (RuntimeException ex) {
			String message = "Could not add bucket";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			LOG.log(Level.SEVERE, "Could not add bucket" + name, ex);
			return null;
		}
	}
	
	public void onAccountChanged() {
		if (bucket.getDefaultStorageOwner() == null) {
			storageItems = new ArrayList<>(0);
			return;
		}
		
		List<StorageListElementRepresentation> storages = client.accounts().get(bucket.getDefaultStorageOwner()).getStorages().get().getStorages();
		storageItems = new ArrayList<>(storages.size());
		
		for (StorageListElementRepresentation storage : storages) {
			storageItems.add(new SelectItem(storage.getName(), storage.getName()));
		}
	}

	public List<SelectItem> getStorageItems() {
		return storageItems;
	}
	
}

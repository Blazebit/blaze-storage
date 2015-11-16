package com.blazebit.storage.server.object;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.server.util.BucketObjectInputStream;

@Named
@ViewScoped
public class BucketObjectEditPage extends BucketObjectAddPage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectEditPage.class.getName());

	private String parent;
	
	@Override
	protected void init() {
		if (bucketObject != null) {
			bucketObject = new BucketObjectUpdateRepresentation(bucketObject);
			onAccountChanged();
			content = new BucketObjectInputStream(client, bucket, key);
		} else {
			content = null;
		}

		int slashIndex;
		if (bucketObject == null || key == null || key.isEmpty() || key.endsWith("/") || (slashIndex = key.lastIndexOf('/')) < 0) {
			this.parent = null;
		} else {
			this.parent = key.substring(0, slashIndex);
		}
	}
	
	public String getParent() {
		return parent;
	}
	
	public String update() {
		try {
			put();
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not update bucket object"));
			LOG.log(Level.SEVERE, "Could not update bucket object " + key, ex);
			return null;
		}
	}
	
}

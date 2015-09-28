package com.blazebit.storage.server.object;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.StorageListElementRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;
import com.blazebit.storage.server.util.LazyFileInputStream;

@Named
@ViewScoped
public class BucketObjectAddPage extends BucketObjectBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectAddPage.class.getName());

	private List<SelectItem> storageItems;
	
	protected InputStream content;
	protected String contentDisposition;
	protected String contentDispositionFilename;

	public String add() {
		try {
			if (bucket != null && !bucket.isEmpty() && key != null && !key.isEmpty()) {
				put();
				return "/object/detail.xhtml?bucket=" + bucket + "&key=" + key + "&faces-redirect=true";
			}

			String message = "Invalid empty key!";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			return null;
		} catch (RuntimeException ex) {
			String message = "Could not add bucket object";
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
			LOG.log(Level.SEVERE, "Could not add bucket object " + key, ex);
			return null;
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		UploadedFile uploadedFile = event.getFile();
		bucketObject.setContentType(uploadedFile.getContentType());
		bucketObject.setSize(uploadedFile.getSize());
		
		if (contentDispositionFilename == null || contentDispositionFilename.isEmpty()) {
			this.contentDispositionFilename = uploadedFile.getFileName();
		}
		
		try (InputStream is = event.getFile().getInputstream()) {
			Path tempFile = Files.createTempFile(null, null);
			Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
			this.content = new LazyFileInputStream(tempFile);
		} catch (IOException ex) {
			LOG.log(Level.SEVERE, "Could not locally save the file!", ex);
		}
	}

	@Override
	public void put() {
		if (contentDisposition == null || contentDisposition.isEmpty()) {
			bucketObject.setContentDisposition(null);
		} else if (contentDisposition.equals("inline")) {
			bucketObject.setContentDisposition(ContentDisposition.inline(contentDispositionFilename));
		} else if (contentDisposition.equals("attachement")) {
			bucketObject.setContentDisposition(ContentDisposition.attachment(contentDispositionFilename));
		}
		
		super.put();
	}

	public BucketObjectUpdateRepresentation getBucketObject() {
		return (BucketObjectUpdateRepresentation) bucketObject;
	}

	public InputStream getContent() {
		return content;
	}

	public StreamedContent getStreamContent() {
		if (content == null) {
			return null;
		} else {
			return new DefaultStreamedContent(content, bucketObject.getContentType(), bucketObject.getContentDisposition().getFilename(), null);
		}
	}
	
	public void onAccountChanged() {
		if (bucketObject.getStorageOwner() == null) {
			storageItems = new ArrayList<>(0);
			return;
		}
		
		List<StorageListElementRepresentation> storages = client.accounts().get(bucketObject.getStorageOwner()).getStorages().get().getStorages();
		storageItems = new ArrayList<>(storages.size());
		
		for (StorageListElementRepresentation storage : storages) {
			storageItems.add(new SelectItem(storage.getName(), storage.getName()));
		}
	}

	public List<SelectItem> getStorageItems() {
		return storageItems;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public String getContentDispositionFilename() {
		return contentDispositionFilename;
	}

	public void setContentDispositionFilename(String contentDispositionFilename) {
		this.contentDispositionFilename = contentDispositionFilename;
	}
	
}

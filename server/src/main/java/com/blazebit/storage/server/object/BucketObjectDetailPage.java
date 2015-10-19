package com.blazebit.storage.server.object;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;

@Named
@RequestScoped
public class BucketObjectDetailPage extends BucketObjectBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectDetailPage.class.getName());

	private String parent = "";
	
	@Override
	protected void init() {
		super.init();
		int slashIndex;
		if (bucketObject == null || key == null || key.isEmpty() || key.endsWith("/") || (slashIndex = key.lastIndexOf('/')) < 0) {
			this.parent = "";
		} else {
			this.parent = key.substring(0, slashIndex);
		}
	}
	
	public String getParent() {
		return parent;
	}
	
	public BucketObjectHeadRepresentation getBucketObject() {
		return (BucketObjectHeadRepresentation) bucketObject;
	}

	public StreamedContent getStreamContent() {
		if (bucketObject == null) {
			return null;
		} else {
			String filename;
			ContentDisposition disposition = bucketObject.getContentDisposition();
			if (disposition != null && disposition.getFilename() != null) {
				filename = disposition.getFilename();
			} else {
				filename = key.substring(key.lastIndexOf('/') + 1);
			}
			
			return new DefaultStreamedContent(getContent(), bucketObject.getContentType(), filename, null);
		}
	}
	
}

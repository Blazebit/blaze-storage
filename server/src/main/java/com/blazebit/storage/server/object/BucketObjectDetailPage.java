package com.blazebit.storage.server.object;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;

@Named
@RequestScoped
public class BucketObjectDetailPage extends BucketObjectBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectDetailPage.class.getName());

	private String parent = "";
	
	@Override
	protected void init() {
		super.init();
		if (bucketObject == null || key == null || key.isEmpty() || key.endsWith("/")) {
			this.parent = "";
		} else {
			this.parent = key.substring(0, key.lastIndexOf('/'));
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
			return new DefaultStreamedContent(getContent(), bucketObject.getContentType(), bucketObject.getContentDisposition().getFilename(), null);
		}
	}
	
}

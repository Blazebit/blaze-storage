package com.blazebit.storage.rest.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BucketRepresentation extends BucketHeadRepresentation {

	private static final long serialVersionUID = 1L;
	private List<BucketObjectListElementRepresentation> contents = new ArrayList<>(0);

	public BucketRepresentation() {
	}

	public BucketRepresentation(String defaultStorageOwner, String defaultStorageName, String name, String ownerKey, Calendar creationDate, String nextMarker, StatisticsRepresentation statistics, List<BucketObjectListElementRepresentation> contents) {
		super(defaultStorageOwner, defaultStorageName, name, ownerKey, creationDate, nextMarker, statistics);
		this.contents = contents;
	}

	public List<BucketObjectListElementRepresentation> getContents() {
		return contents;
	}

	public void setContents(List<BucketObjectListElementRepresentation> contents) {
		this.contents = contents;
	}

}

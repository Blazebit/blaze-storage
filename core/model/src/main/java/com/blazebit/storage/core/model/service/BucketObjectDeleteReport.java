package com.blazebit.storage.core.model.service;

import java.util.List;

public class BucketObjectDeleteReport {

	private final List<BucketObjectDeleteReportItem> items;

	public BucketObjectDeleteReport(List<BucketObjectDeleteReportItem> items) {
		this.items = items;
	}

	public List<BucketObjectDeleteReportItem> getItems() {
		return items;
	}
}

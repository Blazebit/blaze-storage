package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BucketRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String prefix;
	private Integer limit;
	private String marker;
	private boolean isTruncated;
	private StatisticsRepresentation statistics;
	private List<BucketObjectListElementRepresentation> contents = new ArrayList<>(0);

	public BucketRepresentation() {
	}
	
	public BucketRepresentation(String name, String prefix, Integer limit, String marker, boolean isTruncated, StatisticsRepresentation statistics, List<BucketObjectListElementRepresentation> contents) {
		this.name = name;
		this.prefix = prefix;
		this.limit = limit;
		this.marker = marker;
		this.isTruncated = isTruncated;
		this.statistics = statistics;
		this.contents = contents;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public boolean isTruncated() {
		return isTruncated;
	}

	public void setTruncated(boolean isTruncated) {
		this.isTruncated = isTruncated;
	}

	public StatisticsRepresentation getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsRepresentation statistics) {
		this.statistics = statistics;
	}

	public List<BucketObjectListElementRepresentation> getContents() {
		return contents;
	}

	public void setContents(List<BucketObjectListElementRepresentation> contents) {
		this.contents = contents;
	}

}

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
	private List<FileListElementRepresentation> contents = new ArrayList<>(0);

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

	public List<FileListElementRepresentation> getContents() {
		return contents;
	}

	public void setContents(List<FileListElementRepresentation> contents) {
		this.contents = contents;
	}

}

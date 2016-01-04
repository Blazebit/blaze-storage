package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipleDeleteRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean quiet;
	private List<MultipleDeleteObjectRepresentation> objects = new ArrayList<MultipleDeleteObjectRepresentation>(0);

	public MultipleDeleteRepresentation() {
	}

	public MultipleDeleteRepresentation(boolean quiet, List<MultipleDeleteObjectRepresentation> objects) {
		this.quiet = quiet;
		this.objects = objects;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public List<MultipleDeleteObjectRepresentation> getObjects() {
		return objects;
	}

	public void setObjects(List<MultipleDeleteObjectRepresentation> objects) {
		this.objects = objects;
	}
}

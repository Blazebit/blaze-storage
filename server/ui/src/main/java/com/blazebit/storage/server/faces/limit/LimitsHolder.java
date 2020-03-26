package com.blazebit.storage.server.faces.limit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class LimitsHolder implements Serializable {

	protected List<LimitEntry> limitEntries = new ArrayList<>();

	public Set<Integer> getLimits() {
		Set<Integer> limits = new TreeSet<>();
		FacesContext context = FacesContext.getCurrentInstance();
		boolean valid = true;
		
		for (LimitEntry limitEntry : limitEntries) {
			if (!limits.add(limitEntry.getValue())) {
				// TODO: I18N messages
				String validatorMessageString = "Duplicate limit found: " + limitEntry.getValue();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessageString, validatorMessageString);
                context.addMessage(null, message);
                valid = false;
			}
		}
		
		if (!valid) {
			throw new RuntimeException("Invalid limits!");
		}
		
		return limits;
	}
	
	public void setLimits(Set<Integer> limits) {
		limitEntries = new ArrayList<>(limits.size());
		for (Integer entry : limits) {
			limitEntries.add(new LimitEntry(entry));
		}
	}

	public List<LimitEntry> getLimitEntries() {
		return limitEntries;
	}

	public void setLimitEntries(List<LimitEntry> limitEntries) {
		this.limitEntries = limitEntries;
	}
}

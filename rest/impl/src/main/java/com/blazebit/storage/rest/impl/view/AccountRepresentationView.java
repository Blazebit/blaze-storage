package com.blazebit.storage.rest.impl.view;

import java.util.Map;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.rest.model.AccountRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Account.class)
public abstract class AccountRepresentationView extends AccountRepresentation {

	private static final long serialVersionUID = 1L;

	public AccountRepresentationView(
			@Mapping("name") String name,
			@Mapping("tags") Map<String, String> tags,
			@Mapping("key") String key) {
		super(name, tags, key);
	}
	
	@JsonIgnore
	@IdMapping("id")
	public abstract Long getId();
	
}

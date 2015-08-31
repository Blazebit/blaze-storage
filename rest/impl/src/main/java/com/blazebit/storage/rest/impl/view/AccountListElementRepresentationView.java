package com.blazebit.storage.rest.impl.view;

import java.util.Map;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Account.class)
public abstract class AccountListElementRepresentationView extends AccountListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public AccountListElementRepresentationView(
			@Mapping("key") String key,
			@Mapping("tags") Map<String, String> tags,
			@Mapping("name") String name) {
		super(key, tags, name);
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract Long getId();
	
}

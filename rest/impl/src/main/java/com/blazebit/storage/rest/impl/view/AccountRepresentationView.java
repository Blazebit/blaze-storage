package com.blazebit.storage.rest.impl.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.UserAccount;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

@EntityView(UserAccount.class)
public abstract class AccountRepresentationView extends AccountListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public AccountRepresentationView(
			@IdMapping("id") Long id, 
			@Mapping("key") String key, 
			@Mapping("name") String name) {
		super(key, name);
	}
	
}

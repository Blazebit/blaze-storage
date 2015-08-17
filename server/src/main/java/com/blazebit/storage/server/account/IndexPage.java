package com.blazebit.storage.server.account;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

@Named
@RequestScoped
public class IndexPage {

	@Inject
	private BlazeStorage storage;
	
	@Named("accountList")
	@RequestScoped
	public List<AccountListElementRepresentation> createAccountList() {
		return storage.accounts().get();
	}
}

package com.blazebit.storage.server.account;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

@Named
@RequestScoped
public class AccountIndexPage {

	@Inject
	private BlazeStorage client;
	
	@Produces
	@Named("accountList")
	@RequestScoped
	public List<AccountListElementRepresentation> createAccountList() {
		return client.accounts().get();
	}
}

package com.blazebit.storage.server.account;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

public class AccountSupport {

	@Inject
	private BlazeStorage client;
	@Inject
	@Named("accounts")
	List<AccountListElementRepresentation> accounts;
	
	@Produces
	@Named("accounts")
	@RequestScoped
	public List<AccountListElementRepresentation> getAccounts() {
		return client.accounts().get();
	}
	
	@Produces
	@Named("accountItems")
	@RequestScoped
	public List<SelectItem> getAccountItems(@Named("accounts") List<AccountListElementRepresentation> accounts) {
		List<SelectItem> accountItems = new ArrayList<>(accounts.size());
		
		for (AccountListElementRepresentation account : accounts) {
			accountItems.add(new SelectItem(account.getKey(), account.getName()));
		}
		
		return accountItems;
	}

	public String getAccountName(String ownerKey) {
		if (ownerKey == null || ownerKey.isEmpty()) {
			return null;
		}
		
		for (AccountListElementRepresentation element : accounts) {
			if (ownerKey.equals(element.getKey())) {
				return element.getName();
			}
		}
		
		return null;
	}
}

package com.blazebit.storage.server.account;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.blazebit.storage.rest.model.AccountRepresentation;

@Named
@RequestScoped
public class AccountDetailPage extends AccountBasePage {

	private static final long serialVersionUID = 1L;
	
	public AccountRepresentation getAccount() {
		return (AccountRepresentation) account;
	}
	
}

package com.blazebit.storage.core.impl.alert;

import com.blazebit.storage.core.api.spi.AlertContext;
import com.blazebit.storage.core.api.spi.AlertType;
import com.blazebit.storage.core.model.jpa.Account;

public class AlertContextImpl implements AlertContext {

	private final Account account;
	private final AlertType<?> type;
	private final Object payload;

	public AlertContextImpl(Account account, AlertType<?> type, Object payload) {
		this.account = account;
		this.type = type;
		this.payload = payload;
	}

	public Account getAccount() {
		return account;
	}

	public AlertType<?> getType() {
		return type;
	}

	public Object getPayload() {
		return payload;
	}
	
}

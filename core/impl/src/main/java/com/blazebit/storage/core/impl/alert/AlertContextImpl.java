package com.blazebit.storage.core.impl.alert;

import com.blazebit.storage.core.api.spi.AlertContext;
import com.blazebit.storage.core.api.spi.AlertType;
import com.blazebit.storage.core.model.jpa.UserAccount;

public class AlertContextImpl implements AlertContext {

	private final UserAccount userAccount;
	private final AlertType<?> type;
	private final Object payload;

	public AlertContextImpl(UserAccount userAccount, AlertType<?> type, Object payload) {
		this.userAccount = userAccount;
		this.type = type;
		this.payload = payload;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public AlertType<?> getType() {
		return type;
	}

	public Object getPayload() {
		return payload;
	}
	
}

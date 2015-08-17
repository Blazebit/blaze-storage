package com.blazebit.storage.core.api.spi;

import com.blazebit.storage.core.model.jpa.Account;

public interface AlertContext {

	/**
	 * The account for which this alert is.
	 * 
	 * @return the account
	 */
	public Account getAccount();
	
	/**
	 * The type of this alert.
	 * 
	 * @return the alert type
	 */
	public AlertType<?> getType();
	
	/**
	 * The payload of this alert. 
	 * The type of the payload depends on the alert type.
	 * 
	 * @return the payload
	 */
	public Object getPayload();
}

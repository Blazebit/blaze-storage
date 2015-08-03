package com.blazebit.storage.core.api.spi;

import com.blazebit.storage.core.model.jpa.Storage;

public interface AlertType<T> {

	public static final AlertType<Storage> QUOTA_PLAN_PERCENT_REACHED = new DefaultAltertType<Storage>("QUOTA_PLAN_PERCENT_REACHED");
	public static final AlertType<Storage> FLOATING_SWITCH = new DefaultAltertType<Storage>("FLOATING_SWITCH");

	public T getPayload(AlertContext context);
	
	public static final class DefaultAltertType<T> implements AlertType<T> {
		
		private final String name;

		public DefaultAltertType(String name) {
			this.name = name;
		}

		@SuppressWarnings("unchecked")
		public T getPayload(AlertContext context) {
			if (context.getType() != this) {
				throw new IllegalArgumentException("Expected type [" + this + "] but was [" + context.getType() + "]!");
			}
			
			return (T) context.getPayload();
		}

		@Override
		public String toString() {
			return name;
		}
	}
}

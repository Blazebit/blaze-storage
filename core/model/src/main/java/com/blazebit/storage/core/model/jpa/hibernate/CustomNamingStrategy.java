package com.blazebit.storage.core.model.jpa.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

import com.blazebit.storage.core.model.jpa.RdbmsConstants;

public class CustomNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = 1L;

	@Override
	public String tableName(String tableName) {
		return RdbmsConstants.PREFIX + super.tableName(tableName);
	}

	@Override
	public String classToTableName(String className) {
		return RdbmsConstants.PREFIX + super.classToTableName(className);
	}

	@Override
	public String logicalCollectionTableName(String tableName,
			String ownerEntityTable, String associatedEntityTable,
			String propertyName) {
		return RdbmsConstants.PREFIX
				+ super.logicalCollectionTableName(tableName, ownerEntityTable,
						associatedEntityTable, propertyName);
	}

	@Override
	public String foreignKeyColumnName(String propertyName,
			String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return super.foreignKeyColumnName(propertyName, propertyEntityName,
				propertyTableName, referencedColumnName) + "_" + referencedColumnName;
	}
}

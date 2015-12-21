package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;

@Stateless
public class StorageQuotaModelDataAccessImpl extends AbstractDataAccess implements StorageQuotaModelDataAccess {
	
	@Override
	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<StorageQuotaModel> cb = cbf.create(em, StorageQuotaModel.class);
		return evm.applySetting(setting, cb).getResultList();
	}
	
	@Override
	public StorageQuotaModel findById(String id) {
		if (id == null) {
			return null;
		}
		
		try {
			return cbf.create(em, StorageQuotaModel.class)
					.fetch("plans")
					.where("id").eq(id)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	@Override
	public StorageQuotaPlan findQuotaPlanById(StorageQuotaPlanId id) {
		if (id == null) {
			return null;
		}
		
		try {
			return cbf.create(em, StorageQuotaPlan.class)
					.where("id").eq(id)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public <T> T findById(String id, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		if (id == null) {
			return null;
		}
		
		try {
			CriteriaBuilder<StorageQuotaModel> cb =  cbf.create(em, StorageQuotaModel.class)
					.where("id").eq(id);
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}

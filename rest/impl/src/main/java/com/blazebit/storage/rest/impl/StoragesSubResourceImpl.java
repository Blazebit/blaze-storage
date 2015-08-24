package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.impl.view.StorageListElementRepresentationView;
import com.blazebit.storage.rest.model.OwnerRepresentation;
import com.blazebit.storage.rest.model.StorageListElementRepresentation;
import com.blazebit.storage.rest.model.StorageListRepresentation;

public class StoragesSubResourceImpl extends AbstractResource implements StoragesSubResource {

	private Account owner;
	
	@Inject
	private StorageDataAccess storageDataAccess;
	
	public StoragesSubResourceImpl(Account owner) {
		this.owner = owner;
	}

	@Override
	@SuppressWarnings("unchecked")
	public StorageListRepresentation get() {
		List<StorageListElementRepresentation> list = (List<StorageListElementRepresentation>) (List<?>) storageDataAccess.findAllByAccountId(owner.getId(), EntityViewSetting.create(StorageListElementRepresentationView.class));
		OwnerRepresentation ownerRepresentation = new OwnerRepresentation(owner.getKey(), owner.getName());
		StorageListRepresentation result = new StorageListRepresentation(ownerRepresentation, list);
		return result;
	}

	@Override
	public StorageSubResource get(String storageName) {
		return inject(new StorageSubResourceImpl(owner, storageName));
	}

}

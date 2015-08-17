package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.User;

public interface UserService {

	public void create(User user);

	public void delete(String userId);
}

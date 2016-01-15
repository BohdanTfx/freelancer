package com.epam.test.dao;

import com.epam.freelancer.model.UserEntity;

public interface UserDao<T extends UserEntity> extends
		GenericDao<T, Integer>
{
	T getByEmail(String email);

	boolean emailAvailable(String email);

	T getByUUID(String uuid);
}

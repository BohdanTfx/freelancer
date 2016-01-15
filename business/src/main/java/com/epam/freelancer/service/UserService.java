package com.epam.freelancer.service;

import com.epam.freelancer.model.UserEntity;
import com.epam.test.dao.GenericDao;
import com.epam.test.dao.UserDao;

public abstract class UserService<T extends UserEntity> extends
		GenericService<T, Integer>
{

	public UserService(GenericDao<T, Integer> genericDao) {
		super(genericDao);
	}

	public T findByEmail(String email) {
		if (email == null || email.isEmpty()) {
			return null;
		}
		return ((UserDao<T>) genericDao).getByEmail(email);
	}

	public T getByUUID(String uuid) {
		if (uuid == null || uuid.isEmpty()) {
			return null;
		}
		return ((UserDao<T>) genericDao).getByUUID(uuid);
	}

	public boolean emailAvailable(String email) {
		return ((UserDao<T>) genericDao).emailAvailable(email);
	}
}
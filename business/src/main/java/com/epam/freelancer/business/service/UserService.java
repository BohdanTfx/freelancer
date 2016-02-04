package com.epam.freelancer.business.service;

import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.UserDao;
import com.epam.freelancer.database.model.UserEntity;

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

	public T findByUUID(String uuid) {
		if (uuid == null || uuid.isEmpty()) {
			return null;
		}
		return ((UserDao<T>) genericDao).getByUUID(uuid);
	}

	public boolean emailAvailable(String email) {
		return ((UserDao<T>) genericDao).emailAvailable(email);
	}

	public boolean uuidAvailable(String uuid) {
		return ((UserDao<T>) genericDao).uuidAvailable(uuid);
	}
}

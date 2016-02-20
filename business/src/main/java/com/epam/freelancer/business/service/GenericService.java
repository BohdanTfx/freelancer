package com.epam.freelancer.business.service;

import com.epam.freelancer.business.encode.Encryption;
import com.epam.freelancer.business.encode.SHA256Util;
import com.epam.freelancer.business.encode.SaltUtil;
import com.epam.freelancer.business.util.ValidationParametersBuilder.Parameters;
import com.epam.freelancer.business.util.Validator;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.BaseEntity;
import com.epam.freelancer.database.model.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GenericService<T extends BaseEntity<ID>, ID> implements
		Service<T, ID>
{
	protected GenericDao<T, ID> genericDao;

	public GenericService(GenericDao<T, ID> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public abstract T create(Map<String, String[]> data);

	@Override
	public T modify(T entity) {
		return genericDao.update(entity);
	}

	@Override
	public void remove(T entity) {
		genericDao.delete(entity);
	}

	@Override
	public T findById(ID id) {
		return genericDao.getById(id);
	}

	@Override
	public List<T> findAll() {
		return genericDao.getAll();
	}

	public Integer getObjectAmount() {
		return genericDao.getObjectNumber();
	}

	/*
	 * This method should be overwritten if U need filtration
	 */
	@Override
	public List<T> filterElements(Map<String, Object> parameters,
			Integer start, Integer step)
	{
		return genericDao.filterAll(parameters, start, step);
	}

	public Integer getFilteredObjectNumber(Map<String, Object> parameters) {
		return genericDao.getFilteredObjectNumber(parameters);
	}

	@Override
	public boolean isDataValid(Map<Parameters, String> data) {
		if (data == null || data.size() == 0)
			return false;

		for (Entry<Parameters, String> entry : data.entrySet()) {
			if (Validator.validate(entry.getKey(), entry.getValue()) == false)
				return false;
		}

		return true;
	}

	@Override
	public void encodePassword(UserEntity userEntity) {
		String password = userEntity.getPassword();
		if (password != null) {
			String salt = SaltUtil.createSalt();
			String hashPass = new Encryption(new SHA256Util()).crypt(password,
					salt);
			userEntity.setPassword(hashPass);
			userEntity.setSalt(salt);
		}
	}
}

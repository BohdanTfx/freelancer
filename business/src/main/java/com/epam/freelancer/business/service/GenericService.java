package com.epam.freelancer.business.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.epam.freelancer.business.util.ValidationParametersBuilder.Parameters;
import com.epam.freelancer.business.util.Validator;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.BaseEntity;

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

}

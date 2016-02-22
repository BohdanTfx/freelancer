package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder.Parameters;
import com.epam.freelancer.database.model.BaseEntity;
import com.epam.freelancer.database.model.UserEntity;

import java.util.List;
import java.util.Map;

public interface Service<T extends BaseEntity<ID>, ID> {
	T create(Map<String, String[]> data);

	T modify(T entity);

	void remove(T entity);

	T findById(ID id);

	T findByIdEvenDeleted(ID id);

	List<T> findAll();

	public Integer getObjectAmount();

	List<T> filterElements(Map<String, Object> parameters, Integer start,
			Integer step);

	Integer getFilteredObjectNumber(Map<String, Object> parameters);

	void encodePassword(UserEntity userEntity);

	boolean isDataValid(Map<Parameters, String> data);
}

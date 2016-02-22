package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.persistence.ConnectionPool;

import java.util.List;
import java.util.Map;

public interface GenericDao<T, ID> {
	String NOT_DELETED = "is_deleted IS NOT TRUE";

	T save(T entity);

	T update(T entity);

	void delete(T entity);

	T getById(ID id);

	T getByIdEvenDeleted(ID id);

	List<T> getAll();

	List<T> getAllWithDeleted();

	List<T> filterAll(Map<String, Object> parameters, Integer start,
			Integer step);
	
	Integer getFilteredObjectNumber(Map<String, Object> parameters);
	
	Integer getObjectNumber();

	void setConnectionPool(ConnectionPool connectionPool);
}

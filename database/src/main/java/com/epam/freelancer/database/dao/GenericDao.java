package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.persistence.ConnectionPool;

import java.util.List;
import java.util.Map;

public interface GenericDao<T, ID> {

	T save(T entity);

	T update(T entity);

	void delete(T entity);

	T getById(ID id);

	List<T> getAll();

	List<T> filterAll(Map<String, String> parameters, Integer start,
			Integer step);

	void setConnectionPool(ConnectionPool connectionPool);
}
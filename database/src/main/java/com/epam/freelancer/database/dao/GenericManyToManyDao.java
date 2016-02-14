package com.epam.freelancer.database.dao;

import java.util.List;

import com.epam.freelancer.database.model.BaseEntity;
import com.epam.freelancer.database.persistence.ConnectionPool;

public interface GenericManyToManyDao<F extends BaseEntity<ID>, S extends BaseEntity<ID>, M extends BaseEntity<ID>, ID>
{
	List<S> getBasedOnFirst(ID firstId);

	List<F> getBasedOnSecond(ID secondId);

	// public List<ObjectHolder<M, S>> getFullEntityForFirst(ID firstId);
	//
	// public List<ObjectHolder<M, F>> getFullEntityForSecond(ID secondId);

	void saveContact(ID firstId, ID secondId);

	void removeContact(ID firstId, ID secondId);

	public void setConnectionPool(ConnectionPool connectionPool);
}

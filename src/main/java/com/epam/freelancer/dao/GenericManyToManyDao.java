package com.epam.freelancer.dao;

import com.epam.freelancer.model.BaseEntity;

import javax.sql.DataSource;
import java.util.List;

public interface GenericManyToManyDao<F extends BaseEntity<ID>, S extends BaseEntity<ID>, ID>
{
	List<S> getBasedOnFirst(ID firstId);

	List<F> getBasedOnSecond(ID secondId);

	void saveContact(ID firstId, ID secondId);

    void setDataSource(DataSource dataSource);
}

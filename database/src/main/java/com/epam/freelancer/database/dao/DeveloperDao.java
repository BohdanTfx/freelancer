package com.epam.freelancer.database.dao;

import java.util.Map;

import com.epam.freelancer.database.model.Developer;

/**
 * Created by ������ on 17.01.2016.
 */
public interface DeveloperDao extends UserDao<Developer> {

	Double getPaymentLimit(String limitType);

	Integer getFilteredObjectNumber(Map<String, Object> parameters);
}

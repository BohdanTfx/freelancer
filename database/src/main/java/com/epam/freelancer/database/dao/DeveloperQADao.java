package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.DeveloperQA;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface DeveloperQADao extends GenericDao<DeveloperQA, Integer> {
	List<DeveloperQA> getDevQAByDevId(Integer id);

	DeveloperQA getByDevIdAndTestId(Integer devId, Integer testId);
}

package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Test;

import java.util.List;
import java.util.Map;

public interface TestDao extends GenericDao<Test, Integer> {
	List<Test> getTestsByTechId(Integer id);

	List<Test> getTestsByAdminId(Integer id);

	Map<Test,Integer> getPopularTests();
}

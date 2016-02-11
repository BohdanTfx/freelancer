package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.TestDao;
import com.epam.freelancer.database.model.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TestJdbcDao extends GenericJdbcDao<Test, Integer> implements TestDao {

	public TestJdbcDao() throws Exception {
		super(Test.class);
	}

	@Override
	public List<Test> getTestsByTechId(Integer id) {
		List<Test> tests = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE is_deleted IS NOT TRUE";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
					 .prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Test test;
					test = transformer.getObject(set);
					tests.add(test);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tests;
	}

	@Override
	public List<Test> getTestsByAdminId(Integer id) {
		List<Test> tests = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE admin_id = ? AND is_deleted IS NOT TRUE";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
					 .prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Test test;
					test = transformer.getObject(set);
					tests.add(test);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tests;
	}
}

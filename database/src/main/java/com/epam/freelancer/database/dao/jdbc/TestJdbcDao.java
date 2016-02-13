package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.TestDao;
import com.epam.freelancer.database.model.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public Map<Test,Integer> getPopularTests(){
		Map<Test,Integer> map = new HashMap<>();
		String query = " SELECT dq.test_id as id,t.tech_id,t.name,t.admin_id,t.pass_score,t.sec_per_quest,t.version,t.is_deleted,COUNT(t.id) AS amount" +
				" FROM developer_qa dq JOIN test t ON dq.test_id = t.id " +
				"  GROUP BY dq.test_id  " +
				" ORDER BY COUNT(t.id) DESC LIMIT 5";
		try (Connection connection = connectionPool.getConnection();
			 PreparedStatement statement = connection
					 .prepareStatement(query)) {
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Test test;
					test = transformer.getObject(set);
					map.put(test,set.getInt("amount"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

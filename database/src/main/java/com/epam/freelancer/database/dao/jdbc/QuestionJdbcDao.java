package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.QuestionDao;
import com.epam.freelancer.database.model.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionJdbcDao extends GenericJdbcDao<Question, Integer> implements QuestionDao {

	public QuestionJdbcDao() throws Exception {
		super(Question.class);
	}

	@Override
	public List<Question> getByTechnology(Integer id) {
		List<Question> entities = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE tech_id = ? AND is_deleted <> 1";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Question entity = new Question();
					entity = transformer.getObject(set);
					entities.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	@Override
	public List<Question> getByAdminId(Integer id) {
		List<Question> entities = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE admin_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
					 .prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Question entity = new Question();
					entity = transformer.getObject(set);
					entities.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}
}

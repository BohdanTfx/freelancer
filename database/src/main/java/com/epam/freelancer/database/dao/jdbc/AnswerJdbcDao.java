package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.AnswerDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public class AnswerJdbcDao extends GenericJdbcDao<Answer, Integer> implements
		AnswerDao
{
	public AnswerJdbcDao() throws Exception {
		super(Answer.class);
	}

	@Override
	public List<Answer> getAnswersByQuestionId(Integer id) {
		List<Answer> answers = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE quest_id = ? AND "
				+ GenericDao.NOT_DELETED;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Answer answer;
					answer = transformer.getObject(set);
					answers.add(answer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answers;
	}
}

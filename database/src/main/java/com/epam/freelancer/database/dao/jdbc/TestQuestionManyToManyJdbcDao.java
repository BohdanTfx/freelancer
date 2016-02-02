package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.TestQuestionManyToManyDao;
import com.epam.freelancer.database.model.Question;
import com.epam.freelancer.database.model.Test;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.database.transformer.DataTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TestQuestionManyToManyJdbcDao extends
        GenericJdbcManyToManyDao<Test, Question, Worker, Integer>
        implements TestQuestionManyToManyDao {

    public TestQuestionManyToManyJdbcDao() throws Exception {
        super("test_question", "test", "question", "test_id", "quest_id",
                new DataTransformer<>(Test.class), new DataTransformer<>(
                        Question.class), new DataTransformer<>(Worker.class));
    }

    @Override
    public List<Question> getQuestionsByTestId(Integer id) {
        List<Question> entities = new ArrayList<>();
        String query = "SELECT " + secondTable + ".* FROM " + secondTable
                + ", " + table + " WHERE " + secondTable + ".id = " + table
                + "." + secondIdName + " AND " + table + "." + firstIdName;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setObject(1, id);
            statement.setObject(2, false);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    entities.add(secondTransformer.getObject(set));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entities;
    }
}

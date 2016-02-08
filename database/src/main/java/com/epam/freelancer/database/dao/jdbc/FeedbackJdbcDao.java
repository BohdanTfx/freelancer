package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.FeedbackDao;
import com.epam.freelancer.database.model.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public class FeedbackJdbcDao extends GenericJdbcDao<Feedback, Integer> implements FeedbackDao {
    public FeedbackJdbcDao() throws Exception {
        super(Feedback.class);
    }

    @Override
    public List<Feedback> getFeedbacksByDevId(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE dev_id = ?";
        return getFeedbacksByQuery(query, id);
    }

    @Override
    public List<Feedback>  getFeedbacksByCustId(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE cust_id = ?";
        return getFeedbacksByQuery(query, id);
    }

    @Override
    public List<Feedback> getFeedbacksByCustIdForHim(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE cust_id = ? AND author = 'dev'";
        return getFeedbacksByQuery(query, id);
    }

    @Override
    public List<Feedback> getFeedbacksByDevIdForHim(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND author = 'customer'";
        return getFeedbacksByQuery(query, id);
    }

    @Override
    public int deleteDevFeed(Integer devId, Integer feedId) {
        int res = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("delete from feedback where dev_id = ? && id = ?")) {
            statement.setObject(1, devId);
            statement.setObject(2, feedId);
            res = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public int deleteCustFeed(Integer custId, Integer feedId) {
        int res = 0;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("delete from feedback where cust_id = ? && id = ?")) {
            statement.setObject(1, custId);
            statement.setObject(2, feedId);
            res = statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public Integer getAvgRate(Integer dev_id) {
        Integer avg = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT AVG(rate) from feedback WHERE dev_id = ? ;")) {
            statement.setObject(1, dev_id);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    avg = set.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return avg;
    }

    private List<Feedback>  getFeedbacksByQuery(String query, Integer id) {
        List<Feedback> feedbacks = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    Feedback feedback;
                    feedback = transformer.getObject(set);
                    feedbacks.add(feedback);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
}

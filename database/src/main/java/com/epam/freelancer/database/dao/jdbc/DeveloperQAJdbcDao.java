package com.epam.freelancer.database.dao.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.DeveloperQADao;
import com.epam.freelancer.database.model.DeveloperQA;

/**
 * Created by ������ on 17.01.2016.
 */
public class DeveloperQAJdbcDao extends GenericJdbcDao<DeveloperQA, Integer> implements DeveloperQADao {
    public DeveloperQAJdbcDao() throws Exception {
        super(DeveloperQA.class);
    }

    @Override
    public List<DeveloperQA> getDevQAByDevId(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE dev_id = ?";
        List<DeveloperQA> devsQA = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    DeveloperQA devQA = transformer.getObject(set);
                    devsQA.add(devQA);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devsQA;
    }

    @Override
    public DeveloperQA getByDevIdAndTestId(Integer devId, Integer testId) {
        String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND test_id = ?";
        DeveloperQA entity = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setInt(1, devId);
            statement.setInt(2, testId);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    entity = transformer.getObject(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}

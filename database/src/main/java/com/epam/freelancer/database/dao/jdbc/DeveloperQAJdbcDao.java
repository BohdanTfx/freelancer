package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.DeveloperQADao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.DeveloperQA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
}

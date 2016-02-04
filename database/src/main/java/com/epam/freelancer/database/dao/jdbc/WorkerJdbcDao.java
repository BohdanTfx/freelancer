package com.epam.freelancer.database.dao.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.epam.freelancer.database.dao.WorkerDao;
import com.epam.freelancer.database.model.Worker;

/**
 * Created by ������ on 17.01.2016.
 */
public class WorkerJdbcDao extends GenericJdbcDao<Worker, Integer> implements WorkerDao {
    public WorkerJdbcDao() throws Exception {
        super(Worker.class);
    }

    @Override
    public Worker getWorkerByDevIdAndOrderId(Integer idDev, Integer idOrder) {
        String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND order_id = ?";
        return getContactByQuery(query, idDev,idOrder);
    }

    private Worker getContactByQuery(String query, Integer firstId,Integer secondId) {
        Worker worker = null;
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            if(firstId!=null && secondId!=null){
                statement.setInt(1, firstId);
                statement.setInt(2, secondId);
            }
            if(firstId!=null && secondId==null || firstId==null && secondId!=null){
                statement.setInt(1, firstId);
            }
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    worker = transformer.getObject(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worker;
    }


}

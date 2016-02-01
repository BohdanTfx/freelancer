package com.epam.freelancer.database.dao.jdbc;


import com.epam.freelancer.database.dao.WorkerDao;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by ������ on 17.01.2016.
 */
public class WorkerJdbcDao extends GenericJdbcDao<Worker, Integer> implements WorkerDao {
    public WorkerJdbcDao() throws Exception {
        super(Worker.class);
    }


}

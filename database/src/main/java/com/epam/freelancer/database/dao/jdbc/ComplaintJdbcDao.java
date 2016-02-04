package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.ComplaintDao;
import com.epam.freelancer.database.model.Complaint;

/**
 * Created by spock on 04.02.16.
 */
public class ComplaintJdbcDao extends GenericJdbcDao<Complaint, Integer> implements ComplaintDao {
    public ComplaintJdbcDao() throws Exception {
        super(Complaint.class);
    }
}

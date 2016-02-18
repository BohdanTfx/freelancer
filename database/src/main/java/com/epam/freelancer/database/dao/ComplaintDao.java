package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Complaint;

import java.util.List;
import java.util.Map;

/**
 * Created by spock on 04.02.16.
 */
public interface ComplaintDao extends GenericDao<Complaint, Integer> {

    List<Complaint> getByDevId(Integer devId);

    Complaint isAlreadyExist(Integer devId, Integer orderId);
}

package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Complaint;

import java.util.List;

/**
 * Created by spock on 04.02.16.
 */
public interface ComplaintDao extends GenericDao<Complaint, Integer> {

    List<Complaint> getByDevId(Integer devId);
}

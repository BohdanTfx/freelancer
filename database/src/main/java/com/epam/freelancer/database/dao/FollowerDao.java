package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Follower;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface FollowerDao extends GenericDao<Follower, Integer> {
    List<Follower> getProjectFollowers(Integer id);
}

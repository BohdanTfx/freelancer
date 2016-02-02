package com.epam.freelancer.database.dao;

import java.util.List;

import com.epam.freelancer.database.model.Follower;

/**
 * Created by ������ on 17.01.2016.
 */
public interface FollowerDao extends GenericDao<Follower, Integer> {

	List<Follower> getDeveloperFollowings(Integer developerId);

	List<Follower> getCustomerInvitation(Integer customerId);
    List<Follower> getProjectFollowers(Integer id);
}

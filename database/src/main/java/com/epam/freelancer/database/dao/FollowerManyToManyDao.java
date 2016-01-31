package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Follower;
import com.epam.freelancer.database.model.Ordering;

import java.util.List;

public interface FollowerManyToManyDao extends
		GenericManyToManyDao<Developer, Ordering, Follower, Integer>
{
	List<Ordering> getDevSubscribedProjects(Integer devId);
}

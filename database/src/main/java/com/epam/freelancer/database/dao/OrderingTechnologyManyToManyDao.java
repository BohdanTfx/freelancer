package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;

public interface OrderingTechnologyManyToManyDao extends
		GenericManyToManyDao<Ordering, Technology, Worker, Integer>
{

}

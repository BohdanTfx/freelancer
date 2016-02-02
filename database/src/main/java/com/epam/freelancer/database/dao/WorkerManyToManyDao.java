package com.epam.freelancer.database.dao;

import java.util.List;

import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Worker;

/**
 * Created by Максим on 18.01.2016.
 */
public interface WorkerManyToManyDao extends
		GenericManyToManyDao<Developer, Ordering, Worker, Integer>
{
	List<Ordering> getPortfolio(Integer devId);

	List<Ordering> getOrdersByDevId(Integer devId);

	List<Developer> getDevsByOrderingId(Integer orderId);
}

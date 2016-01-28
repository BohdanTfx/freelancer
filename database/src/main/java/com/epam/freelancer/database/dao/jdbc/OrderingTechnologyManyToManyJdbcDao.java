package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.OrderingTechnologyManyToManyDao;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.database.transformer.DataTransformer;

public class OrderingTechnologyManyToManyJdbcDao extends
		GenericJdbcManyToManyDao<Ordering, Technology, Worker, Integer>
		implements OrderingTechnologyManyToManyDao
{

	public OrderingTechnologyManyToManyJdbcDao() throws Exception {
		super("ordering_technology", "ordering", "technology", "order_id",
				"tech_id", new DataTransformer<>(Ordering.class),
				new DataTransformer<>(Technology.class), new DataTransformer<>(
						Worker.class));
	}

}

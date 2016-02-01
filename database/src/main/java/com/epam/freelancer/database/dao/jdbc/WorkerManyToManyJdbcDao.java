package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.WorkerManyToManyDao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.database.transformer.DataTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Максим on 18.01.2016.
 */
public class WorkerManyToManyJdbcDao extends
		GenericJdbcManyToManyDao<Developer, Ordering, Worker, Integer>
		implements WorkerManyToManyDao
{
	public WorkerManyToManyJdbcDao() throws Exception {
		super("worker", "developer", "ordering", "dev_id", "order_id",
				new DataTransformer<>(Developer.class), new DataTransformer<>(
						Ordering.class), new DataTransformer<>(Worker.class));
	}

	@Override
	public List<Ordering> getPortfolio(Integer devId) {
		List<Ordering> entities = new ArrayList<>();
		String query = "SELECT " + secondTable + ".* FROM " + secondTable
				+ ", " + table + " WHERE " + secondTable + ".id = " + table
				+ "." + secondIdName + " AND " + table + "." + firstIdName
				+ " = ? AND private = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setObject(1, devId);
			statement.setObject(2, false);
				try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					entities.add(secondTransformer.getObject(set));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}



	@Override
	public List<Ordering> getOrdersByDevId(Integer devId) {
		return getBasedOnFirst(devId);
	}

	@Override
	public List<Developer> getDevsByOrderingId(Integer orderId) {
		return getBasedOnSecond(orderId);
	}
}

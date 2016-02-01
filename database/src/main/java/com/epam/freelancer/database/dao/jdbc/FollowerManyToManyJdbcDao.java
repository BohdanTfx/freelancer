package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.FollowerManyToManyDao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Follower;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.transformer.DataTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FollowerManyToManyJdbcDao extends
		GenericJdbcManyToManyDao<Developer, Ordering, Follower, Integer>
		implements FollowerManyToManyDao
{
	public FollowerManyToManyJdbcDao() throws Exception {
		super("follower", "developer", "ordering", "dev_id", "order_id",
				new DataTransformer<>(Developer.class), new DataTransformer<>(
						Ordering.class), new DataTransformer<>(Follower.class));
	}


	@Override
	public List<Ordering> getDevSubscribedProjects(Integer devId) {
		List<Ordering> entities = new ArrayList<>();
		String query = "SELECT " + secondTable + ".* FROM " + secondTable
				+ ", " + table + " WHERE " + secondTable + ".id = " + table
				+ "." + secondIdName + " AND " + table + "." + firstIdName
				+ " = ?";
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection
					 .prepareStatement(query)) {
			statement.setObject(1, devId);
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
}

package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.model.Ordering;

/**
 * Created by ������ on 17.01.2016.
 */
public class OrderingJdbcDao extends GenericJdbcDao<Ordering, Integer>
		implements OrderingDao
{
	public OrderingJdbcDao() throws Exception {
		super(Ordering.class);
	}

	@Override
	public Double getPayment(String paymentType, String limit) {
		if (limit == null || limit.isEmpty() || (!limit.equalsIgnoreCase("max")
				&& !limit.equalsIgnoreCase("min")))
			throw new RuntimeException("Incorrect type");
		String query = "SELECT " + limit + "(payment) FROM " + table
				+ " Where pay_type Like ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query);) {
			statement.setString(1, paymentType);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next())
					return set.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

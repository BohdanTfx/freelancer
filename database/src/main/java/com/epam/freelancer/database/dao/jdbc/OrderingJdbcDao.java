package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.model.Ordering;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		if (limit == null
				|| limit.isEmpty()
				|| (!limit.equalsIgnoreCase("max") && !limit
						.equalsIgnoreCase("min")))
			throw new RuntimeException("Incorrect type");
		String query = "SELECT " + limit + "(payment) FROM " + table
				+ " Where pay_type Like ?";
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
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

	@Override
	public List<Ordering> getAvailableCustOrders(Integer custId) {
		List<Ordering> orderings = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM ordering WHERE started = 0 AND ended = 0 AND customer_id = ? ;"))
		{
			statement.setObject(1, custId);
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				Ordering o = new Ordering();
				o.setId(set.getInt("id"));
				o.setTitle(set.getString("title"));

				orderings.add(o);
			}
			return orderings;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return orderings;
	}

	@Override
	public List<Ordering> getAllCustOrders(Integer custId) {
		List<Ordering> orderings = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM ordering WHERE customer_id = ? ;"))
		{
			statement.setObject(1, custId);
			ResultSet set = statement.executeQuery();
			while (set.next()) {
				Ordering order = transformer.getObject(set);
				orderings.add(order);
			}
			return orderings;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return orderings;
	}

	@Override
	public Integer getFilteredObjectNumber(Map<String, Object> parameters) {
		String query = null;
		if (parameters == null || parameters.isEmpty()) {
			query = "SELECT DISTINCT * FROM " + table;
			query = query.replace("SELECT DISTINCT *",
					"SELECT DISTINCT count(*)");
		} else {
			query = createFilterQuery(parameters, null, null);
			if (query.contains("SELECT DISTINCT " + table + ".*"))
				query = query.replace("SELECT DISTINCT " + table + ".*",
						"SELECT DISTINCT count(*), " + table + ".id");
			else
				query = query.replace("SELECT DISTINCT *",
						"SELECT DISTINCT count(*), " + table + ".id");

			query = query + " GROUP BY " + table + ".id";

		}

		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query);
				ResultSet set = statement.executeQuery()) {
			if (set.next())
				return set.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Ordering> getCustomerPublicHistory(Integer custId) {
		String query = "SELECT * FROM " + table
				+ " WHERE customer_id = ? AND ended = 1 AND private = 0";
		List<Ordering> orders = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, custId);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Ordering order = transformer.getObject(set);
					orders.add(order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	@Override
	public List<Ordering> filterAll(Map<String, Object> parameters,
			Integer start, Integer step)
	{
		List<Ordering> entities = new ArrayList<>();
		String query = null;
		if (parameters == null || parameters.isEmpty())
			query = "SELECT * FROM " + table + " ORDER BY date DESC Limit "
					+ start + ", " + step;
		else {
			query = createFilterQuery(parameters, start, step);
		}

		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query);
				ResultSet set = statement.executeQuery()) {
			while (set.next()) {
				Ordering entity = transformer.getObject(set);
				entities.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	private String createFilterQuery(Map<String, Object> parameters,
			Integer start, Integer step)
	{
		StringBuilder builder = new StringBuilder("SELECT DISTINCT ");
		boolean lastNull = true;

		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) parameters.get("technology");
		if (ids != null) {
			builder.append(table);
			builder.append(".* FROM ");
			builder.append(table);
			builder.append(", ordering_technology ");
			builder.append("WHERE ordering.id = ");
			builder.append("ordering_technology.order_id ");
			builder.append("AND ordering_technology.tech_id IN (");
			for (Integer integer : ids) {
				builder.append(integer);
				builder.append(",");
			}
			builder.deleteCharAt(builder.toString().length() - 1);
			builder.append(") ");

			lastNull = false;
		} else {
			builder.append("* FROM ");
			builder.append(table);
			builder.append(" WHERE ");

			lastNull = true;
		}

		List<?> zone = (List<?>) parameters.get("zone");
		if (zone != null && zone.size() > 0) {
			builder.append(lastNull ? "" : " AND");
			builder.append(" zone IN (");
			for (Object object : zone) {
				builder.append(object);
				builder.append(",");
			}
			builder.deleteCharAt(builder.toString().length() - 1);
			builder.append(") ");

			lastNull = false;
		}

		boolean hourly = parameters.get("hourly") != null;
		boolean fixed = parameters.get("fixed") != null;
		Number hMax = (Number) parameters.get("hmax");
		Number hMin = (Number) parameters.get("hmin");
		Number fMax = (Number) parameters.get("fmax");
		Number fMin = (Number) parameters.get("fmin");

		if (fixed && hourly) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" pay_type IN ('hourly', 'fixed') AND ((payment >= ");
			builder.append(hMin);
			builder.append(" AND payment <= ");
			builder.append(hMax);
			builder.append(") OR (payment >= ");
			builder.append(fMin);
			builder.append(" AND payment <= ");
			builder.append(fMax);
			builder.append(")) ");

			lastNull = false;
		} else {
			if (fixed) {
				builder.append(lastNull ? "" : " AND");

				builder.append(" pay_type LIKE 'fixed' AND payment >= ");
				builder.append(fMin);
				builder.append(" AND payment <= ");
				builder.append(fMax);

				lastNull = false;
			}
			if (hourly) {
				builder.append(lastNull ? "" : " AND");

				builder.append(" pay_type LIKE 'hourly' AND payment >= ");
				builder.append(hMin);
				builder.append(" AND payment <= ");
				builder.append(hMax);

				lastNull = false;
			}
		}

		String string = (String) parameters.get("title");

		if (string != null && !string.isEmpty()) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" title LIKE '%");
			builder.append(string);
			builder.append("%'");
		}

		if (start != null && step != null) {
			builder.append(" ORDER BY date DESC LIMIT ");
			builder.append(start);
			builder.append(",");
			builder.append(step);
		}

		return builder.toString();
	}
}

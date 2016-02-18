package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epam.freelancer.database.dao.DeveloperDao;
import com.epam.freelancer.database.model.Developer;

/**
 * Created by ������ on 17.01.2016.
 */
public class DeveloperJdbcDao extends UserJdbcDao<Developer, Integer> implements
		DeveloperDao
{
	public DeveloperJdbcDao() throws Exception {
		super(Developer.class);
	}

	@Override
	public Double getPaymentLimit(String limitType) {
		if (limitType == null
				|| limitType.isEmpty()
				|| (!limitType.equalsIgnoreCase("max") && !limitType
						.equalsIgnoreCase("min")))
			throw new RuntimeException("Incorrect type");
		String query = "SELECT " + limitType + "(hourly) FROM " + table;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query);
				ResultSet set = statement.executeQuery()) {
			if (set.next())
				return set.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
						"SELECT DISTINCT count(*) ");
			else
				query = query.replace("SELECT DISTINCT *",
						"SELECT DISTINCT count(*) ");
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
	public List<Developer> filterAll(Map<String, Object> parameters,
			Integer start, Integer step)
	{
		List<Developer> entities = new ArrayList<>();
		String query = null;
		if (parameters == null || parameters.isEmpty())
			query = "SELECT * FROM " + table + " Limit " + start + ", " + step;
		else {
			query = createFilterQuery(parameters, start, step);
		}

		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query);
				ResultSet set = statement.executeQuery()) {
			while (set.next()) {
				Developer entity = transformer.getObject(set);
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
			builder.append(", dev_tech ");
			builder.append("WHERE developer.id = ");
			builder.append("dev_tech.dev_id ");
			builder.append("AND dev_tech.tech_id IN (");
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

		Number max = (Number) parameters.get("paymentMax");
		Number min = (Number) parameters.get("paymentMin");

		if (max != null && min != null) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" hourly >= ");
			builder.append(min);
			builder.append(" AND hourly <= ");
			builder.append(max);
			builder.append(" ");

			lastNull = false;
		}

		String position = (String) parameters.get("position");

		if (position != null && !position.isEmpty()) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" position LIKE '%");
			builder.append(position);
			builder.append("%'");
		}
		String name = (String) parameters.get("name");

		if (name != null && !name.isEmpty()) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" name LIKE '%");
			builder.append(name);
			builder.append("%'");
		}
		String lastname = (String) parameters.get("last_name");

		if (lastname != null && !lastname.isEmpty()) {
			builder.append(lastNull ? "" : " AND");

			builder.append(" last_name LIKE '%");
			builder.append(lastname);
			builder.append("%'");
		}

		if (start != null && step != null) {
			builder.append(" LIMIT ");
			builder.append(start);
			builder.append(",");
			builder.append(step);
		}

		return builder.toString();
	}
}

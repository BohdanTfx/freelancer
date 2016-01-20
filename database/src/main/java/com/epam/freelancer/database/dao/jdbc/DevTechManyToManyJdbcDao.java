package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.DevTechManyToManyDao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.database.transformer.DataTransformer;

public class DevTechManyToManyJdbcDao extends
		GenericJdbcManyToManyDao<Developer, Technology, Worker, Integer>
		implements DevTechManyToManyDao
{

	public DevTechManyToManyJdbcDao() throws Exception {
		super("dev_tech", "developer", "technology", "dev_id", "tech_id",
				new DataTransformer<>(Developer.class), new DataTransformer<>(
						Technology.class), new DataTransformer<>(Worker.class));
	}

	@Override
	public List<Technology> getTechnologiesByDevId(Integer id) {
		List<Technology> entities = new ArrayList<>();
		String query = "SELECT " + secondTable + ".* FROM " + secondTable
				+ ", " + table + " WHERE " + secondTable + ".id = " + table
				+ "." + secondIdName + " AND " + table + "." + firstIdName;
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setObject(1, id);
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
}

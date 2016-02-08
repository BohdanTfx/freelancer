package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.DevTechManyToManyDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.database.transformer.DataTransformer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                + "." + secondIdName + " AND " + table + "." + firstIdName + "= ?";
        try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setObject(1, id);
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

	public void saveTechnologies(Integer firstId, Integer secondId){
		String query = "INSERT INTO " + table + " (" + firstIdName + ", "
				+ secondIdName + ") VALUES (?, ?)";
		try (Connection connection = dataSource.getConnection();
			 PreparedStatement statement = connection
					 .prepareStatement(query)) {
			statement.setObject(1, firstId);
			statement.setObject(2, secondId);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

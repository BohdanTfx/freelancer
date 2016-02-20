package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.ComplaintDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.Complaint;

/**
 * Created by spock on 04.02.16.
 */
public class ComplaintJdbcDao extends GenericJdbcDao<Complaint, Integer>
		implements ComplaintDao
{
	public ComplaintJdbcDao() throws Exception {
		super(Complaint.class);
	}

	@Override
	public List<Complaint> getByDevId(Integer devId) {
		List<Complaint> complaints = new ArrayList<>();
		String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND "
				+ GenericDao.NOT_DELETED;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, devId);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Complaint entity = new Complaint();
					entity = transformer.getObject(set);
					complaints.add(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return complaints;
	}

	@Override
	public Complaint isAlreadyExist(Integer devId, Integer orderId) {
		Complaint complaint = null;
		String query = "SELECT * FROM " + table
				+ " WHERE dev_id = ? AND order_id = ? AND "
				+ GenericDao.NOT_DELETED;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, devId);
			statement.setInt(2, orderId);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					complaint = transformer.getObject(set);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return complaint;
	}
}

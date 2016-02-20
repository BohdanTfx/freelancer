package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.WorkerDao;
import com.epam.freelancer.database.model.Worker;

/**
 * Created by ������ on 17.01.2016.
 */
public class WorkerJdbcDao extends GenericJdbcDao<Worker, Integer> implements
		WorkerDao
{
	public WorkerJdbcDao() throws Exception {
		super(Worker.class);
	}

	@Override
	public Worker getWorkerByDevIdAndOrderId(Integer idDev, Integer idOrder) {
		String query = "SELECT * FROM " + table
				+ " WHERE dev_id = ? AND order_id = ? AND "
				+ GenericDao.NOT_DELETED;
		if (getWorkerByQuery(query, idDev, idOrder).size() > 0)
			return getWorkerByQuery(query, idDev, idOrder).get(0);
		else
			return null;
	}

	@Override
	public List<Worker> getAllWorkersByOrderId(Integer idOrder) {
		String query = "SELECT * FROM " + table + " WHERE order_id = ? AND "
				+ GenericDao.NOT_DELETED;
		return getWorkerByQuery(query, idOrder, null);
	}

	@Override
	public List<Worker> getAllWorkersByDevId(Integer idDev) {
		String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND "
				+ GenericDao.NOT_DELETED;
		return getWorkerByQuery(query, idDev, null);
	}

	private List<Worker> getWorkerByQuery(String query, Integer firstId,
			Integer secondId)
	{
		List<Worker> list = new ArrayList<>();

		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			if (firstId != null && secondId != null) {
				statement.setInt(1, firstId);
				statement.setInt(2, secondId);
			}
			if (firstId != null && secondId == null || firstId == null
					&& secondId != null)
			{
				statement.setInt(1, firstId);
			}
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					Worker worker = transformer.getObject(set);
					list.add(worker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}

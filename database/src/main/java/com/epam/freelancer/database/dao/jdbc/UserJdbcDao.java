package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.UserDao;
import com.epam.freelancer.database.model.UserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class UserJdbcDao<T extends UserEntity, ID> extends
		GenericJdbcDao<T, Integer> implements UserDao<T>
{

	public UserJdbcDao(Class<T> class1) throws Exception {
		super(class1);
	}

	@Override
	public T getByEmail(String email) {
		String query = "SELECT * FROM " + table + " WHERE email LIKE ? AND "
				+ GenericDao.NOT_DELETED;
		T entity = null;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setString(1, email);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next()) {
					entity = transformer.getObject(set);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public boolean emailAvailable(String email) {
		String query = "SELECT count(email) FROM " + table
				+ " WHERE email LIKE ? AND " + GenericDao.NOT_DELETED;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setString(1, email);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next())
					return set.getInt(1) == 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean uuidAvailable(String uuid) {
		String query = "SELECT count(uuid) FROM " + table
				+ " WHERE uuid LIKE ? AND " + GenericDao.NOT_DELETED;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setString(1, uuid);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next())
					return set.getInt(1) == 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public T getByUUID(String uuid) {
		String query = "SELECT * FROM " + table + " WHERE UUID LIKE ? AND "
				+ GenericDao.NOT_DELETED;
		T entity = null;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setString(1, uuid);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next()) {
					entity = transformer.getObject(set);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
}

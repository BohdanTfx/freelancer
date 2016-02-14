package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.GenericManyToManyDao;
import com.epam.freelancer.database.model.BaseEntity;
import com.epam.freelancer.database.persistence.ConnectionPool;
import com.epam.freelancer.database.transformer.DataTransformer;

public abstract class GenericJdbcManyToManyDao<F extends BaseEntity<ID>, S extends BaseEntity<ID>, M extends BaseEntity<ID>, ID>
		implements GenericManyToManyDao<F, S, M, ID>
{
	protected ConnectionPool dataSource;
	protected String table;
	protected String firstTable;
	protected String secondTable;

	protected String firstIdName;
	protected String secondIdName;

	protected DataTransformer<F> firstTransformer;
	protected DataTransformer<S> secondTransformer;
	protected DataTransformer<M> middleTransformer;

	public GenericJdbcManyToManyDao(String table, String firstTable,
			String secondTable, String firstIdName, String secondIdName,
			DataTransformer<F> firstTransformer,
			DataTransformer<S> secondTransformer,
			DataTransformer<M> middleTransformer)
	{
		this.table = table;
		this.firstTable = firstTable;
		this.secondTable = secondTable;
		this.firstIdName = firstIdName;
		this.secondIdName = secondIdName;
		this.firstTransformer = firstTransformer;
		this.secondTransformer = secondTransformer;
		this.middleTransformer = middleTransformer;
	}

	public GenericJdbcManyToManyDao(String table, String firstTable,
			String secondTable, String firstIdName, String secondIdName,
			DataTransformer<F> firstTransformer,
			DataTransformer<S> secondTransformer)
	{
		this.table = table;
		this.firstTable = firstTable;
		this.secondTable = secondTable;
		this.firstIdName = firstIdName;
		this.secondIdName = secondIdName;
		this.firstTransformer = firstTransformer;
		this.secondTransformer = secondTransformer;
	}

	@Override
	public List<S> getBasedOnFirst(ID firstId) {
		List<S> entities = new ArrayList<>();
		String query = "SELECT " + secondTable + ".* FROM " + secondTable
				+ ", " + table + " WHERE " + secondTable + ".id = " + table
				+ "." + secondIdName + " AND " + table + "." + firstIdName
				+ " = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setObject(1, firstId);
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
	public List<F> getBasedOnSecond(ID secondId) {
		List<F> entities = new ArrayList<>();
		String query = "SELECT " + firstTable + ".* FROM " + firstTable + ", "
				+ table + " WHERE " + firstTable + ".id = " + table + "."
				+ firstIdName + " AND " + table + "." + secondIdName + " = ?";
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setObject(1, secondId);
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					entities.add(firstTransformer.getObject(set));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}

	@Override
	public void saveContact(ID firstId, ID secondId) {
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

	@Override
	public void removeContact(ID firstId, ID secondId) {
		String query = "DELETE FROM " + table + " WHERE " + firstIdName + " = ?" + " AND "
				+ secondIdName + " = ?";
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

	@Override
	public void setConnectionPool(ConnectionPool connectionPool) {
		this.dataSource = connectionPool;
	}

	// @Override
	// public List<ObjectHolder<M, S>> getFullEntityForFirst(ID firstId) {
	// List<ObjectHolder<M, S>> entities = new ArrayList<>();
	// String query = "SELECT * FROM " + secondTable + ", " + table
	// + " WHERE " + secondTable + ".id = " + table + "."
	// + secondIdName + " AND " + table + "." + firstIdName + " = ?";
	// try (Connection connection = dataSource.getConnection();
	// PreparedStatement statement = connection
	// .prepareStatement(query)) {
	// statement.setObject(1, firstId);
	// try (ResultSet set = statement.executeQuery()) {
	// while (set.next()) {
	// ObjectHolder<M, S> entity = new ObjectHolder<>();
	// entity.setFirst(middleTransformer.getObject(set));
	// entity.setSecond(secondTransformer.getObject(set));
	//
	// entities.add(entity);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return entities;
	// }
}

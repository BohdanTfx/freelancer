package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.epam.freelancer.database.dao.ContactDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.model.Contact;

/**
 * Created by ������ on 17.01.2016.
 */
public class ContactJdbcDao extends GenericJdbcDao<Contact, Integer> implements
		ContactDao
{
	public ContactJdbcDao() throws Exception {
		super(Contact.class);
	}

	@Override
	public Contact getContactByDevId(Integer id) {
		String query = "SELECT * FROM " + table + " WHERE dev_id = ? AND "
				+ GenericDao.NOT_DELETED;
		return getContactByQuery(query, id);
	}

	@Override
	public Contact getContactByCustId(Integer id) {
		String query = "SELECT * FROM " + table + " WHERE cust_id = ? AND "
				+ GenericDao.NOT_DELETED;
		return getContactByQuery(query, id);
	}

	private Contact getContactByQuery(String query, Integer id) {
		Contact contact = null;
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet set = statement.executeQuery()) {
				if (set.next()) {
					contact = transformer.getObject(set);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}
}

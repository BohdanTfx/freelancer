package com.epam.freelancer.business.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.ContactDao;
import com.epam.freelancer.database.dao.CustomerDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;

/**
 * Created by Максим on 18.01.2016.
 */
public class CustomerService extends UserService<Customer> {
	private GenericDao<Contact, Integer> contactDao;

	public CustomerService() {
		super(DAOManager.getInstance()
				.getDAO(CustomerDao.class.getSimpleName()));
		DAOManager daoManager = DAOManager.getInstance();
		genericDao.setConnectionPool(daoManager.getConnectionPool());
	}

	@Override
	public Customer create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");
		Customer entity = new Customer();
		String[] value = data.get("first_name");
		entity.setFname(value != null ? value[0] : null);
		value = data.get("last_name");
		entity.setLname(value != null ? value[0] : null);
		value = data.get("email");
		entity.setEmail(value != null ? value[0] : null);
		value = data.get("lang");
		entity.setLang(value != null ? value[0] : "en");
		value = data.get("zone");
		entity.setZone(value != null ? Integer.parseInt(value[0]) : null);
		entity.setRegUrl(UUID.randomUUID().toString());
		entity.setRegDate(new Timestamp(new java.util.Date().getTime()));
		value = data.get("password");
		entity.setPassword(value != null ? value[0] : null);

		encodePassword(entity);

		return genericDao.save(entity);
	}

	private Map<ValidationParametersBuilder.Parameters, String> prepareData(
			Map<String, String[]> data)
	{
		Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
		map.put(ValidationParametersBuilder.createParameters(false)
				.maxLength(50).minLength(1),
				data.get("first_name") == null ? null
						: data.get("first_name")[0]);
		map.put(ValidationParametersBuilder.createParameters(false)
				.maxLength(50).minLength(1),
				data.get("last_name") == null ? null : data.get("last_name")[0]);
		map.put(ValidationParametersBuilder.createParameters(false)
				.maxLength(140).minLength(8),
				data.get("password") == null ? null : data.get("password")[0]);
		map.put(ValidationParametersBuilder
				.createParameters(false)
				.maxLength(50)
				.pattern(
						"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]+)"),
				data.get("email") == null ? null : data.get("email")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(true).max(13.0).min(-12.0),
				data.get("zone") == null ? null : data.get("zone")[0]);

		return map;
	}

	public void setContactDao(GenericDao<Contact, Integer> contactDao) {
		this.contactDao = contactDao;
		this.contactDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public Contact getContactByCustomerId(Integer id) {
		return ((ContactDao) contactDao).getContactByCustId(id);
	}

	public Contact updateContact(Contact contact) {
		return contactDao.update(contact);
	}

	public void deleteContact(Contact contact) {
		contactDao.delete(contact);
	}
}

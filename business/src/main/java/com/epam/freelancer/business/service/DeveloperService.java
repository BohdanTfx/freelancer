package com.epam.freelancer.business.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.ContactDao;
import com.epam.freelancer.database.dao.DevTechManyToManyDao;
import com.epam.freelancer.database.dao.DeveloperDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.GenericManyToManyDao;
import com.epam.freelancer.database.dao.WorkerManyToManyDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Worker;

/**
 * Created by Максим on 18.01.2016.
 */
public class DeveloperService extends UserService<Developer> {
	private GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao;
	private GenericManyToManyDao<Developer, Technology, Worker, Integer> devMTMtechDao;
	private GenericDao<Worker, Integer> workerDao;
	private GenericDao<Contact, Integer> contactDao;

	public DeveloperService() {
		super(DAOManager.getInstance().getDAO(
				DeveloperDao.class.getSimpleName()));
		DAOManager daoManager = DAOManager.getInstance();
		genericDao.setConnectionPool(daoManager.getConnectionPool());
	}

	@Override
	public Developer create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");

		Developer entity = new Developer();
		String[] value = data.get("first_name");
		entity.setFname(value != null ? value[0] : null);
		value = data.get("last_name");
		entity.setLname(value != null ? value[0] : null);
		value = data.get("email");
		entity.setEmail(value != null ? value[0] : null);
		value = data.get("img_url");
		entity.setImgUrl(value != null ? value[0] : null);
		value = data.get("lang");
		entity.setLang(value != null ? value[0] : "en");
		value = data.get("zone");
		entity.setZone(value != null ? Integer.parseInt(value[0]) : null);
		entity.setRegUrl(UUID.randomUUID().toString());
		entity.setRegDate(new Timestamp(new Date().getTime()));
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
				.notEmptyString(false).maxLength(255),
				data.get("img_url") == null ? null : data.get("img_url")[0]);
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

	public List<Ordering> getDeveloperPortfolio(Integer id) {
		return ((WorkerManyToManyDao) workerMTMDao).getPortfolio(id);
	}

	public void setWorkerMTMDao(
			GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao)
	{
		this.workerMTMDao = workerMTMDao;
		this.workerMTMDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public void setWorkerDao(GenericDao<Worker, Integer> workerDao) {
		this.workerDao = workerDao;
		this.workerDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public void setContactDao(GenericDao<Contact, Integer> contactDao) {
		this.contactDao = contactDao;
		this.contactDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public void setDevMTMtechDao(
			GenericManyToManyDao<Developer, Technology, Worker, Integer> devMTMtechDao)
	{
		this.devMTMtechDao = devMTMtechDao;
		this.devMTMtechDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public Worker createWorker(Worker worker) {
		return workerDao.save(worker);
	}

	public void deleteWorker(Worker worker) {
		workerDao.delete(worker);
	}

	public Worker updateWorker(Worker worker) {
		return workerDao.update(worker);
	}

	public Worker getWorkerById(Integer id) {
		return workerDao.getById(id);
	}

	public List<Worker> getAllWorkers() {
		return workerDao.getAll();
	}

	public Contact getContactByDevId(Integer id) {
		return ((ContactDao) contactDao).getContactByDevId(id);
	}

	public Contact updateContact(Contact contact) {
		return contactDao.update(contact);
	}

	public void deleteContact(Contact contact) {
		contactDao.delete(contact);
	}

	public List<Technology> getTechnologiesByDevId(Integer id) {
		return ((DevTechManyToManyDao) devMTMtechDao)
				.getTechnologiesByDevId(id);
	}

}

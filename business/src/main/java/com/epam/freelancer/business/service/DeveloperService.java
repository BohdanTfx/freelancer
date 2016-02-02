package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.dao.jdbc.FollowerManyToManyJdbcDao;
import com.epam.freelancer.database.model.*;

import java.sql.Timestamp;
import java.util.*;

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
	private	GenericManyToManyDao<Developer, Ordering, Follower, Integer> followerMTMDevDao;
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
        value = data.get("lang");
        entity.setLang(value != null ? value[0] : "en");
        value = data.get("uuid");
        entity.setUuid(value != null ? value[0] : null);
        value = data.get("reg_url");
        entity.setRegUrl(value != null ? value[0] : null);
        entity.setRegDate(new Timestamp(new Date().getTime()));
        value = data.get("password");
        entity.setPassword(value != null ? value[0] : null);

        encodePassword(entity);

        return genericDao.save(entity);
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(
            Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(50).minLength(1),
                data.get("first_name") == null ? null
                        : data.get("first_name")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(50).minLength(1),
                data.get("last_name") == null ? null : data.get("last_name")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                .maxLength(50).minLength(1), data.get("uuid") == null ? null
                : data.get("uuid")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(140).minLength(1),
                data.get("password") == null ? null : data.get("password")[0]);
        map.put(ValidationParametersBuilder
                        .createParameters(false)
                        .maxLength(50)
                        .pattern(
                                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]+)"),
                data.get("email") == null ? null : data.get("email")[0]);

        return map;
    }

	public List<Ordering> getDeveloperPortfolio(Integer id) {
		return ((WorkerManyToManyDao) workerMTMDao).getPortfolio(id);
	}

	public List<Ordering> getDeveloperSubscribedProjects(Integer id) {
        return ((FollowerManyToManyDao) followerMTMDevDao).getDevSubscribedProjects(id);

    }

    public List<Developer> getDevelopersByIdOrder(Integer id){
        return workerMTMDao.getBasedOnSecond(id);
    }

//    public List<Worker> getWorkersByIdOrder(Integer id){
//        return workerMTMDao.;
//    }

    public void setFollowerMTMDevDao(GenericManyToManyDao<Developer, Ordering, Follower, Integer> followerMTMDevDao) {
        this.followerMTMDevDao = followerMTMDevDao;
        this.followerMTMDevDao.setConnectionPool(DAOManager.getInstance()
                .getConnectionPool());

    }


    public void setWorkerMTMDao(
            GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao) {
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

    public Worker getWorkerByDevIdAndOrderId(Integer idDev,Integer idOrder) {
        return ((WorkerDao) workerDao).getWorkerByDevIdAndOrderId(idDev,idOrder);
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

package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 18.01.2016.
 */
public class DeveloperService extends UserService<Developer> {
    private GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao;
    private GenericManyToManyDao<Developer, Technology, BaseEntity<Integer>, Integer> devMTMtechDao;
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
        entity.setRegDate(new Timestamp(new java.util.Date().getTime()));
        value = data.get("password");
        entity.setPassword(value != null ? value[0] : null);

        encodePassword(entity);

        return genericDao.save(entity);
    }

    public void deleteDeveloper(Developer Developer) {
        genericDao.delete(Developer);
    }

    public Developer updateDeveloper(Developer Developer) {
        return genericDao.update(Developer);
    }

    public Developer getDeveloper(Integer id) {
        return genericDao.getById(id);
    }

    public List<Developer> getAllDevelopers() {
        return genericDao.getAll();
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
                .maxLength(50).minLength(1), data.get("lang") == null ? null
                : data.get("lang")[0]);
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

    public void setWorkerMTMDao(
            GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao) {
        this.workerMTMDao = workerMTMDao;
    }

    public GenericDao<Worker, Integer> getWorkerDao() {
        return workerDao;
    }

    public void setWorkerDao(GenericDao<Worker, Integer> workerDao) {
        this.workerDao = workerDao;
    }

    public GenericDao<Contact, Integer> getContactDao() {
        return contactDao;
    }

    public void setContactDao(GenericDao<Contact, Integer> contactDao) {
        this.contactDao = contactDao;
    }

    public GenericManyToManyDao<Developer, Technology, BaseEntity<Integer>, Integer> getDevMTMtechDao() {
        return devMTMtechDao;
    }

    public void setDevMTMtechDao(
            GenericManyToManyDao<Developer, Technology, BaseEntity<Integer>, Integer> devMTMtechDao) {
        this.devMTMtechDao = devMTMtechDao;
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

/*    public List<Technology> getTechnologiesByDevId(Integer id) {
        return ((DevMTMTechDao) devMTMtechDao).getTechnologiesByDevId(id);
    }*/
}

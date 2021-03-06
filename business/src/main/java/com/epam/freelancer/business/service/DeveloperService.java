package com.epam.freelancer.business.service;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.business.util.ValidationParametersBuilder.Parameters;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.*;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Максим on 18.01.2016.
 */
public class DeveloperService extends UserService<Developer> {
    private GenericManyToManyDao<Developer, Ordering, Worker, Integer> workerMTMDao;
    private GenericManyToManyDao<Developer, Technology, Worker, Integer> devMTMtechDao;
    private GenericManyToManyDao<Developer, Ordering, Follower, Integer> followerMTMDevDao;
    private GenericDao<Worker, Integer> workerDao;
    private GenericDao<Contact, Integer> contactDao;
    private GenericDao<Follower, Integer> followerDao;
    private GenericDao<Technology, Integer> technologyDao;

    public DeveloperService() {
        super(DAOManager.getInstance().getDAO(
                DeveloperDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    	@Override
	public Developer create(Map<String, String[]> data) {
//		if (!isDataValid(prepareData(data)))
//			throw new RuntimeException("Validation exception");

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
        entity.setUuid(UUID.randomUUID().toString());

        encodePassword(entity);

        return genericDao.save(entity);
    }

    public Developer updateDeveloper(Developer developer) {
        return genericDao.update(developer);
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(
            Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(false)

                .maxLength(50).minLength(1), data.get("first_name") == null ? null
                : data.get("first_name")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(
                255), data.get("img_url") == null ? null
                : data.get("img_url")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(50).minLength(1),
                data.get("last_name") == null ? null
                        : data.get("last_name")[0]);
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

    public List<Ordering> getDeveloperSubscribedProjects(Integer id) {
        return ((FollowerManyToManyDao) followerMTMDevDao)
                .getDevSubscribedProjects(id);

    }

    public List<Developer> getDevelopersByIdOrder(Integer id) {
        return workerMTMDao.getBasedOnSecond(id);
    }

    public void setFollowerMTMDevDao(
            GenericManyToManyDao<Developer, Ordering, Follower, Integer> followerMTMDevDao) {
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
            GenericManyToManyDao<Developer, Technology, Worker, Integer> devMTMtechDao) {
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

    public void deleteFollower(Follower follower) {
        followerDao.delete(follower);
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

    public Worker getWorkerByDevIdAndOrderId(Integer idDev, Integer idOrder) {
        return ((WorkerDao) workerDao).getWorkerByDevIdAndOrderId(idDev,
                idOrder);
    }

    public List<Worker> getAllWorkersByOrderId(Integer idOrder) {
        return ((WorkerDao) workerDao).getAllWorkersByOrderId(idOrder);
    }
    public List<Worker> getAllWorkersByDevId(Integer idDev) {
        return ((WorkerDao) workerDao).getAllWorkersByDevId(idDev);
    }

    public Contact createContact(Contact contact) {
        return contactDao.save(contact);
    }

    public Contact updateContact(Contact contact) {
        return contactDao.update(contact);
    }

    public void deleteContact(Contact contact) {
        contactDao.delete(contact);
    }

    public void addTechnologyForDev(Integer devId, Integer technologyId) {
        devMTMtechDao.saveContact(devId, technologyId);
    }

    public void deleteTechnologyInDev(Integer devId, Integer technologyId){
        devMTMtechDao.removeContact(devId, technologyId);
    }

    public void addTechnologiesForDev(Integer devId, List<Integer> technologiesIds) {
        for (Integer techId : technologiesIds) {
            devMTMtechDao.saveContact(devId, techId);
        }
    }

    public List<Technology> getTechnologiesByDevId(Integer id) {
        return ((DevTechManyToManyDao) devMTMtechDao)
                .getTechnologiesByDevId(id);
    }

    public Technology updateTechnology(Technology technology) {
        return technologyDao.update(technology);
    }

    public void setFollowerDao(GenericDao<Follower, Integer> followerDao) {
        this.followerDao = followerDao;
        this.followerDao.setConnectionPool(DAOManager.getInstance()
                .getConnectionPool());
    }

    public List<Follower> findDeveloperFollowings(Integer developerId) {
        return ((FollowerDao) followerDao).getDeveloperFollowings(developerId);
    }

    public Follower createFollowing(Map<String, String[]> data) {
        /*
		 * if (!isDataValid(prepareFollowerData(data))) throw new
		 * RuntimeException("Validation exception in follower");
		 */

        Follower follower = new Follower();
        String[] value = data.get("dev_id");
        follower.setDevId(value != null ? Integer.parseInt(value[0]) : null);
        value = data.get("cust_id");
        follower.setCustId(value != null ? Integer.parseInt(value[0]) : null);
        value = data.get("order_id");
        follower.setOrderId(value != null ? Integer.parseInt(value[0]) : null);
        value = data.get("author");
        follower.setAuthor(value != null ? value[0] : null);
        value = data.get("message");
        follower.setMessage(value != null ? value[0] : null);

        return followerDao.save(follower);
    }

    private Map<Parameters, String> prepareFollowerData(
            Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(
                true), data.get("dev_id") == null ? null
                : data.get("dev_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(
                true), data.get("cust_id") == null ? null
                : data.get("cust_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(
                true),
                data.get("order_id") == null ? null : data.get("order_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(6),
                data.get("message") == null ? null : data.get("message")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                        .notEmptyString(true).pattern("(dev)"),
                data.get("author") == null ? null : data.get("author")[0]);

        return map;
    }

    public Follower subscribeOnProject(Integer devId, Integer orderId,
                                       String message) {
        Follower follower = new Follower();
        follower.setDevId(devId);
        follower.setOrderId(orderId);
        follower.setAuthor("dev");
        follower.setIsDeleted(false);
        if (!message.isEmpty())
            follower.setMessage(message);
        return followerDao.save(follower);
    }

    public void unsubscribeFromProject(Integer followerId) {
        Follower follower = new Follower();
        follower.setId(followerId);
        followerDao.delete(follower);
    }

    public Integer getFilteredObjectNumber(Map<String, Object> parameters) {
        return ((DeveloperDao) genericDao).getFilteredObjectNumber(parameters);
    }

    public Double findPaymentLimit(String limitType) {
        return ((DeveloperDao) genericDao).getPaymentLimit(limitType);
    }
}

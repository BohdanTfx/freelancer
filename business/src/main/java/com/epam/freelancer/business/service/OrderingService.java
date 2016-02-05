package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.FollowerDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.GenericManyToManyDao;
import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 19.01.2016.
 */
public class OrderingService extends GenericService<Ordering, Integer> {
	private GenericManyToManyDao<Ordering, Technology, Worker, Integer> orderingTechnoloyManyToManyDao;
	private GenericDao<Follower, Integer> followerDao;

	public OrderingService() {
		super(DAOManager.getInstance()
				.getDAO(OrderingDao.class.getSimpleName()));
		DAOManager daoManager = DAOManager.getInstance();
		genericDao.setConnectionPool(daoManager.getConnectionPool());
	}

	@Override
	public Ordering create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");

		Ordering order = new Ordering();
		order.setTitle(data.get("title")[0]);
		order.setPayType(data.get("pay_type")[0]);
		order.setDescr(data.get("descr")[0]);
		order.setCustomerId(Integer.parseInt(data.get("customer_id")[0]));
		order.setZone(Integer.parseInt(data.get("zone")[0]));
		order.setDate(new Timestamp(new java.util.Date().getTime()));
		order.setPayment(Double.parseDouble(data.get("payment")[0]));
		order.setStarted(false);
		order.setEnded(false);
		order.setPrivate(Boolean.parseBoolean(data.get("private")[0]));

		order = genericDao.save(order);
		
		String[] technologies = data.get("technologies")[0].split(",");
		List<Integer> techIds = new ArrayList<>();
		for (String string : technologies)
			techIds.add(Integer.parseInt(string));
		
		for (Integer id : techIds) 
			orderingTechnoloyManyToManyDao.saveContact(order.getId(), id);

		return order;
	}

	private Map<ValidationParametersBuilder.Parameters, String> prepareData(
			Map<String, String[]> data)
	{
		Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
		map.put(ValidationParametersBuilder.createParameters(false)
				.minLength(10).maxLength(120), data.get("title") == null ? null
				: data.get("title")[0]);
		map.put(ValidationParametersBuilder.createParameters(false).pattern(
				"(hourly)|(fixed)"),
				data.get("pay_type") == null ? null : data.get("pay_type")[0]);
		map.put(ValidationParametersBuilder.createParameters(false).pattern(
				"(true)|(false)"),
				data.get("private") == null ? null : data.get("private")[0]);
		map.put(ValidationParametersBuilder.createParameters(false)
				.minLength(50).maxLength(3000),
				data.get("descr") == null ? null : data.get("descr")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(true).min(1.00),
				data.get("customer_id") == null ? null : data
						.get("customer_id")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(true).min(0.00), data.get("payment") == null ? null
				: data.get("payment")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(true).min(-12.0).max(13.0),
				data.get("zone") == null ? null : data.get("zone")[0]);
		map.put(ValidationParametersBuilder.createParameters(false).pattern(
				"(^([0-9])[,0-9]*[0-9]$)|^[0-9]$"),
				data.get("technologies") == null ? null : data
						.get("technologies")[0]);

		return map;
	}

	public Map<String, ObjectHolder<Double, Double>> findPaymentLimits() {
		Map<String, ObjectHolder<Double, Double>> map = new HashMap<>();
		OrderingDao orderingDao = (OrderingDao) genericDao;

		ObjectHolder<Double, Double> fixed = new ObjectHolder<Double, Double>(
				orderingDao.getPayment("fixed", "min"), orderingDao.getPayment(
						"fixed", "max"));
		ObjectHolder<Double, Double> hourly = new ObjectHolder<Double, Double>(
				orderingDao.getPayment("hourly", "min"),
				orderingDao.getPayment("hourly", "max"));

		map.put("hourly", hourly);
		map.put("fixed", fixed);
		return map;
	}

	public Integer getFilteredObjectNumber(Map<String, Object> parameters) {
		return ((OrderingDao) genericDao).getFilteredObjectNumber(parameters);
	}

	public void setOrderingTechnoloyManyToManyDao(
			GenericManyToManyDao<Ordering, Technology, Worker, Integer> orderingTechnoloyManyToManyDao)
	{
		this.orderingTechnoloyManyToManyDao = orderingTechnoloyManyToManyDao;
		this.orderingTechnoloyManyToManyDao.setConnectionPool(DAOManager
				.getInstance().getConnectionPool());
	}

	public List<Technology> findOrderingTechnologies(Integer orderId) {
		return orderingTechnoloyManyToManyDao.getBasedOnFirst(orderId);
	}

	public void setFollowerDao(GenericDao<Follower, Integer> followerDao) {
		this.followerDao = followerDao;
		followerDao.setConnectionPool(DAOManager.getInstance()
				.getConnectionPool());
	}

	public List<Follower> findOrderFollowers(Integer orderId) {
		return ((FollowerDao) followerDao).getProjectFollowers(orderId);
	}
}

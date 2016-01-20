package com.epam.freelancer.business.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Ordering;

/**
 * Created by Максим on 19.01.2016.
 */
public class OrderingService extends GenericService<Ordering, Integer> {

	public OrderingService() {
		super(DAOManager.getInstance()
				.getDAO(OrderingDao.class.getSimpleName()));
	}

	@Override
	public Ordering create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");

		Ordering order = new Ordering();
		String[] value = data.get("title");
		order.setTitle(value != null ? value[0] : null);
		value = data.get("pay_type");
		order.setPayType(value != null ? value[0] : null);
		value = data.get("descr");
		order.setDescr(value != null ? value[0] : null);
		value = data.get("customer_id");
		Integer integer = value != null ? Integer.parseInt(value[0]) : null;
		if (integer == null)
			throw new RuntimeException("Validation exception");
		order.setCustomerId(integer);
		order.setDate(new Date(new java.util.Date().getTime()));
		value = data.get("payment");
		Double doub = value != null ? Double.parseDouble(value[0]) : null;
		if (doub == null)
			throw new RuntimeException("Validation exception");
		order.setStarted(false);
		order.setEnded(false);
		value = data.get("private");
		Boolean bool = value != null ? Boolean.parseBoolean(value[0]) : null;
		if (bool == null)
			throw new RuntimeException("Validation exception");
		order.setPriv(bool);
		order = genericDao.save(order);
		return order;
	}

	private Map<ValidationParametersBuilder.Parameters, String> prepareData(
			Map<String, String[]> data)
	{
		Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
		map.put(ValidationParametersBuilder.createParameters(false)
				.minLength(5).maxLength(120), data.get("title") == null ? null
				: data.get("title")[0]);
		map.put(ValidationParametersBuilder.createParameters(false).pattern(
				"(hourly)|(fixed)"),
				data.get("pay_type") == null ? null : data.get("pay_type")[0]);
		map.put(ValidationParametersBuilder.createParameters(false)
				.minLength(5).maxLength(3000), data.get("descr") == null ? null
				: data.get("descr")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(true).min(1.00),
				data.get("customer_id") == null ? null : data
						.get("customer_id")[0]);
		map.put(ValidationParametersBuilder.createParameters(true)
				.isInteger(false).min(0.00), data.get("payment") == null ? null
				: data.get("payment")[0]);

		return map;
	}

}

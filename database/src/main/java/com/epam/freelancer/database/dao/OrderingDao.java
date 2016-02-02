package com.epam.freelancer.database.dao;

import java.util.List;
import java.util.Map;

import com.epam.freelancer.database.model.Ordering;

/**
 * Created by ������ on 17.01.2016.
 */
public interface OrderingDao extends GenericDao<Ordering, Integer> {

	public Double getPayment(String paymentType, String limit);

	Integer getFilteredObjectNumber(Map<String, Object> parameters);

	public List<Ordering> getCustomerPublicHistory(Integer custId);
}

package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Ordering;

import java.util.List;
import java.util.Map;

/**
 * Created by ������ on 17.01.2016.
 */
public interface OrderingDao extends GenericDao<Ordering, Integer> {

    Double getPayment(String paymentType, String limit);

	Integer getFilteredObjectNumber(Map<String, Object> parameters);

    List<Ordering> getCustomerPublicHistory(Integer custId);

    List<Ordering> getAvailableCustOrders(Integer custId);
}

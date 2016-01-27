package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Ordering;

/**
 * Created by ������ on 17.01.2016.
 */
public interface OrderingDao extends GenericDao<Ordering, Integer> {

	public Double getPayment(String paymentType, String limit);
}

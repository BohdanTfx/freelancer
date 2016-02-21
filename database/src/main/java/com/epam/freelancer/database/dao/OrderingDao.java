package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Ordering;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface OrderingDao extends GenericDao<Ordering, Integer> {

    Double getPayment(String paymentType, String limit);

    List<Ordering> getCustomerPublicHistory(Integer custId);

    List<Ordering> getAvailableCustOrders(Integer custId);

    List<Ordering> getAllCustOrders(Integer custId);

    int getAllAcceptedOrderByDevIdAndCustId(Integer custId, Integer devId);
}

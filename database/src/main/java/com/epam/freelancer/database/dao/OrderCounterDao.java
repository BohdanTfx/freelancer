package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.OrderCounter;

import java.util.List;

/**
* Created by Rynik on 07.02.2016.
*/
public interface OrderCounterDao extends GenericDao<OrderCounter, Integer>  {

    OrderCounter getOrderCounterByDate(java.sql.Date date);
    Integer incrementCounter(Integer id);
    public List<OrderCounter> getAllForLast30Days();
}

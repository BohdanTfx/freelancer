package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.OrderCounterDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.dao.jdbc.OrderCounterJdbcDao;
import com.epam.freelancer.database.model.OrderCounter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rynik on 07.02.2016.
 */
public class OrderCounterService extends GenericService<OrderCounter, Integer> {

    public OrderCounterService() {
        super(DAOManager.getInstance().getDAO(OrderCounterDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public OrderCounter create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        OrderCounter entity = new OrderCounter();
        String[] value  = data.get("count");
        entity.setCount(value != null ? Integer.parseInt(value[0]) : null);
        entity.setDate(new java.sql.Date(new java.util.Date().getTime()));


        return genericDao.save(entity);
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(
            Map<String, String[]> data)
    {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();


        map.put(ValidationParametersBuilder.createParameters(true)
                        .isInteger(true).min(1.00),
                data.get("count") == null ? null : data
                        .get("count")[0]);
       return map;
    }

    public OrderCounter getOrderCounterByDate(java.sql.Date date){
        return ((OrderCounterDao) genericDao).getOrderCounterByDate(date);
    }

    public Integer incrementCounter(Integer id){
        return ((OrderCounterDao) genericDao).incrementCounter(id);
    }

    public List<OrderCounter> getAllForLast30Days(){
        return  ((OrderCounterDao) genericDao).getAllForLast30Days();
    }

}

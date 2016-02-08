package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.OrderCounterDao;
import com.epam.freelancer.database.model.AdminCandidate;
import com.epam.freelancer.database.model.OrderCounter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rynik on 07.02.2016.
 */


public class OrderCounterJdbcDao extends GenericJdbcDao<OrderCounter, Integer> implements OrderCounterDao {

    public OrderCounterJdbcDao() throws Exception {
        super(OrderCounter.class);
    }


    @Override
    public OrderCounter getOrderCounterByDate(java.sql.Date date) {
        OrderCounter orderCounter = null;
        String query = "SELECT * FROM " + table + " WHERE  "+table+".date = ?";
        System.out.println("Query: "+query+"  date: "+date);
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setDate(1, date);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    orderCounter = transformer.getObject(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderCounter;
    }

    @Override
    public Integer incrementCounter(Integer id) {
        Integer result = null;
        String query = "UPDATE " + table + " SET count = count+1 where id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setInt(1, id);
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<OrderCounter> getAllForLast30Days() {
        List<OrderCounter> list = new ArrayList<>();
        OrderCounter orderCounter = null;
        String query = "select * from order_count where order_count.date > DATE_SUB(CURDATE(), INTERVAL  30 DAY)" +
                " AND order_count.date < CURDATE()+1;";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    orderCounter = transformer.getObject(set);
                    list.add(orderCounter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }





}

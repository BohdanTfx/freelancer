package com.epam.freelancer.business.service;

import com.epam.freelancer.database.dao.ComplaintDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Complaint;
import com.epam.freelancer.database.model.Ordering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by spock on 04.02.16.
 */
public class ComplaintService extends GenericService<Complaint, Integer> {
    private GenericDao<Ordering, Integer> orderingDao;

    public ComplaintService() {
        super(DAOManager.getInstance().getDAO(ComplaintDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public Complaint create(Map<String, String[]> data) {
        Complaint complaint = new Complaint();

        String value[] = data.get("order_id");
        Integer order_id = value != null ? Integer.parseInt(value[0]) : null;
        if (order_id == null)
            throw new RuntimeException("Order_id can't be null");
        complaint.setOrderId(order_id);
        value = data.get("dev_id");
        Integer dev_id = value != null ? Integer.parseInt(value[0]) : null;
        if (dev_id == null)
            throw new RuntimeException("Dev_id can't be null");
        complaint.setDevId(dev_id);

        return complaint;
    }

    public Complaint isAlreadyExist(Integer devId, Integer orderId) {
        return ((ComplaintDao) genericDao).isAlreadyExist(devId, orderId);
    }

    public List<Complaint> getByDevId(Integer devId) {
        return ((ComplaintDao) genericDao).getByDevId(devId);
    }

    public Complaint save(Complaint complaint) {
        return genericDao.save(complaint);
    }

    public void setOrderingDao(GenericDao<Ordering, Integer> orderingDao) {
        this.orderingDao = orderingDao;
    }
}

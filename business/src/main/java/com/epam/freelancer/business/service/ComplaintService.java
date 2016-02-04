package com.epam.freelancer.business.service;

import com.epam.freelancer.database.dao.ComplaintDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Complaint;

import java.util.Map;

/**
 * Created by spock on 04.02.16.
 */
public class ComplaintService extends GenericService<Complaint, Integer> {


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
        complaint.setOrder_id(order_id);
        value = data.get("dev_id");
        Integer dev_id = value != null ? Integer.parseInt(value[0]) : null;
        if (dev_id == null)
            throw new RuntimeException("Dev_id can't be null");
        complaint.setDev_id(dev_id);

        return complaint;
    }
}

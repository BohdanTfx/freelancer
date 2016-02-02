package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Worker;

/**
 * Created by ������ on 17.01.2016.
 */
public interface WorkerDao extends GenericDao<Worker, Integer> {

    public Worker getWorkerByDevIdAndOrderId (Integer idDev, Integer idOrder);

}

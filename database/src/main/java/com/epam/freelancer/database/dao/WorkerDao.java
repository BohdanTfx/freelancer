package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Worker;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface WorkerDao extends GenericDao<Worker, Integer> {

    public Worker getWorkerByDevIdAndOrderId (Integer idDev, Integer idOrder);

    public List<Worker> getAllWorkersByOrderId(Integer idOrder);

    public List<Worker> getAllWorkersByDevId(Integer idDev);

}

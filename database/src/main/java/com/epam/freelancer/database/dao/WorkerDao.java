package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Worker;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface WorkerDao extends GenericDao<Worker, Integer> {

    Worker getWorkerByDevIdAndOrderId(Integer idDev, Integer idOrder);

    List<Worker> getAllWorkersByOrderId(Integer idOrder);

    List<Worker> getAllWorkersByDevId(Integer idDev);

}

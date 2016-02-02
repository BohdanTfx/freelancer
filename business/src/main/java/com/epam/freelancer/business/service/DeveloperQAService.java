package com.epam.freelancer.business.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.DeveloperQADao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.DeveloperQA;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Test;

/**
 * Created by Максим on 18.01.2016.
 */
public class DeveloperQAService extends GenericService<DeveloperQA, Integer> {

    private GenericDao<Test, Integer> testDao;
    private GenericDao<Technology, Integer> technologyDao;

    public DeveloperQAService() {
        super(DAOManager.getInstance().getDAO(
                DeveloperQADao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public DeveloperQA create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        DeveloperQA developerQA = new DeveloperQA();
        String[] value = data.get("dev_id");
        Integer integer = value != null ? Integer.parseInt(value[0]) : null;
        developerQA.setDevId(integer);
        value = data.get("test_id");
        integer = value != null ? Integer.parseInt(value[0]) : null;
        developerQA.setTestId(integer);
        value = data.get("rate");
        Double rate = value != null ? Double.parseDouble(value[0]) : null;
        developerQA.setRate(rate);
        value = data.get("expireDate");
        if (value != null) {
            developerQA.setExpire(new Date(Long.parseLong(value[0])));
        }
        developerQA.setIsExpire(false);


        DeveloperQA dQA =((DeveloperQADao) genericDao).getByDevIdAndTestId(developerQA.getDevId(), developerQA.getTestId());
        if(dQA == null ){
            return genericDao.save(developerQA);
        }else{
            developerQA.setId(dQA.getId());
            return genericDao.update(developerQA);
        }
    }

    public List<DeveloperQA> findAllByDevId(Integer id) {
        List<DeveloperQA> developerQAs = ((DeveloperQADao) genericDao).getDevQAByDevId(id);
        List<Test> tests = testDao.getAll();
        List<Technology> techs = technologyDao.getAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(), technology));
        Map<Integer, Test> testMap = new HashMap<>();
        tests.forEach(test -> {
            test.setTechnology(technologyMap.get(test.getTechId()));
            testMap.put(test.getId(), test);
        });
        for (DeveloperQA developerQA : developerQAs) {
            developerQA.setTest(testMap.get(developerQA.getTestId()));
        }
        return developerQAs;
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(
            Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(true)
                .isInteger(true).min(1.00), data.get("dev_id") == null ? null
                : data.get("dev_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(true)
                .isInteger(true).min(1.00), data.get("test_id") == null ? null
                : data.get("test_id")[0]);
        return map;
    }

    public GenericDao<Test, Integer> getTestDao() {
        return testDao;
    }

    public void setTestDao(GenericDao<Test, Integer> testDao) {
        this.testDao = testDao;
    }

    public GenericDao<Technology, Integer> getTechnologyDao() {
        return technologyDao;
    }

    public void setTechnologyDao(GenericDao<Technology, Integer> technologyDao) {
        this.technologyDao = technologyDao;
        this.technologyDao.setConnectionPool(DAOManager.getInstance().getConnectionPool());
    }
}

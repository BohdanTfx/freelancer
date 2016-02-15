package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.FeedbackDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Feedback;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 18.01.2016.
 */
public class FeedbackService extends GenericService<Feedback, Integer> {

    public FeedbackService() {
        super(DAOManager.getInstance().getDAO(FeedbackDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public Feedback create(Map<String, String[]> data) {
        /*if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");*/

        Feedback feedback = new Feedback();
        String[] value = data.get("dev_id");
        Integer integer = value != null ? Integer.parseInt(value[0]) : null;
        feedback.setDevId(integer);
        value = data.get("cust_id");
        integer = value != null ? Integer.parseInt(value[0]) : null;
        feedback.setCustomerId(integer);
        value = data.get("comment");
        feedback.setComment(value != null ? value[0] : null);
        value = data.get("rate");
        integer = value != null ? Integer.parseInt(value[0]) : null;
        feedback.setRate(integer);
        value = data.get("author");
        feedback.setAuthor(value != null ? value[0] : null);
        feedback.setDate(new Timestamp(new Date().getTime()));

        feedback = genericDao.save(feedback);
        
        return feedback;
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(true).minLength(5)
                        .maxLength(50),
                data.get("dev_id") == null ? null : data.get("dev_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(true).min(1.00),
                data.get("cust_id") == null ? null : data.get("cust_id")[0]);
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(true).min(1.00),
                data.get("comment") == null ? null : data.get("comment")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).minLength(5)
                        .maxLength(1000),
                data.get("rate") == null ? null : data.get("name")[0]);
        map.put(ValidationParametersBuilder.createParameters(true).isInteger(true).min(0.00).max(5.00),
                data.get("name") == null ? null : data.get("name")[0]);
        return map;
    }

    public List<Feedback> findFeedbacksByCustId(Integer id) {
        return ((FeedbackDao) genericDao).getFeedbacksByCustId(id);
    }

    public List<Feedback> findFeedbacksByDevId(Integer id) {
        return ((FeedbackDao) genericDao).getFeedbacksByDevId(id);
    }

    public List<Feedback> findFeedbacksByDevIdForHim(Integer id) {
        return ((FeedbackDao) genericDao).getFeedbacksByDevIdForHim(id);
    }

    public List<Feedback> findFeedbacksByCustIdForHim(Integer id) {
        return ((FeedbackDao) genericDao).getFeedbacksByCustIdForHim(id);
    }

    public Integer getAvgRate(Integer dev_id) {
        return ((FeedbackDao) genericDao).getAvgRate(dev_id);
    }

    public int deleteDevFeed(Integer devId, Integer feedId) {
        return ((FeedbackDao) genericDao).deleteDevFeed(devId, feedId);
    }

    public int deleteCustFeed(Integer custId, Integer feedId) {
        return ((FeedbackDao) genericDao).deleteCustFeed(custId, feedId);
    }
}
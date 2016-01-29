package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Feedback;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface FeedbackDao extends GenericDao<Feedback, Integer> {

    List<Feedback> getFeedbacksByDevId(Integer id);

    List<Feedback> getFeedbacksByCustId(Integer id);

    Integer getAvgRate(Integer dev_id);

}

package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Answer;

import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public interface AnswerDao extends GenericDao<Answer, Integer> {
	List<Answer> getAnswersByQuestionId(Integer id);
}

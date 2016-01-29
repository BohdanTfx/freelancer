package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Question;
import com.epam.freelancer.database.model.Test;
import com.epam.freelancer.database.model.Worker;

import java.util.List;

/**
 * Created by Максим on 28.01.2016.
 */
public interface TestQuestionManyToManyDao extends
        GenericManyToManyDao<Test, Question, Worker, Integer>
{
    public List<Question> getQuestionsByTestId(Integer id);
}

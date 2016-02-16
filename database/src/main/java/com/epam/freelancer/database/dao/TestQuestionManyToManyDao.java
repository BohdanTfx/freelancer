package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Question;
import com.epam.freelancer.database.model.Test;
import com.epam.freelancer.database.model.Worker;

import java.util.List;

public interface TestQuestionManyToManyDao extends
        GenericManyToManyDao<Test, Question, Worker, Integer>
{
    List<Question> getQuestionsByTestId(Integer id);
}

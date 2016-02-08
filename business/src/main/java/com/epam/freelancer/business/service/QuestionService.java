package com.epam.freelancer.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.QuestionDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Question;

/**
 * Created by Максим on 18.01.2016.
 */
public class QuestionService extends GenericService<Question, Integer> {

    public QuestionService() {
        super(DAOManager.getInstance().getDAO(QuestionDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public Question create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        Question question = new Question();
        String[] value = data.get("name");
        question.setName(value != null ? value[0] : null);
        value = data.get("tech_id");
        Integer integer = value != null ? Integer.parseInt(value[0]) : null;
        if(integer==null)
            throw new RuntimeException("Validation exception");
        question.setTechId(integer);
        value = data.get("admin_id");
        integer = value != null ? Integer.parseInt(value[0]) : null;
        if(integer==null)
            throw new RuntimeException("Validation exception");
        question.setAdminId(integer);
        value = data.get("multiple");
        Boolean correct = value != null ? Boolean.parseBoolean(value[0]) : null;
        if(correct==null)
            throw new RuntimeException("Validation exception");
        question.setMultiple(correct);

        question = genericDao.save(question);

        return question;
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(false).minLength(5)
                        .maxLength(255),
                data.get("name") == null ? null : data.get("name")[0]);
        return map;
    }

    public List<Question> findQuestionsByTechnologyId(Integer id) {
        return ((QuestionDao) genericDao).getByTechnology(id);
    }

    public List<Question> findQuestionsByAdminId(Integer id) {
        return ((QuestionDao) genericDao).getByAdminId(id);
    }
}
package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.AnswerDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Answer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 18.01.2016.
 */
public class AnswerService extends GenericService<Answer, Integer> {

	public AnswerService() {
		super(DAOManager.getInstance().getDAO(AnswerDao.class.getSimpleName()));
		DAOManager daoManager = DAOManager.getInstance();
		genericDao.setConnectionPool(daoManager.getConnectionPool());
	}

	@Override
	public Answer create(Map<String, String[]> data) {
		if (!isDataValid(prepareData(data)))
			throw new RuntimeException("Validation exception");

		Answer answer = new Answer();
		String[] value = data.get("name");
		answer.setName(value != null ? value[0] : null);
		value = data.get("correct");
		Boolean correct = value != null ? Boolean.parseBoolean(value[0]) : null;
		if (correct == null)
			throw new RuntimeException("Validation exception");
		answer.setCorrect(correct);
		value = data.get("question_id");
		Integer questionId = value != null ? Integer.parseInt(value[0]) : null;
		if (questionId == null)
			throw new RuntimeException("Validation exception");
		answer.setQuestionId(questionId);

		return genericDao.save(answer);
	}

	private Map<ValidationParametersBuilder.Parameters, String> prepareData(
			Map<String, String[]> data)
	{
		Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
		map.put(ValidationParametersBuilder.createParameters(false)
				.minLength(1).maxLength(50), data.get("name") == null ? null
				: data.get("name")[0]);
		return map;
	}

	public List<Answer> findAnswersbyQuestionId(Integer id) {
		return ((AnswerDao) genericDao).getAnswersByQuestionId(id);
	}
}
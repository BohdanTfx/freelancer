package com.epam.freelancer.web.json.model;

import java.util.List;

/**
 * Created by ������ on 28.01.2016.
 */
public class Quest {
    private Integer questionId;
    private List<Integer> answersId;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public List<Integer> getAnswersId() {
        return answersId;
    }

    public void setAnswersId(List<Integer> answersId) {
        this.answersId = answersId;
    }
}

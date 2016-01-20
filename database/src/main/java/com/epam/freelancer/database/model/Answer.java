package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

/**
 * Created by ������ on 15.01.2016.
 */

@Table(name = "answer")
public class Answer implements BaseEntity<Integer> {

    @Id
    private Integer id;
    @Column(name = "quest_id")
    private Integer questionId;
    @Column
    private Boolean correct;
    @Column
    private String name;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;

    public Answer() {
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Boolean getDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (questionId != null ? !questionId.equals(answer.questionId) : answer.questionId != null) return false;
        if (correct != null ? !correct.equals(answer.correct) : answer.correct != null) return false;
        if (name != null ? !name.equals(answer.name) : answer.name != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(answer.isDeleted) : answer.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = questionId != null ? questionId.hashCode() : 0;
        result = 31 * result + (correct != null ? correct.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

	@Override
	public String toString() {
		return "Answer [questionId=" + questionId + ", correct=" + correct + ", name=" + name + ", isDeleted="
				+ isDeleted + ", version=" + version + "]";
	}
    
    


}

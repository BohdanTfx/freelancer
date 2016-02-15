package com.epam.freelancer.database.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "question")
public class Question implements BaseEntity<Integer> {
    @Id
    private Integer id;
    @Column
    private String name;
    @Column(name = "tech_id")
    private Integer techId;
    @Column(name = "admin_id")
    private Integer adminId;
    @Column
    private Boolean multiple;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;

    private List<Answer> answers;
    private Technology technology;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

        Question question = (Question) o;

        if (name != null ? !name.equals(question.name) : question.name != null) return false;
        if (techId != null ? !techId.equals(question.techId) : question.techId != null) return false;
        if (adminId != null ? !adminId.equals(question.adminId) : question.adminId != null) return false;
        if (multiple != null ? !multiple.equals(question.multiple) : question.multiple != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(question.isDeleted) : question.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (techId != null ? techId.hashCode() : 0);
        result = 31 * result + (adminId != null ? adminId.hashCode() : 0);
        result = 31 * result + (multiple != null ? multiple.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", techId=" + techId +
                ", adminId=" + adminId +
                ", multiple=" + multiple +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                '}';
    }
}

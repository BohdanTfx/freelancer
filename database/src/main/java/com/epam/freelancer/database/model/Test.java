package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

import java.util.List;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "test")
public class Test implements BaseEntity<Integer> {
    @Id
    private Integer id;
    @Column(name = "tech_id")
    private Integer techId;
    @Column
    private String name;
    @Column(name = "admin_id")
    private Integer adminId;
    @Column(name = "pass_score")
    private Integer passScore;
    @Column(name = "sec_per_quest")
    private Integer secPerQuest;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;
    private Technology technology;

    private List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public Integer getTechId() {
        return techId;
    }

    public void setTechId(Integer techId) {
        this.techId = techId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getPassScore() {
        return passScore;
    }

    public void setPassScore(Integer passScore) {
        this.passScore = passScore;
    }

    public Integer getSecPerQuest() {
        return secPerQuest;
    }

    public void setSecPerQuest(Integer secPerQuest) {
        this.secPerQuest = secPerQuest;
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

        Test test = (Test) o;

        if (techId != null ? !techId.equals(test.techId) : test.techId != null) return false;
        if (name != null ? !name.equals(test.name) : test.name != null) return false;
        if (adminId != null ? !adminId.equals(test.adminId) : test.adminId != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(test.isDeleted) : test.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = techId != null ? techId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (adminId != null ? adminId.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", techId=" + techId +
                ", name='" + name + '\'' +
                ", adminId=" + adminId +
                ", passScore=" + passScore +
                ", secPerQuest=" + secPerQuest +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                ", technology=" + technology +
                ", questions=" + questions +
                '}';
    }
}

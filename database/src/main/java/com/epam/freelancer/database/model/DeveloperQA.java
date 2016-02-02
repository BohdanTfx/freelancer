package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

import java.sql.Date;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "developer_qa")
public class DeveloperQA implements BaseEntity<Integer> {
    @Id
    private Integer id;
    @Column(name = "dev_id")
    private Integer devId;
    @Column(name = "test_id")
    private Integer testId;
    @Column
    private Double rate;
    @Column
    private Date expire;
    @Column(name = "is_expire")
    private Boolean isExpire;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;
    private Test test;
    private boolean isPassed;
    private boolean isOutOfDate;

    public DeveloperQA() {
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public boolean isOutOfDate() {
        return isOutOfDate;
    }

    public void setIsOutOfDate(boolean isOutOfDate) {
        this.isOutOfDate = isOutOfDate;
    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public Boolean getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(Boolean isExpire) {
        this.isExpire = isExpire;
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
        return null;
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

        DeveloperQA that = (DeveloperQA) o;

        if (devId != null ? !devId.equals(that.devId) : that.devId != null) return false;
        if (testId != null ? !testId.equals(that.testId) : that.testId != null) return false;
        if (expire != null ? !expire.equals(that.expire) : that.expire != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(that.isDeleted) : that.isDeleted != null);

    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public int hashCode() {
        int result = devId != null ? devId.hashCode() : 0;
        result = 31 * result + (testId != null ? testId.hashCode() : 0);
        result = 31 * result + (expire != null ? expire.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeveloperQA{" +
                "id=" + id +
                ", devId=" + devId +
                ", testId=" + testId +
                ", rate=" + rate +
                ", expire=" + expire +
                ", isExpire=" + isExpire +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                ", test=" + test +
                ", isPassed=" + isPassed +
                ", isOutOfDate=" + isOutOfDate +
                '}';
    }
}

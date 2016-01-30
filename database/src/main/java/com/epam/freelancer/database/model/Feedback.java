package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "feedback")
public class Feedback implements BaseEntity<Integer> {
    @Id
    private Integer id;
    @Column(name = "dev_id")
    private Integer devId;
    @Column(name = "cust_id")
    private Integer customerId;
    @Column
    private String comment;
    @Column
    private Integer rate;
    @Column
    private String author;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;
    private Customer customer;

    public Feedback() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

        Feedback feedback = (Feedback) o;

        if (devId != null ? !devId.equals(feedback.devId) : feedback.devId != null) return false;
        if (customerId != null ? !customerId.equals(feedback.customerId) : feedback.customerId != null) return false;
        if (comment != null ? !comment.equals(feedback.comment) : feedback.comment != null) return false;
        if (author != null ? !author.equals(feedback.author) : feedback.author != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(feedback.isDeleted) : feedback.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = devId != null ? devId.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", devId=" + devId +
                ", customerId=" + customerId +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                ", author='" + author + '\'' +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                ", customer=" + customer +
                '}';
    }
}

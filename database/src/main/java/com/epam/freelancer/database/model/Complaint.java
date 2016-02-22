package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

@Table(name = "complaint")
public class Complaint implements BaseEntity<Integer> {

    @Id(name = "id")
    private Integer id;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "dev_id")
    private Integer devId;
    @Column(name = "cust_id")
    private Integer custId;
    private Boolean isDeleted;
    private Integer version;

    public Complaint() {
    }

    public Complaint(Integer id, Integer orderId, Integer devId, Boolean isDeleted, Integer version) {
        this.id = id;
        this.orderId = orderId;
        this.devId = devId;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", devId=" + devId +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Boolean getDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer order_id) {
        this.orderId = order_id;
    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer dev_id) {
        this.devId = dev_id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }
}

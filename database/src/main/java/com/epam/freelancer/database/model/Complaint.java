package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

@Table(name = "complaint")
public class Complaint implements BaseEntity<Integer> {

    @Id
    private Integer id;
    @Column(name = "order_id")
    private Integer order_id;
    @Column(name = "dev_id")
    private Integer dev_id;
    @Column(name = "isDeleted")
    private Boolean isDeleted;
    @Column(name = "version")
    private Integer version;

    public Complaint() {
    }

    public Complaint(Integer id, Integer order_id, Integer dev_id, Boolean isDeleted, Integer version) {
        this.id = id;
        this.order_id = order_id;
        this.dev_id = dev_id;
        this.isDeleted = isDeleted;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complaint complaint = (Complaint) o;

        if (!id.equals(complaint.id)) return false;
        if (!order_id.equals(complaint.order_id)) return false;
        if (!dev_id.equals(complaint.dev_id)) return false;
        if (isDeleted != null ? !isDeleted.equals(complaint.isDeleted) : complaint.isDeleted != null) return false;
        return !(version != null ? !version.equals(complaint.version) : complaint.version != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + order_id.hashCode();
        result = 31 * result + dev_id.hashCode();
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getDev_id() {
        return dev_id;
    }

    public void setDev_id(Integer dev_id) {
        this.dev_id = dev_id;
    }
}

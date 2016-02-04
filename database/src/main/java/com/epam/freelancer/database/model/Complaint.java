package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

@Table(name = "complaint")
public class Complaint {

    @Id
    private Integer id;
    @Column(name = "order_id")
    private Integer order_id;
    @Column(name = "dev_id")
    private Integer dev_id;

    public Complaint() {
    }

    public Complaint(Integer id, Integer order_id, Integer dev_id) {
        this.id = id;
        this.order_id = order_id;
        this.dev_id = dev_id;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", order_id=" + order_id +
                ", dev_id=" + dev_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Complaint complaint = (Complaint) o;

        if (!id.equals(complaint.id)) return false;
        if (!order_id.equals(complaint.order_id)) return false;
        return dev_id.equals(complaint.dev_id);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + order_id.hashCode();
        result = 31 * result + dev_id.hashCode();
        return result;
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

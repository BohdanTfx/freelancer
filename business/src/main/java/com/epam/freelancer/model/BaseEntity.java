package com.epam.freelancer.model;

public interface BaseEntity<ID> {
    ID getId();

    void setId(ID id);
}

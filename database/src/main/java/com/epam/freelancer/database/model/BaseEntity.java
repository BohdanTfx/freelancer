package com.epam.freelancer.database.model;

public interface BaseEntity<ID> {
    ID getId();

    void setId(ID id);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);

    Integer getVersion();

    void setVersion(Integer version);
}

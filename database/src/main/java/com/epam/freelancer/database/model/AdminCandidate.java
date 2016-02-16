package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by Rynik on 05.02.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "admin_candidate")
public class AdminCandidate implements BaseEntity<Integer> {

    @Id
    private Integer id;
    @Column
    private String email;
    @Column(name="access_key")
    private String key;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    private Integer version;


    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

//    public Timestamp getExpire() {
//        return expire;
//    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

//    public void setExpire(Timestamp expire) {
//        this.expire = expire;
//    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminCandidate that = (AdminCandidate) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (isDeleted != null ? !isDeleted.equals(that.isDeleted) : that.isDeleted != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        return !(version != null ? !version.equals(that.version) : that.version != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdminCandidate{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", key='" + key + '\'' +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                '}';
    }
}

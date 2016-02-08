package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by ������ on 16.01.2016.
 */
@Table(name = "contact")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact implements BaseEntity<Integer> {
    @Id
    private Integer id;
    @Column(name = "cust_id")
    private Integer customId;
    @Column(name = "dev_id")
    private Integer devId;
    @Column
    private String phone;
    @Column
    private String skype;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;

    public Integer getCustomId() {
        return customId;
    }

    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    public Integer getDevId() {
        return devId;
    }

    public void setDevId(Integer devId) {
        this.devId = devId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
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

        Contact contact = (Contact) o;

        if (customId != null ? !customId.equals(contact.customId) : contact.customId != null) return false;
        if (devId != null ? !devId.equals(contact.devId) : contact.devId != null) return false;
        if (phone != null ? !phone.equals(contact.phone) : contact.phone != null) return false;
        if (skype != null ? !skype.equals(contact.skype) : contact.skype != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(contact.isDeleted) : contact.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = customId != null ? customId.hashCode() : 0;
        result = 31 * result + (devId != null ? devId.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (skype != null ? skype.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", customId=" + customId +
                ", devId=" + devId +
                ", phone='" + phone + '\'' +
                ", skype='" + skype + '\'' +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                '}';
    }
}

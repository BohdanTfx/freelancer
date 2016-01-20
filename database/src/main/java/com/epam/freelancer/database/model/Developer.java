package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Locale;

/**
 * Created by ������ on 15.01.2016.
 */
@Table(name = "developer")
public class Developer implements UserEntity {
    @Id
    private Integer id;
    @Column
    private String email;
    @Column
    private String password;
    @Column(name = "name")
    private String fname;
    @Column(name = "last_name")
    private String lname;
    @Column
    private Double hourly;
    @Column
    private Timestamp zone;
    private Locale locale;
    @Column
    private String lang;
    @Column
    private String uuid;
    @Column(name = "reg_url")
    private String regUrl;
    @Column(name = "reg_date")
    private Timestamp regDate;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column
    private Integer version;
    @Column
    private String salt;

    public Developer() {
    }

    @Override
    public String getSalt() {
        return salt;
    }

    @Override
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Double getHourly() {
        return hourly;
    }

    public void setHourly(Double hourly) {
        this.hourly = hourly;
    }

    public Timestamp getZone() {
        return zone;
    }

    public void setZone(Timestamp zone) {
        this.zone = zone;
    }

    public String getRegUrl() {
        return regUrl;
    }

    public void setRegUrl(String regUrl) {
        this.regUrl = regUrl;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getLang() {
        return lang != null ? locale.toLanguageTag() : null;
    }

    public void setLang(String lang) {
        if (lang == null) {
            this.lang = null;
            return;
        }
        String[] langCode = lang.split("-");
        if (langCode.length == 2)
            this.locale = new Locale(langCode[0], langCode[1]);
        else
            this.locale = new Locale(langCode[0]);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getFname() {
        return fname;
    }

    @Override
    public void setFname(String fname) {
        this.fname = fname;
    }

    @Override
    public String getLname() {
        return lname;
    }

    @Override
    public void setLname(String lname) {
        this.lname = lname;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Developer developer = (Developer) o;

        if (email != null ? !email.equals(developer.email) : developer.email != null) return false;
        if (uuid != null ? !uuid.equals(developer.uuid) : developer.uuid != null) return false;
        return !(isDeleted != null ? !isDeleted.equals(developer.isDeleted) : developer.isDeleted != null);

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (isDeleted != null ? isDeleted.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", hourly=" + hourly +
                ", zone=" + zone +
                ", lang='" + lang + '\'' +
                ", uuid='" + uuid + '\'' +
                ", regUrl='" + regUrl + '\'' +
                ", regDate=" + regDate +
                ", isDeleted=" + isDeleted +
                ", version=" + version +
                '}';
    }
}

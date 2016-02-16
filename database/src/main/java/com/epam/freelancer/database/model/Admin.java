package com.epam.freelancer.database.model;

import com.epam.freelancer.database.transformer.annotation.Column;
import com.epam.freelancer.database.transformer.annotation.Id;
import com.epam.freelancer.database.transformer.annotation.Table;

import java.sql.Timestamp;
import java.util.Locale;

/**
 * Created by ������ on 15.01.2016.
 */
@Table(name = "admin", getValuesByField = false)
public class Admin implements UserEntity {
	private Integer id;
	private String email;
	private String password;
	private String fname;
	private String lname;
	private String lang;
	private Locale locale;
	private String uuid;
	private String regUrl;
	private Timestamp regDate;
	private Boolean isDeleted;
	private Integer version;
	private String salt;
	private String imgUrl;
    private String role;
	private String confirmCode;
	private String sendEmail;

	@Column(name = "send_email")
	public String getSendEmail() { return sendEmail; }

	public void setSendEmail(String sendEmail) { this.sendEmail = sendEmail; }

	public String getConfirmCode() { return confirmCode; }

	public void setConfirmCode(String confirmCode) { this.confirmCode = confirmCode; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
	@Column
	public String getSalt() {
		return salt;
	}

	@Override
	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		if (this.locale == null)
			this.locale = locale;
	}

	@Column
	public String getLang() {
		return locale != null ? locale.toLanguageTag() : null;
	}

	public void setLang(String lang) {
		if (lang == null) {
			this.lang = null;
			return;
		}
		this.lang = lang;
		String[] langCode = lang.split("-");
		if (langCode.length == 2)
			this.locale = new Locale(langCode[0], langCode[1]);
		else
			this.locale = new Locale(langCode[0]);
	}

	@Override
	@Column
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	@Column
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@Column
	public String getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	@Column(name = "name")
	public String getFname() {
		return fname;
	}

	@Override
	public void setFname(String fname) {
		this.fname = fname;
	}

	@Override
	@Column(name = "last_name")
	public String getLname() {
		return lname;
	}

	@Override
	public void setLname(String lname) {
		this.lname = lname;
	}

	@Override
	@Id
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	@Column(name = "is_deleted")
	public Boolean getDeleted() {
		return isDeleted;
	}

	@Override
	public void setDeleted(Boolean deleted) {
		this.isDeleted = deleted;
	}

	@Override
	@Column
	public Integer getVersion() {
		return version;
	}

	@Override
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Admin admin = (Admin) o;

		if (email != null ? !email.equals(admin.email) : admin.email != null)
			return false;
		if (uuid != null ? !uuid.equals(admin.uuid) : admin.uuid != null)
			return false;
		return !(isDeleted != null ? !isDeleted.equals(admin.isDeleted)
				: admin.isDeleted != null);

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
		return "Admin{" + "id=" + id + ", email='" + email + '\'' + ", fname='"
				+ fname + '\'' + ", lname='" + lname + '\'' + ", lang='" + lang
				+ '\'' + ", uuid='" + uuid + '\'' + ", isDeleted=" + isDeleted
				+ ", version=" + version + '}';
	}

	@Column(name = "reg_url")
	public String getRegUrl() {
		return regUrl;
	}

	public void setRegUrl(String regUrl) {
		this.regUrl = regUrl;
	}

	@Column(name = "reg_date")
	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	@Override
	public void setImgUrl(String imgUrl) {
		this.imgUrl=imgUrl;
	}

	@Override
	@Column(name="img_url")
	public String getImgUrl() {
		return imgUrl;
	}
}

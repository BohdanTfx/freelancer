package com.epam.freelancer.database.model;

import java.sql.Timestamp;
import java.util.Locale;

public interface UserEntity extends BaseEntity<Integer> {
	String getEmail();

	void setEmail(String email);

	String getPassword();

	void setPassword(String password);

	String getUuid();

	void setUuid(String uuid);

	Locale getLocale();

	void setLocale(Locale locale);

	String getFname();

	void setFname(String fname);

	String getLname();

	void setRole(String role);

	String getRole();

	void setLname(String lname);

	String getLang();

	void setLang(String lang);

	String getSalt();

	void setSalt(String salt);

	public String getRegUrl();

	public void setRegUrl(String regUrl);

	public Timestamp getRegDate();

	public void setRegDate(Timestamp regDate);

	public void setImgUrl(String imgUrl);

	public String getImgUrl();

	public String getConfirmCode();

	public void setConfirmCode(String confirmCode);
}

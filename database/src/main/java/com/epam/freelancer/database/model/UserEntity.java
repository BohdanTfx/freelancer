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

	void setLname(String lname);

	String getRole();

	void setRole(String role);

	String getLang();

	void setLang(String lang);

	String getSalt();

	void setSalt(String salt);

	String getRegUrl();

	void setRegUrl(String regUrl);

	Timestamp getRegDate();

	void setRegDate(Timestamp regDate);

	String getImgUrl();

	void setImgUrl(String imgUrl);

	String getConfirmCode();

	void setConfirmCode(String confirmCode);
}

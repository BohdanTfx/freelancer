package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.Contact;

/**
 * Created by ������ on 17.01.2016.
 */
public interface ContactDao extends GenericDao<Contact, Integer> {

	Contact getContactByDevId(Integer id);

	Contact getContactByCustId(Integer id);

}

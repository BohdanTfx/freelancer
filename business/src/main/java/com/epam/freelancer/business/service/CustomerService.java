package com.epam.freelancer.business.service;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.ContactDao;
import com.epam.freelancer.database.dao.CustomerDao;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.jdbc.ContactJdbcDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Максим on 18.01.2016.
 */
public class CustomerService extends UserService<Customer> {

    private GenericDao<Contact, Integer> contactDao;

    public CustomerService() {
        super(DAOManager.getInstance().getDAO(CustomerDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
//        try {
//			contactDao = new ContactJdbcDao();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

    @Override
    public Customer create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        Customer entity = new Customer();
        String[] value = data.get("first_name");
        entity.setFname(value != null ? value[0] : null);
        value = data.get("last_name");
        entity.setLname(value != null ? value[0] : null);
        value = data.get("email");
        entity.setEmail(value != null ? value[0] : null);
        value = data.get("lang");
        entity.setLang(value != null ? value[0] : "en");
        value = data.get("uuid");
        entity.setUuid(value != null ? value[0] : null);
        value = data.get("reg_url");
        entity.setRegUrl(value != null ? value[0] : null);
        entity.setRegDate(new Timestamp(new java.util.Date().getTime()));
        value = data.get("password");
        entity.setPassword(value != null ? value[0] : null);

        encodePassword(entity);

        return genericDao.save(entity);
    }

    public void deleteCustomer(Customer Customer){
        genericDao.delete(Customer);
    }

    public Customer updateCustomer(Customer Customer){
        return genericDao.update(Customer);
    }

    public Customer getCustomer(Integer id){
        return genericDao.getById(id);
    }

    public List<Customer> getAllCustomers(){
        return genericDao.getAll();
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(Map<String, String[]> data) {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(50)
                        .minLength(1),
                data.get("first_name") == null ? null
                        : data.get("first_name")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(50)
                        .minLength(1),
                data.get("last_name") == null ? null : data.get("last_name")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(50)
                        .minLength(1),
                data.get("lang") == null ? null : data.get("lang")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(50)
                        .minLength(1),
                data.get("uuid") == null ? null : data.get("uuid")[0]);
        map.put(ValidationParametersBuilder.createParameters(false).maxLength(140)
                        .minLength(1),
                data.get("password") == null ? null : data.get("password")[0]);
        map.put(ValidationParametersBuilder
                        .createParameters(false)
                        .maxLength(50)
                        .pattern(
                                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]+)"),
                data.get("email") == null ? null : data.get("email")[0]);

        return map;
    }

    public GenericDao<Contact, Integer> getContactDao() {
        return contactDao;
    }

    public void setContactDao(GenericDao<Contact, Integer> contactDao) {
        this.contactDao = contactDao;
    }

    public Contact getContactByCustomerId(Integer id){
        return ((ContactDao)contactDao).getContactByCustId(id);
    }

    public Contact updateContact(Contact contact){
        return contactDao.update(contact);
    }

    public void deleteContact(Contact contact){
        contactDao.delete(contact);
    }
}

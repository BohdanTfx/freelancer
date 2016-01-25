package com.epam.freelancer.business.service;

import java.util.HashMap;
import java.util.Map;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.AdminDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.Admin;

/**
 * Created by Максим on 18.01.2016.
 */
public class AdminService extends UserService<Admin> {

    public AdminService() {
        super(DAOManager.getInstance().getDAO(AdminDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }

    @Override
    public Admin create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        Admin entity = new Admin();
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
        value = data.get("password");
        entity.setPassword(value != null ? value[0] : null);

        encodePassword(entity);

        return genericDao.save(entity);
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
}

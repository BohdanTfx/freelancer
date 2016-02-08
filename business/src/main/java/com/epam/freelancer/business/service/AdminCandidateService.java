package com.epam.freelancer.business.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epam.freelancer.business.util.ValidationParametersBuilder;
import com.epam.freelancer.database.dao.AdminCandidateDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.model.AdminCandidate;

/**
 * Created by Rynik on 05.02.2016.
 */
public class AdminCandidateService extends GenericService<AdminCandidate, Integer> {

    public AdminCandidateService() {
        super(DAOManager.getInstance().getDAO(AdminCandidateDao.class.getSimpleName()));
        DAOManager daoManager = DAOManager.getInstance();
        genericDao.setConnectionPool(daoManager.getConnectionPool());
    }


    @Override
    public AdminCandidate create(Map<String, String[]> data) {
        if (!isDataValid(prepareData(data)))
            throw new RuntimeException("Validation exception");

        AdminCandidate adminCandidate = new AdminCandidate();
        String[] value = data.get("email");
        adminCandidate.setEmail(value != null ? value[0] : null);
        value = data.get("access_key");
        adminCandidate.setKey(value != null ? value[0] : null);

        return genericDao.save(adminCandidate);
    }

    private Map<ValidationParametersBuilder.Parameters, String> prepareData(
            Map<String, String[]> data)
    {
        Map<ValidationParametersBuilder.Parameters, String> map = new HashMap<>();
        map.put(ValidationParametersBuilder.createParameters(false)
                        .maxLength(50)
                        .pattern(
                                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]+)"),
                data.get("email") == null ? null : data.get("email")[0]);
        map.put(ValidationParametersBuilder.createParameters(false)
                .minLength(5).maxLength(80), data.get("access_key") == null ? null
                : data.get("access_key")[0]);
        return map;
    }


    public AdminCandidate getAdminCandidateByEmail(String email) {
        return ((AdminCandidateDao) genericDao).getAdminCandidateByEmail(email);
    }

    public List<AdminCandidate> getAdminCandidateByExpireDate(Timestamp timestamp) {
        return ((AdminCandidateDao) genericDao).getAdminCandidateByExpireDate(timestamp);
    }
    public AdminCandidate getAdminCandidateByKey(String key) {
        return ((AdminCandidateDao) genericDao).getAdminCandidateByKey(key);
    }


}

package com.epam.freelancer.database.dao;

import com.epam.freelancer.database.model.AdminCandidate;
import com.epam.freelancer.database.model.Answer;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Rynik on 05.02.2016.
 */
public interface AdminCandidateDao extends GenericDao<AdminCandidate, Integer> {

    AdminCandidate getAdminCandidateByEmail(String  email);
    AdminCandidate getAdminCandidateByKey(String  key);
    List<AdminCandidate> getAdminCandidateByExpireDate(Timestamp timestamp);

}

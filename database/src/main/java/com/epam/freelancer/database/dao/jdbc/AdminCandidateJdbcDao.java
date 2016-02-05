package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.AdminCandidateDao;
import com.epam.freelancer.database.dao.AnswerDao;
import com.epam.freelancer.database.model.AdminCandidate;
import com.epam.freelancer.database.model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rynik on 05.02.2016.
 */
public class AdminCandidateJdbcDao extends GenericJdbcDao<AdminCandidate, Integer> implements AdminCandidateDao {
    public AdminCandidateJdbcDao() throws Exception {
        super(AdminCandidate.class);
    }



    public AdminCandidate getAdminCandidateByKey(String  key) {
        AdminCandidate adminCandidate = null;
        String query = "SELECT * FROM " + table + " WHERE access_key = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setString(1, key);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    adminCandidate = transformer.getObject(set);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminCandidate;
    }

    public AdminCandidate getAdminCandidateByEmail(String  email) {
        AdminCandidate adminCandidate = null;
        String query = "SELECT * FROM " + table + " WHERE email = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    adminCandidate = transformer.getObject(set);
                   }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminCandidate;
    }


    public List<AdminCandidate> getAdminCandidateByExpireDate(Timestamp timestamp) {
        List<AdminCandidate> adminCandidates = new ArrayList<>();
        String query = "SELECT * FROM " + table + " WHERE expire = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setTimestamp(1, timestamp);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    AdminCandidate candidate;
                    candidate = transformer.getObject(set);
                    adminCandidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminCandidates;
    }
}

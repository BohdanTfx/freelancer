package com.epam.freelancer.database.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.epam.freelancer.database.dao.FollowerDao;
import com.epam.freelancer.database.model.Follower;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ������ on 17.01.2016.
 */
public class FollowerJdbcDao extends GenericJdbcDao<Follower, Integer>
		implements FollowerDao
{
	public FollowerJdbcDao() throws Exception {
		super(Follower.class);
	}

	@Override
	public List<Follower> getDeveloperFollowings(Integer developerId) {
		String query = "SELECT * FROM " + table
				+ " WHERE dev_id = ? and author LIKE ?";
		List<Follower> followers = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, developerId);
			statement.setString(2, "dev");
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					followers.add(transformer.getObject(set));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return followers;
	}

	@Override
	public List<Follower> getCustomerInvitation(Integer customerId) {
		String query = "SELECT * FROM " + table
				+ " WHERE cust_id = ? and author LIKE ?";
		List<Follower> followers = new ArrayList<>();
		try (Connection connection = connectionPool.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(query)) {
			statement.setInt(1, customerId);
			statement.setString(2, "customer");
			try (ResultSet set = statement.executeQuery()) {
				while (set.next()) {
					followers.add(transformer.getObject(set));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return followers;
	}
public class FollowerJdbcDao extends GenericJdbcDao<Follower, Integer> implements FollowerDao {
    public FollowerJdbcDao() throws Exception {
        super(Follower.class);
    }

    @Override
    public List<Follower> getProjectFollowers(Integer id) {
        String query = "SELECT * FROM " + table + " WHERE order_id = ?";
        List<Follower> followers = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    Follower follower = transformer.getObject(set);
                    followers.add(follower);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return followers;
    }
}

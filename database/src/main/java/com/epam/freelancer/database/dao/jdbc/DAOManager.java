package com.epam.freelancer.database.dao.jdbc;

import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.GenericManyToManyDao;
import com.epam.freelancer.database.model.BaseEntity;
import com.epam.freelancer.database.persistence.ConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class DAOManager {
    private ConnectionPool connectionPool;
    private Map<String, Object> daos = new HashMap<>();

    DAOManager() {
    }

    public static DAOManager getInstance() {
        return DAOManagerHolder.INSTANCE;
    }

    private void initConnectionPool() throws ClassNotFoundException,
            IOException, SQLException, InstantiationException,
            IllegalAccessException {
        Properties properties = getDatasourceProperties("/database.properties");
        connectionPool = new ConnectionPool(properties);
    }

    private Properties getDatasourceProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStream file = GenericDao.class.getResourceAsStream(path)) {
            properties.load(file);
        }
        return properties;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void addDao(String name, Object dao) {
        daos.put(name, dao);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseEntity<ID>, ID> GenericDao<T, ID> getDAO(
            String daoName) {
        return (GenericDao<T, ID>) daos.get(daoName);
    }

    @SuppressWarnings("unchecked")
    public <F extends BaseEntity<ID>, S extends BaseEntity<ID>, M extends BaseEntity<ID>, ID> GenericManyToManyDao<F, S, M, ID> getManyToManyDAO(
            String daoName) {
        return (GenericManyToManyDao<F, S, M, ID>) daos.get(daoName);
    }

    private static final class DAOManagerHolder {
        private static final DAOManager INSTANCE = new DAOManager();
        static {
            try {
                INSTANCE.initConnectionPool();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
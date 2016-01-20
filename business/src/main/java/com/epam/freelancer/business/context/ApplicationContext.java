package com.epam.freelancer.business.context;

import com.epam.freelancer.business.service.*;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.*;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Developer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ApplicationContext {
    private Map<String, Object> beans = new ConcurrentHashMap<>();

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        return ApplicationContextHolder.INSTANCE;
    }
    
    public static void main(String[] args) {
    	DeveloperService developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
    	System.out.println(developerService.getAllDevelopers());

	}

    private void initContext() {
        initDAO();
        DAOManager daoManager = DAOManager.getInstance();

        DeveloperService developerService = new DeveloperService();
        developerService.setWorkerMTMDao(daoManager.getManyToManyDAO(WorkerManyToManyDao.class.getSimpleName()));
        developerService.setDevMTMtechDao(daoManager.getManyToManyDAO(DevTechManyToManyDao.class.getSimpleName()));
        developerService.setWorkerDao(daoManager.getDAO(WorkerDao.class.getSimpleName()));
        developerService.setContactDao(daoManager.getDAO(ContactDao.class.getSimpleName()));
        addBean("developerService", developerService);
        addBean("adminService", new AdminService());
        addBean("customerService", new CustomerService());
        addBean("developerQAService", new DeveloperQAService());
        addBean("feedbackService", new FeedbackService());
        addBean("orderingService", new OrderingService());
        addBean("questionService", new QuestionService());
        addBean("answerService", new AnswerService());
        addBean("testService", new TestService());
    }

    private void initDAO() {
        DAOManager daoManager = DAOManager.getInstance();
        try {
            daoManager.addDao(AdminDao.class.getSimpleName(), new AdminJdbcDao());
            daoManager.addDao(AnswerDao.class.getSimpleName(), new AnswerJdbcDao());
            daoManager.addDao(ContactDao.class.getSimpleName(), new ContactJdbcDao());
            daoManager.addDao(CustomerDao.class.getSimpleName(), new CustomerJdbcDao());
            daoManager.addDao(DeveloperDao.class.getSimpleName(), new DeveloperJdbcDao());
            daoManager.addDao(DeveloperQADao.class.getSimpleName(), new DeveloperQAJdbcDao());
            daoManager.addDao(DevTechManyToManyDao.class.getSimpleName(), new DevTechManyToManyJdbcDao());
            daoManager.addDao(FeedbackDao.class.getSimpleName(), new FeedbackJdbcDao());
//            daoManager.addDao(FollowerDao.class.getSimpleName(), new Folo);
//            daoManager.addDao(FollowerManyToManyDao.class.getSimpleName(), new fo);
            daoManager.addDao(OrderingDao.class.getSimpleName(), new OrderingJdbcDao());
            daoManager.addDao(QuestionDao.class.getSimpleName(), new QuestionJdbcDao());
            daoManager.addDao(TechnologyDao.class.getSimpleName(), new TechnologyJdbcDao());
            daoManager.addDao(TestDao.class.getSimpleName(), new TestJdbcDao());
            daoManager.addDao(WorkerDao.class.getSimpleName(), new WorkerJdbcDao());
            daoManager.addDao(WorkerManyToManyDao.class.getSimpleName(), new WorkerManyToManyJdbcDao());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Object getBean(String key) {
        return beans.get(key);
    }

    public Object addBean(String key, Object bean) {
        return beans.put(key, bean);
    }

    private static final class ApplicationContextHolder {
        private static final ApplicationContext INSTANCE = new ApplicationContext();

        static {
            INSTANCE.initContext();
        }
    }

}

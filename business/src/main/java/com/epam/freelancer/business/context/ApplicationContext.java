package com.epam.freelancer.business.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.CookieManager;
import com.epam.freelancer.database.dao.AdminDao;
import com.epam.freelancer.database.dao.AnswerDao;
import com.epam.freelancer.database.dao.ContactDao;
import com.epam.freelancer.database.dao.CustomerDao;
import com.epam.freelancer.database.dao.DevTechManyToManyDao;
import com.epam.freelancer.database.dao.DeveloperDao;
import com.epam.freelancer.database.dao.DeveloperQADao;
import com.epam.freelancer.database.dao.FeedbackDao;
import com.epam.freelancer.database.dao.OrderingDao;
import com.epam.freelancer.database.dao.OrderingTechnologyManyToManyDao;
import com.epam.freelancer.database.dao.QuestionDao;
import com.epam.freelancer.database.dao.TechnologyDao;
import com.epam.freelancer.database.dao.TestDao;
import com.epam.freelancer.database.dao.WorkerDao;
import com.epam.freelancer.database.dao.WorkerManyToManyDao;
import com.epam.freelancer.database.dao.jdbc.AdminJdbcDao;
import com.epam.freelancer.database.dao.jdbc.AnswerJdbcDao;
import com.epam.freelancer.database.dao.jdbc.ContactJdbcDao;
import com.epam.freelancer.database.dao.jdbc.CustomerJdbcDao;
import com.epam.freelancer.database.dao.jdbc.DAOManager;
import com.epam.freelancer.database.dao.jdbc.DevTechManyToManyJdbcDao;
import com.epam.freelancer.database.dao.jdbc.DeveloperJdbcDao;
import com.epam.freelancer.database.dao.jdbc.DeveloperQAJdbcDao;
import com.epam.freelancer.database.dao.jdbc.FeedbackJdbcDao;
import com.epam.freelancer.database.dao.jdbc.OrderingJdbcDao;
import com.epam.freelancer.database.dao.jdbc.OrderingTechnologyManyToManyJdbcDao;
import com.epam.freelancer.database.dao.jdbc.QuestionJdbcDao;
import com.epam.freelancer.database.dao.jdbc.TechnologyJdbcDao;
import com.epam.freelancer.database.dao.jdbc.TestJdbcDao;
import com.epam.freelancer.database.dao.jdbc.WorkerJdbcDao;
import com.epam.freelancer.database.dao.jdbc.WorkerManyToManyJdbcDao;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.*;
import com.epam.freelancer.database.model.DeveloperQA;
import com.epam.freelancer.database.model.Test;

public final class ApplicationContext {
	private Map<String, Object> beans = new ConcurrentHashMap<>();

	private ApplicationContext() {
	}

	public static ApplicationContext getInstance() {
		return ApplicationContextHolder.INSTANCE;
	}

	private void initContext() {
		initDAO();
		DAOManager daoManager = DAOManager.getInstance();

		DeveloperService developerService = new DeveloperService();
		developerService.setWorkerMTMDao(daoManager
                .getManyToManyDAO(WorkerManyToManyDao.class.getSimpleName()));
        developerService.setDevMTMtechDao(daoManager
                .getManyToManyDAO(DevTechManyToManyDao.class.getSimpleName()));
        developerService.setWorkerDao(daoManager.getDAO(WorkerDao.class
                .getSimpleName()));
        developerService.setContactDao(daoManager.getDAO(ContactDao.class
                .getSimpleName()));
        addBean("developerService", developerService);
        addBean("adminService", new AdminService());
        CustomerService customerService = new CustomerService();
        customerService.setContactDao(daoManager.getDAO(ContactDao.class
                .getSimpleName()));
        addBean("customerService", customerService);
        addBean("feedbackService", new FeedbackService());
        addBean("orderingService", new OrderingService());
		addBean("questionService", new QuestionService());
		addBean("testService", new TestService());


		TestService testService = new TestService();
		testService.setQuestionDao(daoManager.getDAO(QuestionDao.class.getSimpleName()));
		testService.setTestMTMquestDao(daoManager
				.getManyToManyDAO(TestQuestionManyToManyDao.class.getSimpleName()));
		testService.setAnswerDao(daoManager.getDAO(AnswerDao.class.getSimpleName()));
		addBean("testService", testService);

		DeveloperQAService developerQAService = new DeveloperQAService();
		developerQAService.setTechnologyDao(daoManager.getDAO(TechnologyDao.class.getSimpleName()));
		developerQAService.setTestDao(daoManager.getDAO(TestDao.class.getSimpleName()));
		addBean("developerQAService", developerQAService);

		TechnologyService technologyService = new TechnologyService();
		technologyService.setOrderingTechnoloyManyToManyDao(DAOManager
				.getInstance().getManyToManyDAO(
						OrderingTechnologyManyToManyDao.class.getSimpleName()));
		addBean("technologyService", technologyService);

		UserManager userManager = new UserManager();
		userManager.setCustomerService(customerService);
		userManager.setDeveloperService(developerService);
		userManager.setAdminService((AdminService) getBean("adminService"));
		addBean("userManager", userManager);
		addBean("cookieManager", new CookieManager());
	}

	private void initDAO() {
		DAOManager daoManager = DAOManager.getInstance();
		try {
			daoManager.addDao(AdminDao.class.getSimpleName(),
					new AdminJdbcDao());
			daoManager.addDao(AnswerDao.class.getSimpleName(),
					new AnswerJdbcDao());
			daoManager.addDao(ContactDao.class.getSimpleName(),
					new ContactJdbcDao());
			daoManager.addDao(CustomerDao.class.getSimpleName(),
					new CustomerJdbcDao());
			daoManager.addDao(DeveloperDao.class.getSimpleName(),
					new DeveloperJdbcDao());
			daoManager.addDao(DeveloperQADao.class.getSimpleName(),
					new DeveloperQAJdbcDao());
			daoManager.addDao(DevTechManyToManyDao.class.getSimpleName(),
					new DevTechManyToManyJdbcDao());
			daoManager.addDao(TestQuestionManyToManyDao.class.getSimpleName(),
					new TestQuestionManyToManyJdbcDao());
			daoManager.addDao(FeedbackDao.class.getSimpleName(),
					new FeedbackJdbcDao());
			// daoManager.addDao(FollowerDao.class.getSimpleName(), new Folo);
			// daoManager.addDao(FollowerManyToManyDao.class.getSimpleName(),
			// new fo);
			daoManager.addDao(OrderingDao.class.getSimpleName(),
					new OrderingJdbcDao());
			daoManager.addDao(QuestionDao.class.getSimpleName(),
					new QuestionJdbcDao());
			daoManager.addDao(TechnologyDao.class.getSimpleName(),
					new TechnologyJdbcDao());
			daoManager.addDao(TestDao.class.getSimpleName(), new TestJdbcDao());
			daoManager.addDao(WorkerDao.class.getSimpleName(),
					new WorkerJdbcDao());
			daoManager.addDao(WorkerManyToManyDao.class.getSimpleName(),
					new WorkerManyToManyJdbcDao());
			daoManager.addDao(
					OrderingTechnologyManyToManyDao.class.getSimpleName(),
					new OrderingTechnologyManyToManyJdbcDao());
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

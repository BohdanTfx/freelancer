package com.epam.freelancer.business.context;

import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.CookieManager;
import com.epam.freelancer.database.dao.*;
import com.epam.freelancer.database.dao.jdbc.*;
import com.epam.freelancer.database.model.Answer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
		developerService.setFollowerDao(daoManager.getDAO(FollowerDao.class
				.getSimpleName()));
		developerService.setContactDao(daoManager.getDAO(ContactDao.class
				.getSimpleName()));
		developerService.setFollowerMTMDevDao(daoManager
				.getManyToManyDAO(FollowerManyToManyDao.class.getSimpleName()));
		addBean("developerService", developerService);
		addBean("adminService", new AdminService());
		CustomerService customerService = new CustomerService();
		customerService.setContactDao(daoManager.getDAO(ContactDao.class
				.getSimpleName()));
		customerService.setOrderingDao(daoManager.getDAO(OrderingDao.class
				.getSimpleName()));
		customerService.setFollowerDao(daoManager.getDAO(FollowerDao.class
				.getSimpleName()));
		addBean("customerService", customerService);
		addBean("feedbackService", new FeedbackService());

		OrderingService orderingService = new OrderingService();
		orderingService.setOrderingTechnoloyManyToManyDao(DAOManager
				.getInstance().getManyToManyDAO(
						OrderingTechnologyManyToManyDao.class.getSimpleName()));
		orderingService.setFollowerDao(DAOManager.getInstance().getDAO(
				FollowerDao.class.getSimpleName()));
		addBean("orderingService", orderingService);

		addBean("questionService", new QuestionService());
		addBean("testService", new TestService());
		addBean("adminCandidateService", new AdminCandidateService());

		TestService testService = new TestService();
		testService.setQuestionDao(daoManager.getDAO(QuestionDao.class
				.getSimpleName()));
		testService.setTestMTMquestDao(daoManager
				.getManyToManyDAO(TestQuestionManyToManyDao.class
						.getSimpleName()));
		testService.setAnswerDao(daoManager.getDAO(AnswerDao.class
				.getSimpleName()));
//		testService.setAdminCandidatDao(daoManager.getDAO(AdminCandidateDao.class
//				.getSimpleName()));
		addBean("testService", testService);

		DeveloperQAService developerQAService = new DeveloperQAService();
		developerQAService.setTechnologyDao(daoManager
				.getDAO(TechnologyDao.class.getSimpleName()));
		developerQAService.setTestDao(daoManager.getDAO(TestDao.class
				.getSimpleName()));
		addBean("developerQAService", developerQAService);

		TechnologyService technologyService = new TechnologyService();
		technologyService.setOrderingTechnoloyManyToManyDao(DAOManager
				.getInstance().getManyToManyDAO(
						OrderingTechnologyManyToManyDao.class.getSimpleName()));
		addBean("technologyService", technologyService);

		addBean("answerService", new AnswerService());

		UserManager userManager = new UserManager();
		userManager.setCustomerService(customerService);
		userManager.setDeveloperService(developerService);
		userManager.setAdminService((AdminService) getBean("adminService"));
		addBean("userManager", userManager);
		addBean("cookieManager", new CookieManager());
        addBean("complaintService", new ComplaintService());
    }

	private void initDAO() {
		DAOManager daoManager = DAOManager.getInstance();
		try {
			daoManager.addDao(AdminDao.class.getSimpleName(),
					new AdminJdbcDao());
			daoManager.addDao(AnswerDao.class.getSimpleName(),
					new AnswerJdbcDao());
			daoManager.addDao(AdminCandidateDao.class.getSimpleName(),
					new AdminCandidateJdbcDao());
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
			daoManager.addDao(FollowerManyToManyDao.class.getSimpleName(),
					new FollowerManyToManyJdbcDao());
			daoManager.addDao(TestQuestionManyToManyDao.class.getSimpleName(),
					new TestQuestionManyToManyJdbcDao());
			daoManager.addDao(FollowerManyToManyDao.class.getSimpleName(),
					new FollowerManyToManyJdbcDao());
			daoManager.addDao(FeedbackDao.class.getSimpleName(),
					new FeedbackJdbcDao());
			daoManager.addDao(FollowerDao.class.getSimpleName(),
					new FollowerJdbcDao());
			daoManager.addDao(FollowerManyToManyDao.class.getSimpleName(),
					new FollowerManyToManyJdbcDao());
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
            daoManager.addDao(
                    FollowerDao.class.getSimpleName(),
                    new FollowerJdbcDao());
            daoManager.addDao(
                    ComplaintDao.class.getSimpleName(),
                    new ComplaintJdbcDao());
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

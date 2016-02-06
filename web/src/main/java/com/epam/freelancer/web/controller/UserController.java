package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.exceptions.OAuthException;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperQAService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.service.FeedbackService;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.DeveloperQA;
import com.epam.freelancer.database.model.Feedback;
import com.epam.freelancer.database.model.Follower;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.social.model.LinkedinProfile;
import com.epam.freelancer.web.util.Paginator;
import com.epam.freelancer.web.util.SignInType;

public class UserController extends HttpServlet implements Responsable {
	public static final Logger LOG = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = -2356506023594947745L;
	private AuthenticationProvider authenticationProvider;
	private FeedbackService feedbackService;
	private OrderingService orderingService;
	private AdminService adminService;
	private CustomerService customerService;
	private DeveloperService developerService;
	private TechnologyService technologyService;
	private UserManager userManager;
	private Linkedin linkedin;
	private ObjectMapper mapper;
	private Paginator paginator;

	public UserController() {
		init();
	}

	@Override
	public void init() {
		LOG.info(getClass().getSimpleName() + " - " + " loaded");
		linkedin = new Linkedin();
		mapper = new ObjectMapper();
		paginator = new Paginator();
		try {
			linkedin.initKeys("/social.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		orderingService = (OrderingService) ApplicationContext.getInstance()
				.getBean("orderingService");
		technologyService = (TechnologyService) ApplicationContext
				.getInstance().getBean("technologyService");
		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
		adminService = (AdminService) ApplicationContext.getInstance().getBean(
				"adminService");
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
		developerService = (DeveloperService) ApplicationContext.getInstance()
				.getBean("developerService");
		authenticationProvider = AuthenticationProvider
				.createAuthenticationProvider();
		feedbackService = (FeedbackService) ApplicationContext.getInstance()
				.getBean("feedbackService");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			switch (FrontController.getPath(request)) {
			case "user/email":
				sendResponse(response, userManager.isEmailAvailable(request
						.getParameter("email")), mapper);
				return;
			case "user/social":
				configSocials(request, response);
				return;
			case "user/signup/linkedin":
				sendResponse(response, getLinkedInProfile(request, response),
						mapper);
				return;
			case "user/signin/linkedin":
				signIn(request, response, SignInType.LINKEDIN);
				return;
			case "user/developers/filter":
				filterDevelopers(request, response);
				return;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
		}
	}

	private LinkedinProfile getLinkedInProfile(HttpServletRequest request,
			HttpServletResponse response)
	{
		String oauthVerifier = request.getParameter("verifier");
		try {
			linkedin.loadData(oauthVerifier);
			return linkedin.getProfile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void configSocials(HttpServletRequest request,
			HttpServletResponse response)
	{
		String callbackUrl = request.getParameter("callbackUrlLinkedIn");
		Map<String, Object> result = new HashMap<>();
		result.put("linkedinUrl", linkedin.getAuthentificationUrl(callbackUrl));
		sendResponse(response, result, mapper);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
			case "user/signin":
				signIn(request, response, SignInType.MANUAL);
				return;
			case "user/create":
				create(request, response);
				return;
			case "user/getById":
				getById(request, response);
				return;
			case "user/getTechById":
				getTechById(request, response);
				return;
			case "user/getContById":
				getContById(request, response);
				return;
			case "user/getPortById":
				getPortfolioById(request, response);
				return;
			case "user/getFeed":
				getFeedbackByIdForDev(request, response);
				return;
			case "user/send":
				send(request, response);
				return;
			case "user/comment":
				comment(request, response);
				return;
			case "user/sms":
				sendSmsAndFollowOrHire(request, response);
				return;
			case "user/isAuth":
				isAuth(request, response);
				return;
			case "user/logout":
				logout(request, response);
				return;
			case "user/orders/filter":
				filterOrders(request, response);
				break;
			case "user/orders/limits":
				sendResponse(response, orderingService.findPaymentLimits(),
						mapper);
				break;
			case "user/technologies":
				sendResponse(response, technologyService.findAll(), mapper);
				break;
			case "user/order":
				getOrderById(request, response);
				break;
			case "user/order/followers":
				getFollowersByOrderId(request, response);
				break;
			case "user/order/techs":
				getOrderTechs(request, response);
				break;
			case "user/order/subscribe":
				subscribe(request, response);
				break;
			case "user/order/unsubscribe":
				unsubscribe(request, response);
				break;
			case "user/type":
				getUserById(request, response);
				break;
			case "user/customer/history":
				getCustomerHistory(request, response);
				break;
			case "user/getAvailableCustOrders":
				getAvailableCustOrders(request, response);
				break;
			case "user/customer/feedbacks":
				getFeedbacksByIdForCust(request, response);
				break;
			case "user/contact":
				getUserContact(request, response);
				break;
			case "user/getTestByDevId":
				getTestByDevId(request, response);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	private void filterOrders(HttpServletRequest request,
			HttpServletResponse response)
	{
		try {
			JsonPaginator result = mapper.readValue(request.getReader()
					.readLine(), JsonPaginator.class);
			List<Ordering> orderings = orderingService.filterElements(result
					.getContent(), result.getPage().getStart()
					* result.getPage().getStep(), result.getPage().getStep());

			for (Ordering ordering : orderings) {
				ordering.setTechnologies(orderingService
						.findOrderingTechnologies(ordering.getId()));
			}

			paginator.next(result.getPage(), response, orderingService
					.getFilteredObjectNumber(result.getContent()), orderings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void filterDevelopers(HttpServletRequest request,
			HttpServletResponse response)
	{
		try {
			JsonPaginator result = mapper.readValue(request.getReader()
					.readLine(), JsonPaginator.class);
			List<Developer> developers = developerService.filterElements(result
					.getContent(), result.getPage().getStart()
					* result.getPage().getStep(), result.getPage().getStep());

			for (Developer developer : developers)
				developer.setTechnologies(developerService
						.getTechnologiesByDevId(developer.getId()));

			paginator.next(result.getPage(), response, orderingService
					.getFilteredObjectNumber(result.getContent()), developers);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "logout");
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
				"user");
		AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
				.getInstance().getBean("authenticationProvider");
		authenticationProvider.invalidateUserCookie(response,
				"freelancerRememberMeCookie", userEntity);
		if (userEntity != null) {
			request.getSession().invalidate();
		}
	}

	public void isAuth(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue != null) {
			sendResponse(response, ue, mapper);
		} else {
			response.sendError(500);
			return;
		}
	}

	public void sendSmsAndFollowOrHire(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String phone = request.getParameter("phone");
		String author = request.getParameter("author");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");

		if (ue == null || phone == null || author == null) {
			response.sendError(303);
			return;
		}
		String sms = null;

		if ("dev".equals(author)) {
			sms = "This freelancer, " + ue.getFname() + " " + ue.getLname()
					+ ", followed you. See details in your cabinet.";

		} else
			sms = "This customer, " + ue.getFname() + " " + ue.getLname()
					+ ", would like to hire you. See details in your cabinet.";

		String[] str = new SmsSender().sendSms(phone, sms, ue.getFname());

		Follower f = null;
		if ("customer".equals(author)) {
			f = developerService.createFollowing(request.getParameterMap());
		} else {
			f = customerService.hireDeveloper(request.getParameterMap());
		}

		if (f == null) {
			response.sendError(303);
		}
	}

	public void comment(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String author = request.getParameter("role");
		String rate = request.getParameter("rate");
		String dev_id = null;
		String cust_id = null;
		if (author.equals("customer"))
			dev_id = request.getParameter("id");
		else
			cust_id = request.getParameter("id");

		String comment = request.getParameter("comment");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(404);
			return;
		}

		if (comment == null || rate == null) {
			response.sendError(500);
			return;
		}

		if ("".equals(comment)) {
			response.sendError(500);
			return;
		}

		if (author.equals("customer"))
			cust_id = ue.getId().toString();
		else
			dev_id = ue.getId().toString();

		if ("".equals(dev_id) || "".equals(cust_id) || "".equals(comment)
				|| "".equals(rate) || "".equals(author))
		{
			response.sendError(500);
			return;
		}
		if (comment.contains("<")) {
			response.sendError(500);
			return;
		}
		FeedbackService feedbackService = (FeedbackService) ApplicationContext
				.getInstance().getBean("feedbackService");
		Map<String, String[]> map = new HashMap<>();
		map.put("dev_id", new String[] { dev_id });
		map.put("cust_id", new String[] { cust_id });
		map.put("comment", new String[] { comment });
		map.put("rate", new String[] { rate });
		map.put("author", new String[] { author });

		feedbackService.create(map);
	}

	public void send(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String email = request.getParameter("email");
		String message = request.getParameter("message");
		String subject = request.getParameter("subject");

		if (email == null || message == null) {
			response.sendError(304);
			return;
		}

		if ("undefined".equals(email) || "undefined".equals(message)) {
			response.sendError(304);
			return;
		}

		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");

		if (ue == null) {
			response.sendError(304);
			return;
		}

		String fromAdmin = "onlineshopjava@gmail.com";
		String fromAdminPass = "ForTestOnly";
		String[] to = new String[] { email };

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean res = false;
				try {
					res = SendMessageToEmail.sendFromGMail(fromAdmin,
							fromAdminPass, to, subject, message);
					if (!res) {
						response.sendError(304);
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void getById(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				Developer developer = ds.findById(id);

				if (developer != null) {
					developer.setPassword(null);
					sendResponse(response, developer, mapper);
				} else
					response.sendError(404);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
			return;
		}
	}

	public void getTechById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				List<Technology> list = ds.getTechnologiesByDevId(id);
				if (list != null)
					sendResponse(response, list, mapper);
				else
					response.sendError(404);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
			return;
		}
	}

	public void getContById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				Contact contact = ds.getContactByDevId(id);
				if (contact != null) {
					sendResponse(response, contact, mapper);
				} else {
					response.sendError(404);
				}

			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.setStatus(404);
		}
	}

	public void getPortfolioById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				OrderingService orderingService = (OrderingService) ApplicationContext
						.getInstance().getBean("orderingService");
				List<Ordering> orderings = ds.getDeveloperPortfolio(id);
				for (Ordering ordering : orderings) {
					ordering.setTechnologies(orderingService
							.findOrderingTechnologies(ordering.getId()));
				}
				sendResponse(response, orderings, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	public void getFeedbackByIdForDev(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				FeedbackService fs = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				List<Feedback> feedbacks = fs.findFeedbacksByDevIdForHim(id);
				CustomerService customerService = (CustomerService) ApplicationContext
						.getInstance().getBean("customerService");
				for (Feedback f : feedbacks) {
					Customer customer = customerService.findById(f
							.getCustomerId());
					customer.setPassword(null);
					customer.setSalt(null);
					f.setCustomer(customer);
				}
				Collections.reverse(feedbacks);
				sendResponse(response, feedbacks, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
		}
	}

	private void create(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		String requestData = request.getReader().readLine();
		Map<String, String> data = mapper.readValue(requestData,
				new TypeReference<Map<String, String>>() {
				});
		String role = data.get("role");
		if (role == null || role.isEmpty()) {
			response.sendError(HttpServletResponse.SC_MULTIPLE_CHOICES);
			return;
		}

		if (!userManager.isEmailAvailable(data.get("email"))) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		try {
			userManager
					.createUser(
							data.entrySet()
									.stream()
									.collect(
											Collectors.toMap(Map.Entry::getKey,
													e -> new String[] { e
															.getValue() })),
							role);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setStatus(200);
	}

	private void signIn(HttpServletRequest request,
			HttpServletResponse response, SignInType type)
			throws ServletException, IOException
	{
		boolean remember = "true".equals(request.getParameter("remember"));
		String email = null;
		switch (type) {
		case MANUAL:
			email = request.getParameter("email");
			break;
		case LINKEDIN:
			try {
				email = getLinkedInProfile(request, response).getEmailAddress();
			} catch (OAuthException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_CONFLICT);
			}
			break;
		case GOOGLE:
			break;
		}
		if (email == null || "".equals(email)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		UserEntity userEntity = userManager.findUserByEmail(email);
		if (userEntity == null)
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid credentials");

		String password = request.getParameter("password");
		switch (type) {
		case MANUAL:
			if (password == null
					|| !userManager.validCredentials(email, password,
							userEntity))
			{
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				return;
			}
		case LINKEDIN:
		case GOOGLE:
			request.getSession().setAttribute("user", userEntity);
			userEntity.setRole(getRole(userEntity));

			if (remember)
				authenticationProvider.loginAndRemember(response,
						"freelancerRememberMeCookie", userEntity);
			else
				authenticationProvider.invalidateUserCookie(response,
						"freelancerRememberMeCookie", userEntity);

			sendResponse(response, userEntity, mapper);
		}
	}

	private String getRole(UserEntity userEntity) {
		String result = null;
		if (userEntity instanceof Developer)
			result = "developer";
		if (userEntity instanceof Customer)
			result = "customer";
		if (userEntity instanceof Admin)
			result = "admin";
		return result;
	}

	private void getOrderById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer orderId = Integer.parseInt(param);
				Ordering order = orderingService.findById(orderId);
				sendResponse(response, order, mapper);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getFollowersByOrderId(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer orderId = Integer.parseInt(param);
				List<Follower> followers = orderingService
						.findOrderFollowers(orderId);
				followers.forEach(follower -> {
					follower.setDeveloper(developerService.findById(follower
							.getDevId()));
					follower.getDeveloper().setPassword(null);
					follower.getDeveloper().setSalt(null);
				});
				sendResponse(response, followers, mapper);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getOrderTechs(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Technology> technologies = orderingService
						.findOrderingTechnologies(id);
				sendResponse(response, technologies, mapper);

			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getCustomerHistory(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("custId");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Ordering> orders = customerService
						.getProjectsPublicHistory(id);
				for (Ordering ordering : orders) {
					ordering.setTechnologies(orderingService
							.findOrderingTechnologies(ordering.getId()));
				}
				sendResponse(response, orders, mapper);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void subscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String order_id = request.getParameter("orderId");
		String message = request.getParameter("message");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (order_id == null || order_id.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Integer orderId;
		try {
			orderId = Integer.parseInt(order_id);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Follower follower = developerService.subscribeOnProject(ue.getId(),
				orderId, message);
		follower.setDeveloper((Developer) ue);
		sendResponse(response, follower, mapper);

	}

	private void unsubscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String follower_id = request.getParameter("followerId");

		try {
			developerService.unsubscribeFromProject(Integer
					.parseInt(follower_id));
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

	}

	private void getUserById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String paramId = request.getParameter("id");
		String role = request.getParameter("role");

		if (paramId == null || "".equals(paramId) || !paramId.matches("[0-9]"))
		{
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		Integer id = Integer.parseInt(paramId);

		try {
			UserEntity userEntity = null;
			switch (role) {
			case "admin":
				userEntity = adminService.findById(id);
				break;
			case "dev":
				userEntity = developerService.findById(id);
				break;
			case "customer":
				userEntity = customerService.findById(id);
				break;
			}
			if (userEntity != null) {
				userEntity.setPassword(null);
				userEntity.setEmail(null);
				userEntity.setSalt(null);
				userEntity.setUuid(null);
				userEntity.setRegUrl(null);

				sendResponse(response, userEntity, mapper);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void getUserContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String paramId = request.getParameter("id");
		String role = request.getParameter("role");

		if (paramId == null || "".equals(paramId) || !paramId.matches("[0-9]"))
		{
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		Integer id = Integer.parseInt(paramId);

		try {
			Contact contact = null;
			switch (role) {
			case "dev":
				contact = developerService.getContactByDevId(id);
				break;
			case "customer":
				contact = customerService.getContactByCustomerId(id);
				break;
			}
			if (contact != null) {
				contact.setPhone(null);
				contact.setVersion(null);
				contact.setDeleted(null);

				sendResponse(response, contact, mapper);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	private void getFeedbacksByIdForCust(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Feedback> feedbacks = feedbackService
						.findFeedbacksByCustIdForHim(id);
				for (Feedback f : feedbacks) {
					f.setDeveloper(developerService.findById(f.getDevId()));
				}
				Collections.reverse(feedbacks);

				sendResponse(response, feedbacks, mapper);

			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	public void getAvailableCustOrders(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		String from = request.getParameter("from");

		if (ue == null) {
			response.setStatus(304);
			return;
		}

		if (!"dev".equals(from)) {
			OrderingService os = (OrderingService) ApplicationContext
					.getInstance().getBean("orderingService");
			List<Ordering> orderings = os.getAvailableCustOrders(ue.getId());

			sendResponse(response, orderings, mapper);
		} else {
			String custId = request.getParameter("id");
			OrderingService os = (OrderingService) ApplicationContext
					.getInstance().getBean("orderingService");
			List<Ordering> orderings = os.getAvailableCustOrders(Integer
					.parseInt(custId));

			sendResponse(response, orderings, mapper);
		}
	}

	public void getTestByDevId(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");

		try {
			Integer devId = Integer.parseInt(param);
			TestService ts = (TestService) ApplicationContext.getInstance()
					.getBean("testService");
			DeveloperQAService dQAs = (DeveloperQAService) ApplicationContext
					.getInstance().getBean("developerQAService");
			List<DeveloperQA> developerQAs = dQAs.findAllByDevId(devId);
			for (DeveloperQA developerQA : developerQAs) {
				developerQA.setTest(ts.findById(developerQA.getTestId()));
				if (developerQA.getTest().getPassScore() >= developerQA
						.getRate())
				{
					developerQA.setIsPassed(false);
				} else {
					developerQA.setIsPassed(true);
				}
			}
			TechnologyService technologyService = (TechnologyService) ApplicationContext
					.getInstance().getBean("technologyService");
			for (DeveloperQA developerQA : developerQAs) {
				developerQA.getTest().setTechnology(
						technologyService.findById(developerQA.getTestId()));
			}
			sendResponse(response, developerQAs, mapper);
		} catch (Exception e) {
			response.sendError(500);
			return;
		}
	}

}

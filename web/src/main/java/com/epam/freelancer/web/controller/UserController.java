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

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.service.FeedbackService;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Feedback;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.social.model.LinkedinProfile;
import com.epam.freelancer.web.util.Paginator;

public class UserController extends HttpServlet implements Responsable {
	public static final Logger LOG = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = -2356506023594947745L;
	private OrderingService orderingService;
	private CustomerService customerService;
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
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
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
			case "user/signup/social":
				configSocials(request, response);
				return;
			case "user/signup/linkedin":
				linkedinSignUp(request, response);
				return;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
		}
	}

	private void linkedinSignUp(HttpServletRequest request,
			HttpServletResponse response)
	{
		String oauthVerifier = request.getParameter("verifier");
		try {
			linkedin.loadData(oauthVerifier);
			LinkedinProfile linkedinProfile = linkedin.getProfile();

			sendResponse(response, linkedinProfile, mapper);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void configSocials(HttpServletRequest request,
			HttpServletResponse response)
	{
		String callbackUrl = request.getParameter("callbackUrl");
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
				signIn(request, response);
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
			case "user/getRate":
				getRate(request, response);
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
				sendSms(request, response);
				return;
			case "user/isAuth":
				isAuth(request, response);
				return;
			case "user/logout":
				logout(request, response);
				return;
			case "user/orders/getorderbyid":
				getOrderById(request, response);
				break;
			case "user/orders/getfollowersbyorderid":
				getFollowersByOrderId(request, response);
				break;
			case "user/orders/getcustomerbyid":
				getCustomerById(request, response);
				break;
			case "user/orders/filter":
				filterOrders(request, response);
				break;
			case "user/orders/limits":
				sendResponse(response, orderingService.findPaymentLimits(),
						mapper);
				break;
			case "user/orders/tech":
				sendResponse(response, technologyService.findAll(), mapper);
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

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		System.out.println("LOGOUT");
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
		// response.sendRedirect(request.getContextPath() + "/");
	}

	public void isAuth(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		System.out.println(ue + " session");
		if (ue != null) {
			sendResponse(response, ue, mapper);
		} else {
			response.sendError(500);
			return;
		}
	}

	public void sendSms(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String phone = request.getParameter("phone");
		String sms = request.getParameter("sms");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");

		if (sms == null || phone == null) {
			response.sendError(500);
			return;
		}

		if (ue == null || "".equals(sms) || "".equals(phone)) {
			response.sendError(500);
			return;
		}

		String[] str = new SmsSender().sendSms(phone, sms, ue.getFname());

		try {
			int res = Integer.parseInt(str[1]);
			if (res < 0) {
				response.sendError(500);
			}
		} catch (Exception e) {
			response.sendError(500);
		}
	}

	public void comment(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String rate = request.getParameter("rate");
		String dev_id = request.getParameter("id");
		String comment = request.getParameter("comment");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(404);
			return;
		}

		if (comment == null || rate == null || dev_id == null) {
			response.sendError(500);
			return;
		}

		if ("".equals(comment)) {
			response.sendError(500);
			return;
		}

		String cust_id = ue.getId().toString();
		String author = "customer";

		if ("".equals(dev_id) || "".equals(cust_id) || "".equals(comment)
				|| "".equals(rate) || "".equals(author))
		{
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
		String changeEmail = request.getParameter("changeEmail");

		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(500);
			return;
		}
		String from = ue.getEmail();
		if (changeEmail != null && !"".equals(changeEmail)
				&& !changeEmail.equals(from))
		{
			from = changeEmail;
		}
		String fromPass = ue.getPassword();

		if ("".equals(email) || "".equals(message)) {
			response.sendError(500);
		}

		String[] to = new String[] { email };
		String name = "Feedback from " + ue.getFname() + " " + ue.getLname();

		boolean bool = SendMessageToEmail.sendFromGMail(from, fromPass, to,
				name, message);
		if (!bool) {
			response.sendError(500);
		}
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
				if (orderings != null) {
					sendResponse(response, orderings, mapper);
				} else {
					response.sendError(500);
				}
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	public void getRate(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				FeedbackService fs = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				Integer avg = fs.getAvgRate(id);
				sendResponse(response, avg, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
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
				List<Feedback> feedbacks = fs.findFeedbacksByDevId(id);
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

	public void create(HttpServletRequest request, HttpServletResponse response)
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

	private boolean isAvailable(String email) {
		boolean result = false;
		if (((AdminService) ApplicationContext.getInstance().getBean(
				"adminService")).emailAvailable(email)
				&& ((DeveloperService) ApplicationContext.getInstance()
						.getBean("developerService")).emailAvailable(email)
				&& ((CustomerService) ApplicationContext.getInstance().getBean(
						"customerService")).emailAvailable(email))
		{
			result = true;
		}
		return result;
	}

	public void signIn(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		boolean remember = "true".equals(request.getParameter("remember"));
		String email = request.getParameter("username");
		String password = request.getParameter("password");

		System.out.println(email);
		System.out.println(password);
		System.out.println(remember);

		if (email == null || "".equals(email)) {
			response.sendError(404);
			return;
		}

		if (email == null || "".equals(email)) {
			response.sendError(404);
			return;
		}

		HttpSession session = request.getSession();
		ApplicationContext.getInstance().addBean("authenticationProvider",
				new AuthenticationProvider());
		AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
				.getInstance().getBean("authenticationProvider");

		DeveloperService ds = (DeveloperService) ApplicationContext
				.getInstance().getBean("developerService");
		Developer developer = ds.findByEmail(email);

		boolean authorized = false;

		if (developer != null) {
			if (ds.validCredentials(email, password, developer)) {
				session.setAttribute("user", developer);
				developer.setRole("developer");
				authorized = true;
				if (remember) {
					authenticationProvider.loginAndRemember(response,
							"freelancerRememberMeCookie", developer);
					sendResponse(response, developer, mapper);
					return;
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", developer);
					sendResponse(response, developer, mapper);
					return;
				}
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				response.flushBuffer();
				return;
			}
		}

		CustomerService cs = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
		Customer customer = cs.findByEmail(email);

		if (customer != null) {
			if (cs.validCredentials(email, password, customer)) {
				session.setAttribute("user", customer);
				customer.setRole("customer");
				authorized = true;
				if (remember) {
					authenticationProvider.loginAndRemember(response,
							"freelancerRememberMeCookie", customer);
					sendResponse(response, customer, mapper);
					return;
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", customer);
					sendResponse(response, customer, mapper);
					return;
				}
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				response.flushBuffer();
				return;
			}
		}

		AdminService as = (AdminService) ApplicationContext.getInstance()
				.getBean("adminService");
		Admin admin = as.findByEmail(email);

		if (admin != null) {
			if (as.validCredentials(email, password, admin)) {
				session.setAttribute("user", admin);
				admin.setRole("admin");
				authorized = true;
				if (remember) {
					authenticationProvider.loginAndRemember(response,
							"freelancerRememberMeCookie", admin);
					sendResponse(response, admin, mapper);
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", admin);
					sendResponse(response, admin, mapper);
				}
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				response.flushBuffer();
			}
		} else if (!authorized) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid credentials");
			response.flushBuffer();
			return;
		}
	}

	private void getOrderById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		Ordering order = orderingService.findById(orderId);
		sendResponse(response, order, mapper);
	}

	private void getFollowersByOrderId(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		List<Developer> developers = orderingService
				.findOrderFollowers(orderId);
		developers.forEach(dev -> dev.setPassword(null));
		sendResponse(response, developers, mapper);
	}

	private void getCustomerById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		Integer custId = Integer.parseInt(request.getParameter("custId"));
		Customer customer = customerService.findById(custId);
		customer.setPassword(null);
		sendResponse(response, customer, mapper);
	}
}

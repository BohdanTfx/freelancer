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
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
		developerService = (DeveloperService) ApplicationContext.getInstance()
				.getBean("developerService");
		authenticationProvider = AuthenticationProvider
				.createAuthenticationProvider();
		feedbackService = (FeedbackService) ApplicationContext
				.getInstance().getBean("feedbackService");
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
			case "user/orders/getorderbyid":
				getOrderById(request, response);
				break;
			case "user/orders/getfollowersbyorderid":
				getFollowersByOrderId(request, response);
				break;
			case "user/orders/getcustomerbyid":
				getCustomerById(request, response);
				break;
			case "user/orders/getcustomerfeedbacks":
				getFeedbacksByIdForCust(request, response);
				break;
			case "user/orders/getordertechs":
				getOrderTechs(request, response);
				break;
			case "user/orders/getcustomerhistory":
				getCustomerHistory(request, response);

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
		LOG.info(getClass().getSimpleName() + " - " + "logout");
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
				"user");
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
				Developer developer = developerService.findById(id);

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
				List<Technology> list = developerService.getTechnologiesByDevId(id);
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
				Contact contact = developerService.getContactByDevId(id);
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
				List<Ordering> orderings = developerService.getDeveloperPortfolio(id);
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

	public void getRate(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				Integer avg = feedbackService.getAvgRate(id);
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
				List<Feedback> feedbacks = feedbackService.findFeedbacksByDevIdForHim(id);
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

	public void signIn(HttpServletRequest request,
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
				response.sendError(500);
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
				response.sendError(500);
			}
		}
	}

	private void getCustomerById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("custId");
		if (param != null) {
			try {
				Integer custId = Integer.parseInt(param);
				Customer customer = customerService.findById(custId);
				customer.setPassword(null);
				customer.setSalt(null);
				sendResponse(response, customer, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	private void getFeedbacksByIdForCust(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("custId");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Feedback> feedbacks = feedbackService.findFeedbacksByCustIdForHim(id);

				for (Feedback feedback : feedbacks) {
					Developer developer = developerService.findById(feedback
							.getDevId());
					developer.setPassword(null);
					developer.setSalt(null);
					feedback.setDeveloper(developer);
				}

				sendResponse(response, feedbacks, mapper);

			} catch (Exception e) {
				response.sendError(500);
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
				response.sendError(500);
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
				response.sendError(500);
			}
		}
	}
}
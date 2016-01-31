package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.util.Paginator;
import com.google.gson.Gson;

public class UserController extends HttpServlet implements Responsable {
	public static final Logger LOG = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = -2356506023594947745L;
	private OrderingService orderingService;
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
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
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
			case "user/signin":
				signIn(request, response);
				return;
			case "user/create":
				create(request, response);
				return;
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

	public void create(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		String role = request.getParameter("role");
		if (role == null || role.isEmpty()) {
			response.sendRedirect("/chooserole");
			return;
		}
		String email = request.getParameter("email");
		if (!isAvailable(email)) {
			request.setAttribute("notAvailableEmail", true);
			request.setAttribute("role", role);
			request.getRequestDispatcher("/views/signup.jsp").forward(request,
					response);
			return;
		}

		if (!request.getParameter("password").equals(
				request.getParameter("password_confirmation")))
		{
			request.setAttribute("notEqualsPasswords", true);
			request.setAttribute("role", role);
			request.getRequestDispatcher("/views/signup.jsp").forward(request,
					response);
			return;
		}

		request.getParameterMap().put("uuid",
				new String[] { UUID.randomUUID().toString() });

		try {
			if (role.equals("developer")) {
				DeveloperService developerService = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				developerService.create(request.getParameterMap());
			} else if (role.equals("customer")) {
				CustomerService customerService = (CustomerService) ApplicationContext
						.getInstance().getBean("customerService");
				customerService.create(request.getParameterMap());
			}
		} catch (Exception e) {
			request.setAttribute("error_message", "Not correct data");
			request.setAttribute("role", role);
			request.getRequestDispatcher("/views/signup.jsp").forward(request,
					response);
			return;
		}
		request.setAttribute("confirm_email", true);
		response.sendRedirect("/signin");
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

		if (email == null || "".equals(email)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"important_parameter needed");
			response.flushBuffer();
			return;
		}

		if (email == null || "".equals(email)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"important_parameter needed");
			response.flushBuffer();
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
					sendResp(developer, response);
					return;
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", developer);
					sendResp(developer, response);
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
					sendResp(customer, response);
					return;
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", customer);
					sendResp(customer, response);
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
					sendResp(admin, response);
				} else {
					authenticationProvider.invalidateUserCookie(response,
							"freelancerRememberMeCookie", admin);
					sendResp(admin, response);
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

	private void sendResp(UserEntity ue, HttpServletResponse response)
			throws IOException
	{
		ue.setPassword(null);
		String json = new Gson().toJson(ue);
		sendResponse(response, json, mapper);
	}
}

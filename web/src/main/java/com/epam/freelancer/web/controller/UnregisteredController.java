package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.util.Paginator;

public class UnregisteredController extends HttpServlet {
	private final static Logger LOG = Logger
			.getLogger(UnregisteredController.class);
	private static final long serialVersionUID = 1L;
	private OrderingService orderingService;
	private UserManager userManager;
	private Linkedin linkedin;
	private Paginator paginator;

	public UnregisteredController() {
		init();
	}

	@Override
	public void init() {
		LOG.info(getClass().getSimpleName() + " - " + " loaded");
		linkedin = new Linkedin();

		paginator = new Paginator();
		try {
			linkedin.initKeys("/social.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		orderingService = (OrderingService) ApplicationContext.getInstance()
				.getBean("orderingService");

		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			switch (FrontController.getPath(request)) {
			case "signup":
				request.setAttribute("role", request.getParameter("role"));
				fillSignup(request, response);
				break;
			case "orders":
				request.setAttribute("orders", orderingService.findAll());
				break;
			case "language/bundle":
				sendBundle(request, response);
				return;
			case "logout":
				logout(request, response);
				return;
			default:
			}
			request.getRequestDispatcher(
					"/views/" + FrontController.getPath(request) + ".jsp")
					.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
		}
	}

	private void fillSignup(HttpServletRequest request,
			HttpServletResponse response)
	{
		request.setAttribute("linkedinurl",
				linkedin.getAuthentificationUrl("http://localhost:8081/signin"));
		// request.setAttribute(
		// "linkedinurl",
		// linkedin.getAuthentificationUrl(request.getRemoteHost()
		// + ":"
		// + request.get
		// + (request.getContextPath().isEmpty() ? "" : "/" + "/"
		// + request.getContextPath()) + "/signin"));
	}

	private void sendBundle(HttpServletRequest request,
			HttpServletResponse response)
	{
		LOG.info(getClass().getSimpleName() + " - " + "sendBundle");
		Locale locale = ((Locale) request.getSession().getAttribute("language"));
		ResourceBundle bundle = null;
		if (locale != null)
			bundle = ResourceBundle.getBundle("/i18n/language", locale);
		else
			bundle = ResourceBundle.getBundle("/i18n/language", Locale.ENGLISH);

		Map<String, String> bundleMap = new HashMap<>();

		for (String key : bundle.keySet()) {
			String value = bundle.getString(key);
			bundleMap.put(key, value);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(new ObjectMapper().writeValueAsString(bundleMap));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doPost");
		try {
			switch (FrontController.getPath(request)) {
			case "orders/filter":
				filterOrders(request, response);
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
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, String> content = mapper.readValue(
					request.getParameter("content"),
					new TypeReference<Map<String, String>>() {
					});
			Map<String, Integer> pagination = mapper.readValue(
					request.getParameter("page"),
					new TypeReference<Map<String, Integer>>() {
					});

			List<Ordering> orderings = orderingService.filterElements(content,
					pagination.get("start"), pagination.get("step"));
			paginator.next(pagination, orderings, response);
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
		userManager.modifyUser(userEntity);
		request.getSession().removeAttribute("user");
		response.sendRedirect(request.getContextPath() + "/");
	}

}
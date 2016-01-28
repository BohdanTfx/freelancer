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

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.util.Paginator;

public class UnregisteredController extends HttpServlet {
	private final static Logger LOG = Logger
			.getLogger(UnregisteredController.class);
	private static final long serialVersionUID = 1L;
	private OrderingService orderingService;
	private TechnologyService technologyService;
	private UserManager userManager;
	private Linkedin linkedin;
	private ObjectMapper mapper;
	private Paginator paginator;

	public UnregisteredController() {
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
			case "language/bundle":
				sendBundle(request, response);
				return;
			case "logout":
				logout(request, response);
				return;
			default:
			}
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
			case "orders/limits":
				sendLimits(response);
				break;
			case "orders/tech":
				sendTechnologies(response);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	private void sendTechnologies(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(mapper.writeValueAsString(technologyService.findAll()));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * private Technology findTechnology(Map<Integer, Technology> technologies,
	 * Integer id) { Technology technology = technologies.get(id); if
	 * (technology == null) technology = technologyService.findById(id);
	 * 
	 * technologies.put(id, technology); return technology; }
	 */

	private void sendLimits(HttpServletResponse response) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(mapper.writeValueAsString(orderingService
					.findPaymentLimits()));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
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

			paginator.next(result.getPage(), response,
					orderingService.getObjectAmount(), orderings);
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

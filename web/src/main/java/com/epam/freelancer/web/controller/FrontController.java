package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.util.Paginator;

public class FrontController extends HttpServlet {
	private final static Logger LOG = Logger.getLogger(FrontController.class);
	private static final long serialVersionUID = 1L;
	private Map<String, HttpServlet> controllers = new HashMap<>();
	private OrderingService orderingService;
	private UserManager userManager;
	private Linkedin linkedin;
	private Paginator paginator;

	public static String getPath(HttpServletRequest request) {
		return request.getRequestURI()
				.substring(request.getContextPath().length())
				.substring("/front/".length());
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		LOG.info(getClass().getSimpleName() + " - " + "front controller loaded");
		linkedin = new Linkedin();

		paginator = new Paginator("pagedItems", "start", "firstPage",
				"lastPage", "currentPage");
		try {
			linkedin.initKeys("/social.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		orderingService = (OrderingService) ApplicationContext.getInstance()
				.getBean("orderingService");

		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
		super.init(config);
		configControllers();
	}

	private void configControllers() {
		controllers.put("user/", new UserController());
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			if (request.getSession().isNew())
				configAutoAuthentication(request.getSession());

			String path = request.getRequestURI().substring(
					request.getContextPath().length());

			if (path.startsWith("/front/")) {
				path = path.substring("/front/".length());
				switch (path) {
				case "":
					path = "home";
					break;
				case "orders":
					fillOrdering(request, response);
					break;
				case "signup":
					request.setAttribute("role",request.getParameter("role"));
					fillSignup(request, response);
					break;
				case "language/bundle":
					sendBundle(request, response);
					return;
				case "logout":
					logout(request, response);
					return;
				default:
					if (path.startsWith("admin/")) {
						controllers.get("admin/").service(request, response);
						return;
					}
					if (path.startsWith("dev/")) {
						controllers.get("dev/").service(request, response);
						return;
					}
					if (path.startsWith("cust/")) {
						controllers.get("cust/").service(request, response);
						return;
					}
					if (path.startsWith("signup")) {
						request.setAttribute("role",
								request.getParameter("role"));
						request.getRequestDispatcher("/views/signup.jsp")
								.forward(request, response);
						return;
					}

				}
				request.getRequestDispatcher("/views/" + path + ".jsp")
						.forward(request, response);
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

	private void fillOrdering(HttpServletRequest request,
			HttpServletResponse response)
	{
		List<Ordering> orderings = orderingService.findAll();
		request.setAttribute("orders", orderings);
		paginator.next(request, orderings);

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

	private void configAutoAuthentication(HttpSession session) {
		/*
		 * LOG.info(getClass().getSimpleName() + " - " +
		 * "configAutoAuthentication"); EnvironmentVariablesManager manager =
		 * EnvironmentVariablesManager .getInstance();
		 * session.setAttribute(manager.getVar("session.dev.autoauth"), 1);
		 * session.setAttribute(manager.getVar("session.admin.autoauth"), 1);
		 * session.setAttribute(manager.getVar("session.cust.autoauth"), 1);
		 */
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doPost");
		try {
			String path = request.getRequestURI().substring(
					request.getContextPath().length());

			path = path.substring("/front/".length());
			if (path.startsWith("user/")) {
				controllers.get("user/").service(request, response);
				return;
			}
			controllers.get(path).service(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	@Override
	public void destroy() {
		LOG.info(getClass().getSimpleName() + " - " + "destroy");
		super.destroy();
		for (Entry<String, HttpServlet> controller : controllers.entrySet()) {
			controller.getValue().destroy();
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

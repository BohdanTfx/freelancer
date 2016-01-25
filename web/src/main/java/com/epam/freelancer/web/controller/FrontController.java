package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class FrontController extends HttpServlet {
	private final static Logger LOG = Logger.getLogger(FrontController.class);
	private static final long serialVersionUID = 1L;
	private Map<String, HttpServlet> controllers = new HashMap<>();

	public static String getPath(HttpServletRequest request) {
		return request.getRequestURI()
				.substring(request.getContextPath().length())
				.substring("/front/".length());
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		LOG.info(getClass().getSimpleName() + " - " + "front controller loaded");

		super.init(config);
		configControllers();
	}

	private void configControllers() {
		controllers.put("user/", new UserController());
		controllers.put("unreg/", new UnregisteredController());
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
				/*case "":
					path = "home";
					break;*/
				case "orders":
				case "signup":
				case "language/bundle":
				case "logout":
					controllers.get("unreg/").service(request, response);
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
				}
				/*request.getRequestDispatcher("/views/" + path + ".jsp")
						.forward(request, response);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
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
			if (request.getSession().isNew())
				configAutoAuthentication(request.getSession());

			String path = request.getRequestURI().substring(
					request.getContextPath().length());

			if (path.startsWith("/front/")) {
				path = path.substring("/front/".length());
				switch (path) {
				case "orders/filter":
					controllers.get("unreg/").service(request, response);
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
					if (path.startsWith("user/")) {
						controllers.get("user/").service(request, response);
						return;
					}
				}
			}
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
}

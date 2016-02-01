package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;

public class UnregisteredController extends HttpServlet {
	private final static Logger LOG = Logger
			.getLogger(UnregisteredController.class);
	private static final long serialVersionUID = 1L;
	private UserManager userManager;

	public UnregisteredController() {
		init();
	}

	@Override
	public void init() {
		LOG.info(getClass().getSimpleName() + " - " + " loaded");

		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			switch (FrontController.getPath(request)) {
			case "unreg/language/bundle":
				sendBundle(request, response);
				return;
			case "unreg/logout":
				logout(request, response);
				return;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
		}
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
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
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
		// response.sendRedirect(request.getContextPath() + "/");
	}

}

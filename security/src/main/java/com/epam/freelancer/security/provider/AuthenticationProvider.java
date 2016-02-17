package com.epam.freelancer.security.provider;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.UserService;
import com.epam.freelancer.business.util.CookieManager;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.UserEntity;

public class AuthenticationProvider {
	private CookieManager cookieManager;
	private String sessinUserName = "user";

	public static AuthenticationProvider createAuthenticationProvider() {
		AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
				.getInstance().getBean("authenticationProvider");
		if (authenticationProvider == null) {
			authenticationProvider = new AuthenticationProvider();
			ApplicationContext.getInstance().addBean("authenticationProvider",
					authenticationProvider);
		}

		return authenticationProvider;
	}

	public AuthenticationProvider() {
		cookieManager = (CookieManager) ApplicationContext.getInstance()
				.getBean("cookieManager");
	}

	public UserEntity isAutoAuthenticationEnable(String cookieName,
			UserManager userManager, HttpServletRequest request) throws IOException, ServletException
	{
		String uuid = cookieManager.getCookieValue(request, cookieName);

		if (uuid != null) {
			UserEntity user = userManager.findUserByUUID(uuid);
			if (user != null) 
				return user;
		}
		return null;
	}

	public boolean provideAccess(String cookieName, String type, String url,
			UserService<? extends UserEntity> service,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		UserEntity user = (UserEntity) request.getSession().getAttribute(
				sessinUserName);
		if (user != null) {
			if (checkUserEntityType(user, type))
				return true;
			else
				response.sendError(403);
			return false;
		} else {
			String uuid = cookieManager.getCookieValue(request, cookieName);

			if (uuid != null) {
				user = service.findByUUID(uuid);

				if (user != null) {
					request.getSession().setAttribute(sessinUserName, user);
					return true;
				} else {
					cookieManager.removeCookie(response, cookieName);
				}
			} else
				response.sendRedirect(request.getContextPath() + "/" + url);
		}

		return false;
	}

	private boolean checkUserEntityType(Object user, String type) {
		boolean result = false;
		switch (type) {
		case "developer":
			if (user instanceof Developer)
				result = true;
			break;
		case "admin":
			if (user instanceof Admin)
				result = true;
			break;
		case "customer":
			if (user instanceof Customer)
				result = true;
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

	public void loginAndRemember(HttpServletResponse response,
			String cookieName, UserEntity entity)
	{
		String uuid = UUID.randomUUID().toString();
		cookieManager.addCookie(response, cookieName, uuid, 60 * 60 * 24 * 30);
		entity.setUuid(uuid);
	}

	public void invalidateUserCookie(HttpServletResponse response,
			String cookieName, UserEntity entity)
	{
		cookieManager.removeCookie(response, cookieName);
		if (entity != null)
			entity.setUuid(null);
	}
}

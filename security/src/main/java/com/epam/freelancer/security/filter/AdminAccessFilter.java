package com.epam.freelancer.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.security.provider.AuthenticationProvider;

public class AdminAccessFilter implements Filter {
	private static final Logger LOG = Logger.getLogger(AdminAccessFilter.class);
	private AdminService adminService;
	private AuthenticationProvider authenticationProvider;
	private String cookieAutoAuthName = "freelancerRememberMeCookie";
	private String userName = "admin";

	public void init(FilterConfig config) throws ServletException {
		authenticationProvider = (AuthenticationProvider) ApplicationContext
				.getInstance().getBean("authenticationProvider");
		adminService = (AdminService) ApplicationContext.getInstance().getBean(
				"adminService");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doFilter");
//		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//		if (authenticationProvider.provideAccess(cookieAutoAuthName, userName,
//				"login", adminService, httpServletRequest, httpServletResponse))
			chain.doFilter(request, response);
	}

	public void destroy() {
	}

}

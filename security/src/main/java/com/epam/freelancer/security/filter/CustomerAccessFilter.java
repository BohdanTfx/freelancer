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
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.security.provider.AuthenticationProvider;

public class CustomerAccessFilter implements Filter {
	public static final Logger LOG = Logger
			.getLogger(CustomerAccessFilter.class);
	private CustomerService customerService;
	private AuthenticationProvider authenticationProvider;
	private String cookieAutoAuthName = "freelancerRememberMeCookie";
	private String userName = "customer";

	public void init(FilterConfig config) throws ServletException {
		authenticationProvider = AuthenticationProvider
				.createAuthenticationProvider();
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doFilter");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (authenticationProvider.provideAccess(cookieAutoAuthName, userName,
				customerService, httpServletRequest, httpServletResponse))
			chain.doFilter(request, response);
		else
			httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

	public void destroy() {
	}

}

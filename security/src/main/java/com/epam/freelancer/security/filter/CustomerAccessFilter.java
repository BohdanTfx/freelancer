package com.epam.freelancer.security.filter;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.util.EnvironmentVariablesManager;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerAccessFilter implements Filter {
	public static final Logger LOG = Logger
			.getLogger(CustomerAccessFilter.class);
	private CustomerService customerService;
	private AuthenticationProvider authenticationProvider;
	private String cookieAutoAuthName;
	private String userName;

	public void init(FilterConfig config) throws ServletException {
		/*authenticationProvider = (AuthenticationProvider) ApplicationContext.getInstance().getBean("authenticationProvider");
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
		EnvironmentVariablesManager manager = EnvironmentVariablesManager
				.getInstance();
		cookieAutoAuthName = manager.getVar("cookie.user.remember");
		userName = manager.getVar("user.customer");*/
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doFilter");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
/*
        if (authenticationProvider.provideAccess(cookieAutoAuthName, userName,
				"login", customerService, httpServletRequest,
				httpServletResponse))*/
        chain.doFilter(request, response);
	}

	public void destroy() {
	}

}

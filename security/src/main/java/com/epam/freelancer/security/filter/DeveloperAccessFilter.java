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
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.util.EnvironmentVariablesManager;
import com.epam.freelancer.security.provider.AuthenticationProvider;

public class DeveloperAccessFilter implements Filter {
	public static final Logger LOG = Logger
			.getLogger(DeveloperAccessFilter.class);
	private DeveloperService developerService;
	private AuthenticationProvider authenticationProvider;
	private String cookieAutoAuthName;
	private String userName;

	public void init(FilterConfig config) throws ServletException {
		authenticationProvider = new AuthenticationProvider();
		developerService = (DeveloperService) ApplicationContext.getInstance()
				.getBean("developerService");
		EnvironmentVariablesManager manager = EnvironmentVariablesManager
				.getInstance();
		cookieAutoAuthName = manager.getVar("cookie.user.remember");
		userName = manager.getVar("user.developer");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doFilter");
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

//		if (authenticationProvider.provideAccess(cookieAutoAuthName, userName,
//				"login", developerService, httpServletRequest,
//				httpServletResponse))
			chain.doFilter(request, response);
	}

	public void destroy() {
	}

}

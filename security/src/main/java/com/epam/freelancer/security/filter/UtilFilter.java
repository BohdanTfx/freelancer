package com.epam.freelancer.security.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UtilFilter implements Filter {
	private final static Logger LOG =
			Logger.getLogger(UtilFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws
			ServletException {

		System.out.println("Util filter init");


	}

	@Override
	public void doFilter(ServletRequest request,
						 ServletResponse response,
						 FilterChain chain) throws
			IOException, ServletException
	{
		LOG.info(getClass().getSimpleName() + " - " + "Filter");
				HttpServletRequest req =
						(HttpServletRequest) request;
		req.setCharacterEncoding("UTF-8");

		String path = req.getRequestURI
				().substring(
				req.getContextPath
						().length());
		if (path.endsWith(".jsp"))
			((HttpServletResponse)
					response).sendRedirect("/home");
		if (path.equals("/") || path.startsWith
				("/images/")
				|| path.startsWith
				("/front/") || path.startsWith("/app/")
				|| path.startsWith
				("/bower_components/") || path.startsWith("/target/"))
			chain.doFilter(request, response);
		else
			request.getRequestDispatcher
                    ("/front" + path).forward(request,
                    response);
	}

	@Override
	public void destroy() {

	}

}

package com.epam.freelancer.security.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DevPublicPageFilter implements Filter {

    public static final Logger LOG = Logger.getLogger(DevPublicPageFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOG.info("DevPublicPageFilter - Filter");
        String param = servletRequest.getParameter("id");

        if (param == null || "".equals(param)) {
            ((HttpServletResponse) servletResponse).sendRedirect("/");
            return;
        }

        try {
            Integer.parseInt(param);
        } catch (Exception e) {
            ((HttpServletResponse) servletResponse).sendRedirect("/");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

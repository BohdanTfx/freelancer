package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.AdminService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

public class UserController extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            switch (FrontController.getPath(request)) {
                case "user/email":
//                    checkEmail(request, response);
                    break;
                case "user/signin":
                    signIn(request, response);
                    return;
                case "user/create":
                    create(request, response);
                    return;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String role = request.getParameter("role");
        if (role == null || role.isEmpty()) {
            response.sendRedirect("/chooserole");
            return;
        }
        String email = request.getParameter("email");
        if (!isAvailable(email)) {
            request.setAttribute("notAvailableEmail", true);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }

        if (!request.getParameter("password").equals(request.getParameter("password_confirmation"))) {
            request.setAttribute("notEqualsPasswords", true);
            request.setAttribute("role", role);
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }

        request.getParameterMap().put("uuid", new String[]{UUID.randomUUID().toString()});

        try {
            if (role.equals("developer")) {
                DeveloperService developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                developerService.create(request.getParameterMap());
            } else if (role.equals("customer")) {
                CustomerService customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
                customerService.create(request.getParameterMap());
            }
        } catch (Exception e) {
            request.setAttribute("error_message", "Not correct data");
            request.setAttribute("role", role);
            request.getRequestDispatcher("/views/signup.jsp").forward(request, response);
            return;
        }
        request.setAttribute("confirm_email", true);
        response.sendRedirect("/signin");
    }

    private boolean isAvailable(String email) {
        boolean result = false;
        if (((AdminService) ApplicationContext.getInstance().getBean("adminService")).emailAvailable(email) &&
                ((DeveloperService) ApplicationContext.getInstance().getBean("developerService")).emailAvailable(email) &&
                ((CustomerService) ApplicationContext.getInstance().getBean("customerService")).emailAvailable(email)) {
            result = true;
        }
        return result;
    }

    public void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean remember = "on".equals(request.getParameter("remember"));

        if (email == null || "".equals(email)) {
            request.setAttribute("notCorrectData", "Invalid credentials");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            return;
        }
        
        if (password == null || "".equals(password)) {
            request.setAttribute("notCorrectData", "Invalid credentials");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        ApplicationContext.getInstance().addBean("authenticationProvider", new AuthenticationProvider());
        AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext.
                getInstance().getBean("authenticationProvider");

        DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
        Developer developer = ds.findByEmail(email);

        boolean authorized = false;

        if (developer != null) {
            if (ds.validCredentials(email, password, developer)) {
                session.setAttribute("user", developer);
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", developer);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", developer);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
                return;
            }
        }

        CustomerService cs = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
        Customer customer = cs.findByEmail(email);

        if (customer != null) {
            if (cs.validCredentials(email, password, customer)) {
                session.setAttribute("user", customer);
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", customer);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", customer);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
                return;
            }
        }

        AdminService as = (AdminService) ApplicationContext.getInstance().getBean("adminService");
        Admin admin = as.findByEmail(email);

        if (admin != null) {
            if (as.validCredentials(email, password, admin)) {
                session.setAttribute("user", admin);
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", admin);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", admin);
                    request.getRequestDispatcher("/views/home.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            }
        } else if (!authorized) {
            request.setAttribute("notCorrectData", "Invalid credentials");
            request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            return;
        }
    }
}

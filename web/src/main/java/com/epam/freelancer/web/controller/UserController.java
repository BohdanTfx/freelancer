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
	private static final long serialVersionUID = -2356506023594947745L;
	public static final Logger LOG = Logger.getLogger(UserController.class);


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
                    break;
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

    public void create(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");
        if (role == null || role.isEmpty()) {
            response.sendRedirect("/chooserole");
            return;
        }
        String email = request.getParameter("email");
        if (!isAvailable(email)) {
            request.setAttribute("role",role);
            request.setAttribute("notAvailableEmail", true);
            request.getRequestDispatcher("/views/signup.jsp").forward(request,response);
            return;
        }

        if(!request.getParameter("password").equals(request.getParameter("password_confirmation"))){
            request.setAttribute("role",role);
            request.setAttribute("notEqualsPasswords",true);
            request.getRequestDispatcher("/views/signup.jsp").forward(request,response);
            return;
        }

        request.getParameterMap().put("uuid", new String[]{UUID.randomUUID().toString()});

        if (role.equals("developer")) {
            DeveloperService developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
            developerService.create(request.getParameterMap());
        } else if (role.equals("customer")) {
            CustomerService customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
            customerService.create(request.getParameterMap());
        }
        request.setAttribute("confirm_email", true);
        request.getRequestDispatcher("/views/signin.jsp").forward(request, response);

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

        DeveloperService ds = new DeveloperService();
        Developer developer = ds.findByEmail(email);

        if (developer != null) {
            if (ds.validCredentials(password, developer)) {
                session.setAttribute("user", developer);
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", developer);
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", developer);
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
                return;
            }
        }

        CustomerService cs = new CustomerService();
        Customer customer = cs.findByEmail(email);

        if (customer != null) {
            if (cs.validCredentials(password, customer)) {
                session.setAttribute("user", customer);
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", customer);
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", customer);
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
                return;
            }
        }

        AdminService as = new AdminService();
        Admin admin = as.findByEmail(email);

        if (admin != null) {
            if (as.validCredentials(password, admin)) {
                session.setAttribute("user", admin);
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", admin);
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", admin);
                }
            } else {
                request.setAttribute("notCorrectData", "Invalid credentials");
                request.getRequestDispatcher("/views/signin.jsp").forward(request, response);
            }
        }
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
}

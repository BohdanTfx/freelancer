package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                case "user/getById":
                    getById(request, response);
                    return;
                case "user/getTechById":
                    getTechById(request, response);
                    return;
                case "user/getContById":
                    getContById(request, response);
                    return;
                case "user/getPortById":
                    getPortfolioById(request, response);
                    return;
                case "user/getRate":
                    getRate(request, response);
                    return;
                case "user/getFeed":
                    getFeedbackByIdForDev(request, response);
                    return;
                case "user/send":
                    send(request, response);
                    return;
                case "user/comment":
                    comment(request, response);
                    return;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    public void comment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rate = request.getParameter("rate");
        System.out.println("RATE " + rate);
        String dev_id = request.getParameter("id");
        String comment = request.getParameter("comment");
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue == null) {
            response.sendError(404);
            return;
        }
        String cust_id = ue.getId().toString();
        String author = "customer";

        if ("".equals(dev_id) || "".equals(cust_id) || "".equals(comment) || "".equals(rate) || "".equals(author)) {
            response.sendError(500);
            return;
        }

        FeedbackService feedbackService = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
        Map<String, String[]> map = new HashMap<>();
        map.put("dev_id", new String[]{dev_id});
        map.put("cust_id", new String[]{cust_id});
        map.put("comment", new String[]{comment});
        map.put("rate", new String[]{rate});
        map.put("author", new String[]{author});

        feedbackService.create(map);
    }

    public void send(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue == null) {
            response.sendError(500);
            return;
        }
        String from = ue.getEmail();
        String fromPass = ue.getPassword();

        if ("".equals(email) || "".equals(message) || ue == null) {
            response.sendError(500);
        }

        String[] to = new String[]{email};
        String name = "Feedback from " + ue.getFname() + " " + ue.getLname();


        SendMessageToEmail.sendFromGMail(from, fromPass, to, name, message);

        /*
        * String from = LOGIN;
        String pass = PASSWORD;
        String log = req.getParameter("email");
        String body = req.getParameter("mes");
        String[] to = {log};
        String name = "Feedback from " + req.getParameter("name") + " [" + log + "].";

        if(log == null || body == null)
            throw new RuntimeException();

        if("".equals(log) || "".equals(body))
            throw new RuntimeException();

        SendMessage.sendFromGMail(LOGIN, PASSWORD, new String[]{LOGIN}, name, body);
    }*/
    }

    public void getById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                Developer developer = ds.findById(id);

                if (developer != null) {
                    developer.setPassword(null);
                    sendResp(developer, response);
                }
                else
                    response.sendError(404);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
            return;
        }
    }

    public void getTechById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                List<Technology> list = ds.getTechnologiesByDevId(id);
                if (list != null)
                    sendListResp(list, response);
                else
                    response.sendError(404);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
            return;
        }
    }

    public void getContById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                Contact contact = ds.getContactByDevId(id);
                if (contact != null) {
                    sendResp(contact, response);
                } else {
                    response.sendError(404);
                }

            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.setStatus(404);
        }
    }

    public void getPortfolioById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                OrderingService orderingService = (OrderingService) ApplicationContext.getInstance().getBean("orderingService");
                List<Ordering> orderings = ds.getDeveloperPortfolio(id);
                for (Ordering ordering : orderings) {
                    ordering.setTechnologies(orderingService
                            .findOrderingTechnologies(ordering.getId()));
                }
                if (orderings != null) {
                    sendListResp(orderings, response);
                } else {
                    response.sendError(500);
                }
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    public void getRate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
                Integer avg = fs.getAvgRate(id);
                sendResp(avg, response);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
        }
    }

    public void getFeedbackByIdForDev(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
                List<Feedback> feedbacks = fs.findFeedbacksByDevId(id);
                CustomerService customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
                for (Feedback f : feedbacks) {
                    Customer customer = customerService.findById(f.getCustomerId());
                    customer.setPassword(null);
                    customer.setSalt(null);
                    f.setCustomer(customer);
                }
                sendListResp(feedbacks, response);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
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
        boolean remember = "true".equals(request.getParameter("remember"));
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        if (email == null || "".equals(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "important_parameter needed");
            response.flushBuffer();
            return;
        }

        if (email == null || "".equals(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "important_parameter needed");
            response.flushBuffer();
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
                developer.setRole("developer");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", developer);
                    sendResp(developer, response);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", developer);
                    sendResp(developer, response);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
                response.flushBuffer();
                return;
            }
        }

        CustomerService cs = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
        Customer customer = cs.findByEmail(email);

        if (customer != null) {
            if (cs.validCredentials(email, password, customer)) {
                session.setAttribute("user", customer);
                customer.setRole("customer");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", customer);
                    sendResp(customer, response);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", customer);
                    sendResp(customer, response);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
                response.flushBuffer();
                return;
            }
        }

        AdminService as = (AdminService) ApplicationContext.getInstance().getBean("adminService");
        Admin admin = as.findByEmail(email);

        if (admin != null) {
            if (as.validCredentials(email, password, admin)) {
                session.setAttribute("user", admin);
                admin.setRole("admin");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response, "freelancerRememberMeCookie", admin);
                    sendResp(admin, response);
                } else {
                    authenticationProvider.invalidateUserCookie(response, "freelancerRememberMeCookie", admin);
                    sendResp(admin, response);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
                response.flushBuffer();
            }
        } else if (!authorized) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
            response.flushBuffer();
            return;
        }
    }


    private void sendResp(Object ue, HttpServletResponse response) throws IOException {
        String json = new Gson().toJson(ue);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void sendListResp(List<?> list, HttpServletResponse response) throws IOException {
        String json = new Gson().toJson(list);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}

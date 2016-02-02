package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.util.Paginator;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class UserController extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private OrderingService orderingService;
    private CustomerService customerService;
    private TechnologyService technologyService;
    private UserManager userManager;
    private Linkedin linkedin;
    private ObjectMapper mapper;
    private Paginator paginator;
    public UserController() {
        init();
    }
    @Override
    public void init() {
        LOG.info(getClass().getSimpleName() + " - " + " loaded");
        linkedin = new Linkedin();
        mapper = new ObjectMapper();
        paginator = new Paginator();
        try {
            linkedin.initKeys("/social.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        orderingService = (OrderingService) ApplicationContext.getInstance()
                .getBean("orderingService");
        technologyService = (TechnologyService) ApplicationContext
                .getInstance().getBean("technologyService");
        userManager = (UserManager) ApplicationContext.getInstance().getBean(
                "userManager");
        customerService = (CustomerService) ApplicationContext.getInstance()
                .getBean("customerService");
    }


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
                case "user/sms":
                    sendSms(request, response);
                    return;
                case "user/isAuth":
                    isAuth(request, response);
                    return;
                case "user/logout":
                    logout(request, response);
                    return;
                case "user/orders/getorderbyid":
                    getOrderById(request, response);
                    break;
                case "user/orders/getfollowersbyorderid":
                    getFollowersByOrderId(request, response);
                    break;
                case "user/orders/getcustomerbyid":
                    getCustomerById(request, response);
                    break;
                case "user/orders/getcustomerfeedbacks":
                    getFeedbacksByIdForCust(request, response);
                    break;
                case "user/orders/getordertechs":
                    getOrderTechs(request, response);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("LOGOUT");
        LOG.info(getClass().getSimpleName() + " - " + "logout");
        UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
                "user");
        AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
                .getInstance().getBean("authenticationProvider");
        authenticationProvider.invalidateUserCookie(response,
                "freelancerRememberMeCookie", userEntity);
        if (userEntity != null) {
            request.getSession().invalidate();
        }
        // response.sendRedirect(request.getContextPath() + "/");
    }

    public void isAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        System.out.println(ue + " session");
        if (ue != null) {
            sendResp(ue, response);
        } else {
            response.sendError(500);
            return;
        }
    }

    public void sendSms(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phone = request.getParameter("phone");
        String sms = request.getParameter("sms");
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");

        if (sms == null || phone == null) {
            response.sendError(500);
            return;
        }

        if (ue == null || "".equals(sms) || "".equals(phone)) {
            response.sendError(500);
            return;
        }

        String[] str = new SmsSender().sendSms(phone, sms, ue.getFname());

        try {
            int res = Integer.parseInt(str[1]);
            if (res < 0) {
                response.sendError(500);
            }
        } catch (Exception e) {
            response.sendError(500);
        }
    }

    public void comment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rate = request.getParameter("rate");
        String dev_id = request.getParameter("id");
        String comment = request.getParameter("comment");
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue == null) {
            response.sendError(404);
            return;
        }

        if (comment == null || rate == null || dev_id == null) {
            response.sendError(500);
            return;
        }

        if ("".equals(comment)) {
            response.sendError(500);
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
        String changeEmail = request.getParameter("changeEmail");

        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue == null) {
            response.sendError(500);
            return;
        }
        String from = ue.getEmail();
        if (changeEmail != null && !"".equals(changeEmail) && !changeEmail.equals(from)) {
            from = changeEmail;
        }
        String fromPass = ue.getPassword();

        if ("".equals(email) || "".equals(message)) {
            response.sendError(500);
        }

        String[] to = new String[]{email};
        String name = "Feedback from " + ue.getFname() + " " + ue.getLname();


        boolean bool = SendMessageToEmail.sendFromGMail(from, fromPass, to, name, message);
        if (!bool) {
            response.sendError(500);
        }
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
                Collections.reverse(feedbacks);
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

        System.out.println(email);
        System.out.println(password);
        System.out.println(remember);

        if (email == null || "".equals(email)) {
            response.sendError(404);
            return;
        }

        if (email == null || "".equals(email)) {
            response.sendError(404);
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

    private void getOrderById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        Ordering order = orderingService.findById(orderId);
        sendResp(order, response);
    }

    private void getFollowersByOrderId(HttpServletRequest request, HttpServletResponse respons) throws IOException {
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        List<Developer> developers = orderingService.findOrderFollowers(orderId);
        developers.forEach(dev -> dev.setPassword(null));
        sendListResp(developers, respons);
    }

    private void getCustomerById(HttpServletRequest request, HttpServletResponse respons) throws IOException {
        Integer custId = Integer.parseInt(request.getParameter("custId"));
        Customer customer = customerService.findById(custId);
        customer.setPassword(null);
        sendResp(customer, respons);
    }

    private void getFeedbacksByIdForCust(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("custId");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
                List<Feedback> feedbacks = fs.findFeedbacksByCustId(id);
                sendListResp(feedbacks, response);

            }catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getOrderTechs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("orderId");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                List<Technology> technologies = orderingService.findOrderingTechnologies(id);
                sendListResp(technologies, response);
            }catch (Exception e) {
                response.sendError(500);
            }
        }
    }
}

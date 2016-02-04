package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.social.model.LinkedinProfile;
import com.epam.freelancer.web.util.Paginator;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private OrderingService orderingService;
    private CustomerService customerService;
    private DeveloperService developerService;
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
        developerService = (DeveloperService) ApplicationContext.getInstance()
                .getBean("developerService");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        LOG.info(getClass().getSimpleName() + " - " + "doGet");
        try {
            switch (FrontController.getPath(request)) {
                case "user/email":
                    sendResponse(response, userManager.isEmailAvailable(request
                            .getParameter("email")), mapper);
                    return;
                case "user/signup/social":
                    configSocials(request, response);
                    return;
                case "user/signup/linkedin":
                    linkedinSignUp(request, response);
                    return;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
        }
    }

    private void linkedinSignUp(HttpServletRequest request,
                                HttpServletResponse response) {
        String oauthVerifier = request.getParameter("verifier");
        try {
            linkedin.loadData(oauthVerifier);
            LinkedinProfile linkedinProfile = linkedin.getProfile();

            sendResponse(response, linkedinProfile, mapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configSocials(HttpServletRequest request,
                               HttpServletResponse response) {
        String callbackUrl = request.getParameter("callbackUrl");
        Map<String, Object> result = new HashMap<>();
        result.put("linkedinUrl", linkedin.getAuthentificationUrl(callbackUrl));
        sendResponse(response, result, mapper);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        try {
            switch (FrontController.getPath(request)) {
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
                case "user/orders/filter":
                    filterOrders(request, response);
                    break;
                case "user/orders/limits":
                    sendResponse(response, orderingService.findPaymentLimits(),
                            mapper);
                    break;
                case "user/orders/tech":
                    sendResponse(response, technologyService.findAll(), mapper);
                    break;
                case "user/orders/getorderbyid":
                    getOrderById(request, response);
                    break;
                case "user/orders/getfollowersbyorderid":
                    getFollowersByOrderId(request,
                            response);
                    break;
                case "user/orders/getcustomerbyid":
                    getCustomerById(request, response);
                    break;
                case "user/orders/getcustomerfeedbacks":
                    getFeedbacksByIdForCust(request,
                            response);
                    break;
                case "user/orders/getordertechs":
                    getOrderTechs(request, response);
                    break;
                case "user/orders/getcustomerhistory":
                    getCustomerHistory(request, response);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    private void filterOrders(HttpServletRequest request,
                              HttpServletResponse response) {
        try {
            JsonPaginator result = mapper.readValue(request.getReader()
                    .readLine(), JsonPaginator.class);
            List<Ordering> orderings = orderingService.filterElements(result
                    .getContent(), result.getPage().getStart()
                    * result.getPage().getStep(), result.getPage().getStep());

            for (Ordering ordering : orderings) {
                ordering.setTechnologies(orderingService
                        .findOrderingTechnologies(ordering.getId()));
            }

            paginator.next(result.getPage(), response, orderingService
                    .getFilteredObjectNumber(result.getContent()), orderings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
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

    public void isAuth(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue != null) {
            sendResponse(response, ue, mapper);
        } else {
            response.sendError(500);
            return;
        }
    }

    public void sendSms(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String phone = request.getParameter("phone");
        String author = request.getParameter("author");
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");

        if (ue == null || phone == null || author == null) {
            response.sendError(303);
            return;
        }
        String sms = null;

        if ("dev".equals(author)) {
            sms = "This freelancer, " + ue.getFname() + " " + ue.getLname() + ", followed you. See details in your cabinet.";

        } else
            sms = "This customer, " + ue.getFname() + " " + ue.getLname() + ", would like to hire you. See details in your cabinet.";

        String[] str = new SmsSender().sendSms(phone, sms, ue.getFname());

        Follower f = null;
        if ("customer".equals(author)) {
            f = developerService.createFollowing(request.getParameterMap());//wrong
        } else {
            f = customerService.hireDeveloper(request.getParameterMap());//ok
        }

        if (f == null) {
            response.sendError(303);
        }
    }

    public void comment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String author = request.getParameter("role");
        String rate = request.getParameter("rate");
        String dev_id = null;
        String cust_id = null;
        if (author.equals("customer"))
            dev_id = request.getParameter("id");
        else
            cust_id = request.getParameter("id");

        String comment = request.getParameter("comment");
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue == null) {
            response.sendError(404);
            return;
        }

        if (comment == null || rate == null) {
            response.sendError(500);
            return;
        }

        if ("".equals(comment)) {
            response.sendError(500);
            return;
        }

        if (author.equals("customer"))
            cust_id = ue.getId().toString();
        else
            dev_id = ue.getId().toString();

        if ("".equals(dev_id) || "".equals(cust_id) || "".equals(comment)
                || "".equals(rate) || "".equals(author)) {
            response.sendError(500);
            return;
        }
        if (comment.contains("<")) {
            response.sendError(500);
            return;
        }
        FeedbackService feedbackService = (FeedbackService) ApplicationContext
                .getInstance().getBean("feedbackService");
        Map<String, String[]> map = new HashMap<>();
        map.put("dev_id", new String[]{dev_id});
        map.put("cust_id", new String[]{cust_id});
        map.put("comment", new String[]{comment});
        map.put("rate", new String[]{rate});
        map.put("author", new String[]{author});

        feedbackService.create(map);
    }

    public void send(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        String subject = request.getParameter("subject");

        if (email == null || message == null) {
            response.sendError(304);
            return;
        }

        if ("undefined".equals(email) || "undefined".equals(message)) {
            response.sendError(304);
            return;
        }

        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");

        if (ue == null) {
            response.sendError(304);
            return;
        }

        String fromAdmin = "onlineshopjava@gmail.com";
        String fromAdminPass = "ForTestOnly";
        String[] to = new String[]{email};

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = false;
                try {
                    res = SendMessageToEmail.sendFromGMail(fromAdmin, fromAdminPass, to, subject, message);
                    if (!res) {
                        response.sendError(304);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getById(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext
                        .getInstance().getBean("developerService");
                Developer developer = ds.findById(id);

                if (developer != null) {
                    developer.setPassword(null);
                    sendResponse(response, developer, mapper);
                } else
                    response.sendError(404);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
            return;
        }
    }

    public void getTechById(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext
                        .getInstance().getBean("developerService");
                List<Technology> list = ds.getTechnologiesByDevId(id);
                if (list != null)
                    sendResponse(response, list, mapper);
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

    public void getContById(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext
                        .getInstance().getBean("developerService");
                Contact contact = ds.getContactByDevId(id);
                if (contact != null) {
                    sendResponse(response, contact, mapper);
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

    public void getPortfolioById(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                DeveloperService ds = (DeveloperService) ApplicationContext
                        .getInstance().getBean("developerService");
                OrderingService orderingService = (OrderingService) ApplicationContext
                        .getInstance().getBean("orderingService");
                List<Ordering> orderings = ds.getDeveloperPortfolio(id);
                for (Ordering ordering : orderings) {
                    ordering.setTechnologies(orderingService
                            .findOrderingTechnologies(ordering.getId()));
                }
                if (orderings != null) {
                    sendResponse(response, orderings, mapper);
                } else {
                    response.sendError(500);
                }
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    public void getRate(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService) ApplicationContext
                        .getInstance().getBean("feedbackService");
                Integer avg = fs.getAvgRate(id);
                sendResponse(response, avg, mapper);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
        }
    }

    public void getFeedbackByIdForDev(HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService) ApplicationContext
                        .getInstance().getBean("feedbackService");
                List<Feedback> feedbacks = fs.findFeedbacksByDevIdForHim(id);
                CustomerService customerService = (CustomerService) ApplicationContext
                        .getInstance().getBean("customerService");
                for (Feedback f : feedbacks) {
                    Customer customer = customerService.findById(f
                            .getCustomerId());
                    customer.setPassword(null);
                    customer.setSalt(null);
                    f.setCustomer(customer);
                }
                Collections.reverse(feedbacks);
                sendResponse(response, feedbacks, mapper);
            } catch (Exception e) {
                response.sendError(500);
            }
        } else {
            response.sendError(404);
        }
    }

    public void create(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String requestData = request.getReader().readLine();
        Map<String, String> data = mapper.readValue(requestData,
                new TypeReference<Map<String, String>>() {
                });
        String role = data.get("role");
        if (role == null || role.isEmpty()) {
            response.sendError(HttpServletResponse.SC_MULTIPLE_CHOICES);
            return;
        }

        if (!userManager.isEmailAvailable(data.get("email"))) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        try {
            userManager
                    .createUser(
                            data.entrySet()
                                    .stream()
                                    .collect(
                                            Collectors.toMap(Map.Entry::getKey,
                                                    e -> new String[]{e
                                                            .getValue()})),
                            role);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        response.setStatus(200);
    }

    private boolean isAvailable(String email) {
        boolean result = false;
        if (((AdminService) ApplicationContext.getInstance().getBean(
                "adminService")).emailAvailable(email)
                && ((DeveloperService) ApplicationContext.getInstance()
                .getBean("developerService")).emailAvailable(email)
                && ((CustomerService) ApplicationContext.getInstance().getBean(
                "customerService")).emailAvailable(email)) {
            result = true;
        }
        return result;
    }

    public void signIn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean remember = "true".equals(request.getParameter("remember"));
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        if (email == null || "".equals(email)) {
            response.sendError(404);
            return;
        }

        if (email == null || "".equals(email)) {
            response.sendError(404);
            return;
        }

        HttpSession session = request.getSession();
        ApplicationContext.getInstance().addBean("authenticationProvider",
                new AuthenticationProvider());
        AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
                .getInstance().getBean("authenticationProvider");

        DeveloperService ds = (DeveloperService) ApplicationContext
                .getInstance().getBean("developerService");
        Developer developer = ds.findByEmail(email);

        boolean authorized = false;

        if (developer != null) {
            if (ds.validCredentials(email, password, developer)) {
                session.setAttribute("user", developer);
                developer.setRole("developer");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response,
                            "freelancerRememberMeCookie", developer);
                    sendResponse(response, developer, mapper);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response,
                            "freelancerRememberMeCookie", developer);
                    sendResponse(response, developer, mapper);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid credentials");
                response.flushBuffer();
                return;
            }
        }

        CustomerService cs = (CustomerService) ApplicationContext.getInstance()
                .getBean("customerService");
        Customer customer = cs.findByEmail(email);

        if (customer != null) {
            if (cs.validCredentials(email, password, customer)) {
                session.setAttribute("user", customer);
                customer.setRole("customer");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response,
                            "freelancerRememberMeCookie", customer);
                    sendResponse(response, customer, mapper);
                    return;
                } else {
                    authenticationProvider.invalidateUserCookie(response,
                            "freelancerRememberMeCookie", customer);
                    sendResponse(response, customer, mapper);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid credentials");
                response.flushBuffer();
                return;
            }
        }

        AdminService as = (AdminService) ApplicationContext.getInstance()
                .getBean("adminService");
        Admin admin = as.findByEmail(email);

        if (admin != null) {
            if (as.validCredentials(email, password, admin)) {
                session.setAttribute("user", admin);
                admin.setRole("admin");
                authorized = true;
                if (remember) {
                    authenticationProvider.loginAndRemember(response,
                            "freelancerRememberMeCookie", admin);
                    sendResponse(response, admin, mapper);
                } else {
                    authenticationProvider.invalidateUserCookie(response,
                            "freelancerRememberMeCookie", admin);
                    sendResponse(response, admin, mapper);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid credentials");
                response.flushBuffer();
            }
        } else if (!authorized) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid credentials");
            response.flushBuffer();
            return;
        }
    }

    private void getOrderById(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        String param = request.getParameter("orderId");
        if (param != null) {
            try {
                Integer orderId = Integer.parseInt(param);
                Ordering order = orderingService.findById
                        (orderId);
                sendResponse(response, order, mapper);
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getFollowersByOrderId(HttpServletRequest
                                               request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("orderId");
        if (param != null) {
            try {
                Integer orderId = Integer.parseInt(param);
                List<Follower> followers =
                        orderingService.findOrderFollowers(orderId);
                followers.forEach(follower ->
                {
                    follower.setDeveloper(developerService.findById(follower.getDevId()));
                    follower.getDeveloper().setPassword(null);
                    follower.getDeveloper().setSalt(null);
                });
                sendResponse(response, followers, mapper);
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getCustomerById(HttpServletRequest
                                         request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("custId");
        if (param != null) {
            try {
                Integer custId = Integer.parseInt(param);
                Customer customer =
                        customerService.findById(custId);
                customer.setPassword(null);
                customer.setSalt(null);
                sendResponse(response, customer, mapper);
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getFeedbacksByIdForCust
            (HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String param = request.getParameter("custId");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                FeedbackService fs = (FeedbackService)
                        ApplicationContext.getInstance().getBean
                                ("feedbackService");
                List<Feedback> feedbacks =
                        fs.findFeedbacksByCustIdForHim(id);

                for (Feedback feedback : feedbacks) {
                    Developer developer =
                            developerService.findById(feedback.getDevId());
                    developer.setPassword(null);
                    developer.setSalt(null);
                    feedback.setDeveloper(developer);
                }

                sendResponse(response, feedbacks, mapper);


            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getOrderTechs(HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        String param = request.getParameter("orderId");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                List<Technology> technologies =
                        orderingService.findOrderingTechnologies(id);
                sendResponse(response, technologies, mapper);

            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void getCustomerHistory(HttpServletRequest
                                            request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("custId");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                List<Ordering> orders =
                        customerService.getProjectsPublicHistory(id);
                for (Ordering ordering : orders) {
                    ordering.setTechnologies
                            (orderingService
                                    .findOrderingTechnologies
                                            (ordering.getId()));
                }
                if (orders != null) {
                    sendResponse(response, orders, mapper);
                } else {
                    response.sendError(500);
                }
            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }
}

package com.epam.freelancer.web.controller;

import java.io.File;
import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.database.model.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.util.SmsSender;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.service.FeedbackService;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Feedback;
import com.epam.freelancer.database.model.UserEntity;

public class CustomerController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(CustomerController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private CustomerService customerService;
    private FeedbackService feedbackService;
    private OrderingService orderingService;
    private TestService testService;
    private DeveloperService developerService;
    private TechnologyService technologyService;
    private ObjectMapper mapper;
    private OrderCounterService orderCounterService;
    private UserManager userManager;

    public CustomerController() {
        testService = (TestService) ApplicationContext.getInstance().getBean(
                "testService");
        technologyService = (TechnologyService) ApplicationContext
                .getInstance().getBean("technologyService");
        customerService = (CustomerService) ApplicationContext.getInstance()
                .getBean("customerService");
        mapper = new ObjectMapper();
        developerService = (DeveloperService) ApplicationContext.getInstance()
                .getBean("developerService");
        feedbackService = (FeedbackService) ApplicationContext.getInstance()
                .getBean("feedbackService");
        userManager = (UserManager) ApplicationContext.getInstance().getBean(
                "userManager");
        orderingService = (OrderingService) ApplicationContext.getInstance()
                .getBean("orderingService");
        orderCounterService = (OrderCounterService) ApplicationContext.getInstance().getBean("orderCounterService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
                case "cust/getPersonalData":
                    fillCustomerPersonalPage(request, response);
                    break;
                case "cust/workersByIdOrder":
                    sendWorkersByIdOrder(request, response);
                    break;
                case "cust/dev/isWorker":
                    isWorker(request, response);
                    break;
                default:
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
        }
    }

private void createOrder(HttpServletRequest request,
        HttpServletResponse response) throws IOException
        {
        UserEntity customer = (UserEntity) request.getSession().getAttribute(
        "user");
        try {
        String requestData = request.getReader().readLine();
        Map<String, String> data = mapper.readValue(requestData,
        new TypeReference<Map<String, String>>() {
        });
        data.put("customer_id", customer.getId().toString());
        orderingService.create(data
        .entrySet()
        .stream()
        .collect(
        Collectors.toMap(Map.Entry::getKey,
        e -> new String[] { e.getValue() })));
        incrementOrderCreationStatiscts();
        } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return;
        }
        response.sendError(HttpServletResponse.SC_OK);
        }



    private void incrementOrderCreationStatiscts() {
        OrderCounter counter = orderCounterService.getOrderCounterByDate(
                new java.sql.Date(new java.util.Date().getTime()));
        if (counter != null) {
            orderCounterService.incrementCounter(counter.getId());
        } else {
            String[] count = {"1"};
            Map<String, String[]> map = new HashMap<>();
            map.put("count", count);
            orderCounterService.create(map);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            switch (FrontController.getPath(request)) {
                case "cust/getCustById":
                    getCustById(request, response);
                    break;
                case "cust/sendPersonalData":
                    updatePersonalData(request, response);
                    break;
                case "cust/getFeedForCust":
                     getFeedbacksByIdForCust(request, response);
                     break;
                case "cust/getContForCust":
                     getContForCust(request, response);
                    break;
                case "cust/history":
                    getCustomerHistory(request, response);
                    break;
                case "cust/order/create":
                    createOrder(request, response);
                    break;
                case "cust/changePassword":
                    changeCustomerPassword(request, response);
                    break;
                case "cust/confirmChangePasswordAndEmail":
                    confirmChangePasswordAndEmail(request, response);
                    break;
                case "cust/uploadImage":
                    uploadImage(request, response);
                    break;
                case "cust/allWorks":
                    fillCustomerMyWorksPage(request, response);
                    break;
                case "cust/dev/accept":
                    acceptDeveloper(request, response);
                    break;
                case "cust/changeEmail":
                    changeEmail(request, response);
                    break;
                case "cust/finishOrdering":
                    finishOrdering(request, response);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }


    public void getContForCust(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                Contact contact = customerService.getContactByCustomerId(id);

                if (contact != null) {
                    sendResponse(response, contact, mapper);
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

    public void getCustById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                CustomerService cs = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
                Customer customer = cs.findById(id);

                if (customer != null) {
                    customer.setPassword(null);
                    sendResponse(response, customer, mapper);
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

    public void getFeedbacksByIdForCust(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                List<Feedback> feedbacks = feedbackService
                        .findFeedbacksByCustIdForHim(id);
                for (Feedback f : feedbacks) {
                    f.setDeveloper(developerService.findById(f.getDevId()));
                }
                Collections.reverse(feedbacks);

                sendResponse(response, feedbacks, mapper);

            }catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    public void getRateForCust(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                List<Feedback> feedbacks = feedbackService
                        .findFeedbacksByCustId(id);
                Integer rate = 0;
                for (Feedback f : feedbacks) {
                    rate += f.getRate();
                }

                rate = rate / feedbacks.size();
                sendResponse(response, rate, mapper);

            } catch (Exception e) {
                response.sendError(500);
            }
        }
    }

    private void fillCustomerPersonalPage(HttpServletRequest request,
        HttpServletResponse response) throws IOException
        {
            Customer customer = (Customer) request.getSession().getAttribute("user");
            customer.setPassword(null);
            customer.setSalt(null);
            customer.setUuid(null);
            customer.setRegUrl(null);
            Map<String, Object> resultMap = new HashMap();
            resultMap.put("cust", customer);
            resultMap.put("contacts", customerService.getContactByCustomerId(customer.getId()));
            sendResponse(response, resultMap, mapper);
        }

    private void updatePersonalData(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        String paramCustomer = request.getParameter("customer");
        String paramContact = request.getParameter("contact");

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        mapper.setDateFormat(format);

        if (paramCustomer == null || "".equals(paramCustomer)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Customer customer = mapper.readValue(paramCustomer, new TypeReference<Customer>() {
        });
        Customer dbCustomer = customerService.findById(((UserEntity) request.getSession().getAttribute("user")).getId());
        customer.setPassword(dbCustomer.getPassword());
        customer.setSalt(dbCustomer.getSalt());
        customer.setUuid(dbCustomer.getUuid());
        customer.setRegUrl(dbCustomer.getRegUrl());
        customerService.modify(customer);
        if (paramContact == null || "".equals(paramContact)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Contact contact = mapper.readValue(paramContact, Contact.class);
        contact.setCustomId(customer.getId());
        Contact oldContact = customerService.getContactByCustomerId(customer.getId());
        if (oldContact == null) {
            customerService.createContact(contact);
        } else {
            contact.setId(oldContact.getId());
            customerService.updateContact(contact);
        }
        customer.setPassword(null);
        customer.setSalt(null);
        customer.setUuid(null);
        customer.setRegUrl(null);
        customer.setRole("customer");
        request.getSession().setAttribute("user", customer);
    }

    private void getCustomerHistory(HttpServletRequest
                                            request, HttpServletResponse response) throws IOException {
        //method for private history
    }

    private void changeCustomerPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        if (userManager.validCredentials(customer.getEmail(), password, customerService.findById(customer.getId()))) {
            generatePhoneCode(customer);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
        }
    }

    private void changeEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        if (customer != null) {
            if (userManager.isEmailAvailable(email) || customer.getEmail().equals(email)) {
                generatePhoneCode(customer);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user");
            return;
        }
    }

    private void confirmChangePasswordAndEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String confirmCode = request.getParameter("confirmCode");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        if (password != null) {
            if (checkConfirmCode(customer, confirmCode, response)) {
                customer.setPassword(password);
                customerService.encodePassword(customer);
                customerService.modify(customer);
            }
        }
        if (email != null) {
            if (checkConfirmCode(customer, confirmCode, response)) {
                customer = customerService.findById(customer.getId());
                customer.setSendEmail(email);
                customerService.modify(customer);
                customer.setPassword(null);
                customer.setSalt(null);
                customer.setUuid(null);
                customer.setRegUrl(null);
                customer.setRole("customer");
                session.setAttribute("user", customer);
            }
        }
    }

    private void uploadImage(HttpServletRequest request,
                             HttpServletResponse response){
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        String  imageJson = request.getParameter("image");
        byte[] encodImage = Base64.decodeBase64(imageJson);
        String fileName = customer.getFname() + customer.getLname();
        File file = null;
        try {
            file = new File("../target/WEB-INF/userData/" + fileName + ".jpg");
            FileUtils.writeByteArrayToFile(file, encodImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        customer.setImgUrl("target/WEB-INF/userData/" + fileName + ".jpg");
        customerService.modify(customer);
    }


    private void fillCustomerMyWorksPage(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
       List<Ordering> allWorks = orderingService.getAllCustOrders(user.getId());
       List<Ordering> finishedWorks = new ArrayList<>();
       List<Ordering> inProgressWorks = new ArrayList<>();
       List<Ordering> availableWorks = new ArrayList<>();
       Map<String,List<Ordering>> resultMap = new HashMap<>();

        allWorks.forEach(ordering ->{
            ordering.setTechnologies(technologyService.findTechnolodyByOrderingId(ordering.getId()));
            if((ordering.getStarted()!=null && ordering.getStarted()) && (ordering.getEnded()!=null && ordering.getEnded())){
                finishedWorks.add(ordering);
            }else{
                if(ordering.getStarted()!=null && ordering.getStarted()){
                    inProgressWorks.add(ordering);
                }else{
                    availableWorks.add(ordering);
                }
            }
        });
       resultMap.put("finishedWorks",finishedWorks);
       resultMap.put("inProgressWorks",inProgressWorks);
       resultMap.put("availableWorks",availableWorks);

       sendResponse(response,resultMap,mapper);
    }

    private void sendWorkersByIdOrder(HttpServletRequest request,
                                      HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        Map<String, Object> resultMap = new HashMap<>();

        List<Worker> allWorkersOfOrder = developerService.getAllWorkersByOrderId(orderId);
        List<Developer> acceptedDevelopers = new ArrayList<>();

        allWorkersOfOrder.forEach(worker ->{
            if(worker.getAccepted()){
                acceptedDevelopers.add(developerService.findById(worker.getDevId()));}
        });
        resultMap.put("workers", acceptedDevelopers);
        sendResponse(response, resultMap, mapper);
    }

    private void acceptDeveloper(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramCustomer = request.getParameter("customer");
        if (paramCustomer == null) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        Customer customer = mapper.readValue(paramCustomer, new TypeReference<Customer>() {
        });
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        if (ue.getId() != customer.getId()) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        String paramDevId = request.getParameter("devId");
        if (paramDevId == null || "".equals(paramDevId) || !paramDevId.matches("[0-9]*")) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        Integer devId = Integer.parseInt(paramDevId);
        Developer dev = developerService.findById(devId);
        String jobName = request.getParameter("jobName");
        String paramJobId = request.getParameter("jobId");
        if (paramJobId == null || "".equals(paramJobId) || !paramJobId.matches("[0-9]*")) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        Integer jobId = Integer.parseInt(paramJobId);
        addInWorker(dev, jobId);
        sendDevAcceptSms(jobName, customer, dev);
    }

    private void addInWorker(Developer dev, Integer orderId) {
        Worker worker = new Worker();
        worker.setDevId(dev.getId());
        worker.setNewHourly(dev.getHourly());
        worker.setOrderId(orderId);
        developerService.createWorker(worker);
    }

    private boolean sendDevAcceptSms(String jobName, Customer customer, Developer dev) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello! You are accepted on project \"" + jobName + "\" by ");
        stringBuilder.append(customer.getFname() + " " + customer.getLname() + "!");
        Contact contact = developerService.getContactByDevId(dev.getId());
        if (contact == null)
            return false;
        String phone = contact.getPhone();
        if (phone == null || "".equals(phone)) {
            return false;
        }
        String[] strs = new SmsSender().sendSms(phone, stringBuilder.toString(), customer.getFname());
        return true;
    }

    private void isWorker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramFollower = request.getParameter("follower");
        if (paramFollower == null || "".equals(paramFollower)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Follower follower = mapper.readValue(paramFollower, new TypeReference<Follower>() {
        });
        if (follower == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Worker worker = developerService.getWorkerByDevIdAndOrderId(follower.getDevId(), follower.getOrderId());
        Boolean isWorker = false;
        if(worker!=null){
            isWorker = true;
        }
        sendResponse(response,isWorker,mapper);
    }

    private void finishOrdering(HttpServletRequest request,HttpServletResponse response){
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        Ordering ordering =  orderingService.findById(orderId);
        ordering.setEnded(true);
        ordering.setEndedDate(new Timestamp(new java.util.Date().getTime()));
        orderingService.modify(ordering);

    }

    private String generatePhoneCode(Customer customer) {
        StringBuilder confirmPhoneCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
        }
        customer.setConfirmCode(confirmPhoneCode.toString());
        try {
            String phoneNumber = customerService.getContactByCustomerId(customer.getId()).getPhone();
            SmsSender smsSender = new SmsSender();
            smsSender.sendSms(phoneNumber, "Confirm code: " + confirmPhoneCode.toString(),
                    "e-freelance");
        } catch (NullPointerException e) {
            LOG.warn("The phone number is empty");
        }
        return confirmPhoneCode.toString();
    }

    private Boolean checkConfirmCode(Customer customer, String confirmCode, HttpServletResponse response) throws IOException {
        if (!customer.getConfirmCode().equals(confirmCode)){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid code");
            response.flushBuffer();
            return false;
        }
        return true;
    }
}

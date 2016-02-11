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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.util.SmsSender;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.epam.freelancer.business.context.ApplicationContext;
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
        orderingService = (OrderingService) ApplicationContext.getInstance()
                .getBean("orderingService");
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
        } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return;
        }
        response.sendError(HttpServletResponse.SC_OK);
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
                case "cust/changeCustPassword":
                    changeCustomerPassword(request, response);
                    break;
                case "cust/confirmChangePasswordAndEmail":
                    confirmChangePassword(request, response);
                    break;
                case "cust/uploadImage":
                    uploadImage(request, response);
                case "cust/allWorks":
                    fillCustomerMyWorksPage(request, response);
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
            HttpSession session = request.getSession();
            Customer customer = (Customer) session.getAttribute("user");
            Contact contact = customerService.getContactByCustomerId(customer.getId());

            String customerJson = new Gson().toJson(customer);
            String contactJson = new Gson().toJson(contact);
            String resultJson = "{\"cust\":" + customerJson + ",\"cont\":" + contactJson +"}";
            if(contactJson.length() == 0){
                resultJson = "{\"cust\":" + customerJson + "}";
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(resultJson);
        }

    private void updatePersonalData(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        Customer    customer = null;
        Contact     contact = null;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        mapper.setDateFormat(format);
        String customerJson = request.getParameter("customer");
        try {
            customer = mapper.readValue(customerJson, Customer.class);
        } catch(Exception e){
            LOG.warn("Some problem with mapper Customer Controller");
        }
        String contactJson = request.getParameter("contact");
        if(contactJson.length() != 0 ) {
            contact = mapper.readValue(contactJson, Contact.class);
        }
        if(customerService.getContactByCustomerId(customer.getId()) == null){
            contact.setCustomId(customer.getId());
            customerService.createContact(contact);
        } else {
            contact.setCustomId(customer.getId());
            customerService.updateContact(contact);
        }
        customerService.modify(customer);
    }

    private void getCustomerHistory(HttpServletRequest
                                            request, HttpServletResponse response) throws IOException {
        //method for private history
    }

    private void changeCustomerPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");
        StringBuilder confirmPhoneCode =  new StringBuilder();
        UserManager userManager = new UserManager();

        if (customer != null) {
            if(userManager.validCredentials(customer.getEmail(), password, customer)){
                SecureRandom random = new SecureRandom();
                for(int i = 0; i < 4; i++){
                    confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
                }
                customer.setConfirmCode(confirmPhoneCode.toString());
                try{
                    String phoneNumber = customerService.getContactByCustomerId(customer.getId()).getPhone();
                    SmsSender smsSender = new SmsSender();
                    smsSender.sendSms(phoneNumber, "Confirm code: " + confirmPhoneCode.toString(),
                            "e-freelance");
                } catch(NullPointerException e){
                    LOG.warn("The phone number is empty");
                }
                String confirmCodeJson = new Gson().toJson(confirmPhoneCode);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(confirmCodeJson);
            } else {
                System.out.println("Invalid password");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid credentials");
                response.flushBuffer();
                return;
            }
        }
    }

    private void confirmChangePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String confirmCode = request.getParameter("confirmCode");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("user");

        if(customer.getConfirmCode().equals(confirmCode)){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson("good"));
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid credentials");
            response.flushBuffer();
            return;
        }

        customer.setPassword(password);
        customerService.encodePassword(customer);
        customerService.modify(customer);
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
        } catch(Exception e){
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


}

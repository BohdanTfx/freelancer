package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Feedback;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by Максим on 22.01.2016.
 */
public class CustomerController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(CustomerController.class);
    private static final long serialVersionUID = -2356506023594947745L;

    private TestService testService;
    private TechnologyService technologyService;
    private ObjectMapper mapper;

    public CustomerController() {
        mapper = new ObjectMapper();
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
                case "cust/personal":
                    fillCustomerPersonalPage(request, response);
                    break;
                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            switch (FrontController.getPath(request)) {
                case "cust/getCustById":
                    getCustById(request, response);
                    break;
                case "cust/sendpersonaldata":
                    updatePersonalData(request, response);
                    break;
                case "cust/getFeedForCust":
                    getFeedbacksByIdForCust(request, response);
                    break;
                case "cust/getContForCust":
                    getContForCust(request, response);
                    break;
                case "cust/getRateForCust":
                    getRateForCust(request, response);
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
                CustomerService cs = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
                Contact contact = cs.getContactByCustomerId(id);

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
                FeedbackService fs = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
                List<Feedback> feedbacks = fs.findFeedbacksByCustIdForHim(id);
                DeveloperService ds = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
                for (Feedback f : feedbacks) {
                    f.setDeveloper(ds.findById(f.getDevId()));
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
                FeedbackService fs = (FeedbackService) ApplicationContext.getInstance().getBean("feedbackService");
                List<Feedback> feedbacks = fs.findFeedbacksByCustId(id);
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

    private void fillCustomerPersonalPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Customer            customer;
        Contact             contact;
        HttpSession         session;
        CustomerService     customerService;
        String              customerJson;
        String              contactJson;
        String              resultJson;

        customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");

        session = request.getSession();
        session.setAttribute("user", customerService.findById(1));

        customer = (Customer) session.getAttribute("user");
        contact = customerService.getContactByCustomerId(customer.getId());

        customerJson    = new Gson().toJson(customer);
        System.out.println(customerJson);
        contactJson     = new Gson().toJson(contact);
        System.out.println(contactJson);

        resultJson = "{\"cust\":" + customerJson + ",\"cont\":" + contactJson +"}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
    }

    private void updatePersonalData(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Customer    customer = null;
        Contact     contact;
        String      customerJson;
        String      contactJson;

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        mapper.setDateFormat(format);

        customerJson = request.getParameter("customer");
        System.out.println(customerJson);

        try {
            customer = mapper.readValue(customerJson, Customer.class);

        } catch(Exception e){
            LOG.warn("Some problem with mapper Customer Controller");
        }

        System.out.println("AFTER MAPPER +++++\n" + customer);

        contactJson = request.getParameter("contact");
        System.out.println(contactJson);

        contact = mapper.readValue(contactJson, Contact.class);
        System.out.println(contact);
    }
}

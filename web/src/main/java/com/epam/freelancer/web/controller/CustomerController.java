package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Feedback;
import com.epam.freelancer.database.model.UserEntity;
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
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
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
                case "cust/sendpersonaldata":
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
        Customer customer;
        Contact contact;
        HttpSession session;
        String customerJson;
        String contactJson;
        String resultJson;

        session = request.getSession();
        session.setAttribute("user", customerService.findById(1));

        customer = (Customer) session.getAttribute("user");
        contact = customerService.getContactByCustomerId(customer.getId());

        customerJson = new Gson().toJson(customer);
        System.out.println(customerJson);
        contactJson = new Gson().toJson(contact);
        System.out.println(contactJson);

        resultJson = "{\"cust\":" + customerJson + ",\"cont\":" + contactJson
        + "}";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
        }

    private void updatePersonalData(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException
    {
        Customer customer = null;
        Contact contact;
        String customerJson;
        String contactJson;

        SimpleDateFormat format = new SimpleDateFormat(
                "MMM dd, yyyy hh:mm:ss a");
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



    private void getCustomerHistory(HttpServletRequest
                                            request, HttpServletResponse response) throws IOException {
        //method for private history
    }
}

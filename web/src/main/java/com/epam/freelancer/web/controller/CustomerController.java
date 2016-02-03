package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.FeedbackService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Feedback;

/**
 * Created by Максим on 22.01.2016.
 */
public class CustomerController extends HttpServlet implements Responsable {
	public static final Logger LOG = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = -2356506023594947745L;
	private CustomerService customerService;
	private TestService testService;
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
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {

			String path = FrontController.getPath(request);

			switch (path) {
			/*
			 * case "cus/getalltests": fillTestPage(request, response); break;
			 */
			default:

			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
			case "cust/getCustById":
				getCustById(request, response);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	public void getCustById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				Customer customer = customerService.findById(id);

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

	public void getFeedbacksByIdForCust(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				FeedbackService fs = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				List<Feedback> feedbacks = fs.findFeedbacksByCustId(id);
				sendResponse(response, feedbacks, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}
}

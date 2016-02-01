package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.FeedbackService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Feedback;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Максим on 22.01.2016.
 */
public class CustomerController extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;

    private TestService testService;
    private TechnologyService technologyService;

    public CustomerController() {
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
                /*case "cus/getalltests":
                    fillTestPage(request, response);
                    break;*/
                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    public void getCustById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String param = request.getParameter("id");
        if (param != null) {
            try {
                Integer id = Integer.parseInt(param);
                CustomerService cs = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
                Customer customer = cs.findById(id);

                if (customer != null) {
                    customer.setPassword(null);
                    sendResp(customer, response);
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
                List<Feedback> feedbacks = fs.findFeedbacksByCustId(id);
                sendListResp(feedbacks, response);

            }catch (Exception e) {
                response.sendError(500);
            }
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

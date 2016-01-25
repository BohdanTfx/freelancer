package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.DeveloperQAService;
import com.epam.freelancer.business.service.GenericService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.database.dao.GenericDao;
import com.epam.freelancer.database.dao.TechnologyDao;
import com.epam.freelancer.database.dao.TestDao;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.DeveloperQA;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Test;
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

/**
 * Created by Максим on 22.01.2016.
 */
public class DeveloperController extends HttpServlet {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;

    private TestService testService;
    private TechnologyService technologyService;

    public DeveloperController() {
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

                switch (path) {
                case "dev/test":
                    fillTestPage(request,response);
                    break;
                default:

            }
            request.getRequestDispatcher("/views/" + path + ".jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void fillTestPage(HttpServletRequest request, HttpServletResponse response){
        DeveloperQAService developerQAService = (DeveloperQAService) ApplicationContext.getInstance().getBean("developerQAService");
        HttpSession session = request.getSession();
        Developer dev = (Developer) session.getAttribute("user");
        List<DeveloperQA> devQAs = developerQAService.findAllByDevId(dev.getId());

        List<Test> tests = testService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(),technology));
        Map<Integer, Test> testMap = new HashMap<>();
        tests.forEach(test -> {
            test.setTechnology(technologyMap.get(test.getTechId()));
            testMap.put(test.getId(),test);
        });
        for(DeveloperQA developerQA:devQAs){
            developerQA.setTest(testMap.get(developerQA.getTestId()));
        }
        request.setAttribute("devQAs", devQAs);
        request.setAttribute("tests", tests);
    }
}
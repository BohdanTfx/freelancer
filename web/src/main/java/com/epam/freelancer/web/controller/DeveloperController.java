package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.DeveloperQAService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.web.json.model.Quest;
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
    private ObjectMapper mapper;
    private DeveloperQAService developerQAService;

    public DeveloperController() {
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
        mapper = new ObjectMapper();
        developerQAService = (DeveloperQAService) ApplicationContext.getInstance().getBean("developerQAService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
                case "dev/getalltests":
                    fillTestPage(request, response);
                    break;
                case "dev/gettestbyid":
                    sendTestById(request, response);
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

            String path = FrontController.getPath(request);

            switch (path) {
                case "dev/getresults":
                    sendResults(request, response);
                    break;
                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    private void fillTestPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
//        UserEntity user = (UserEntity) session.getAttribute("user");
        List<DeveloperQA> devQAs = developerQAService.findAllByDevId(19);

        List<Test> tests = testService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(), technology));
        Map<Integer, Test> testMap = new HashMap<>();
        for(int i=0;i<tests.size();i++){
        }
        tests.forEach(test -> {
            test.setTechnology(technologyMap.get(test.getTechId()));
            testMap.put(test.getId(), test);
        });
        for (DeveloperQA developerQA : devQAs) {
            developerQA.setTest(testMap.get(developerQA.getTestId()));
        }
        for (int i = 0; i < tests.size(); i++) {
            for (DeveloperQA developerQA : devQAs) {
                if (developerQA.getTest().equals(tests.get(i)) && !developerQA.getIsExpire()) {
                    tests.remove(developerQA.getTest());
                    i--;
                    break;
                }
            }
        }
        String devQAsJson = new Gson().toJson(devQAs);
        String testsJson = new Gson().toJson(tests);
        String resultJson = "{\"devQAs\":" + devQAsJson + ",\"tests\":" + testsJson + "}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
    }

    private void sendTestById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int testId = Integer.parseInt(request.getParameter("test_id"));
        TestService testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        Test test = testService.findById(testId);
        test.setQuestions(
                testService.findQuestionsByTestId(testId));
        String testJson = new Gson().toJson(test);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(testJson);
    }

    private void sendResults(HttpServletRequest request, HttpServletResponse response) {

        try {

            String resultsJson = request.getParameter("results");
            List<Quest> results = mapper.readValue(resultsJson, new TypeReference<List<Quest>>() {
            });
            Map<Integer, List<Integer>> answersMap = new HashMap<>();
            results.forEach(quest -> answersMap.put(quest.getQuestionId(), quest.getAnswersId()));
            String questionsJson = request.getParameter("questions");
            List<Question> questions = mapper.readValue(questionsJson, new TypeReference<List<Question>>() {
            });
            double rate = 0;
            double maxRate = 0;
            int errors = 0;
            int success = 0;
            for (Question question : questions) {
                int points = 0;
                Integer questionId = question.getId();
                List<Integer> answersId = answersMap.get(questionId);
                List<Answer> answers = question.getAnswers();
                int qErrors = 0;
                for (Answer answer : answers) {
                    if (answer.getCorrect()) {
                        maxRate++;
                    }
                    if ((answer.getCorrect() && !answersId.contains(answer.getId())) ||
                            (!answer.getCorrect() && answersId.contains(answer.getId()))) {
                        points--;
                        qErrors++;
                    } else if (answer.getCorrect()) {
                        points++;
                    }
                }
                errors += points > 0 ? 0 : 1;
                success += qErrors == 0 ? 1 : 0;
                rate += points > 0 ? points : 0;
            }
            rate = 100 * rate / maxRate;

            //add in db results!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Map<String, String[]> map = new HashMap<>();
//            UserEntity user = (UserEntity) request.getSession().getAttribute("user");
//            Integer userId = user.getId();
            map.put("dev_id", new String[]{String.valueOf(19)});
            map.put("test_id", new String[]{request.getParameter("testId")});
            map.put("rate", new String[]{String.valueOf(rate)});
            map.put("expireDate", new String[]{request.getParameter("expireDate")});

            developerQAService.create(map);

            StringBuilder sb = new StringBuilder();
            sb.append("{\"rate\":");
            sb.append(rate);
            sb.append(",\"errors\":");
            sb.append(errors);
            sb.append(",\"success\":");
            sb.append(success);
            sb.append('}');
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

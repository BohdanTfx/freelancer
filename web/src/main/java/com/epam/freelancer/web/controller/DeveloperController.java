package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.web.json.model.Quest;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeveloperController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(UserController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private TestService testService;
    private TechnologyService technologyService;
    private ObjectMapper mapper;
    private DeveloperQAService developerQAService;
    private DeveloperService developerService;
    private OrderingService orderingService;
    private UserManager userManager;

    public DeveloperController() {
        testService = (TestService) ApplicationContext.getInstance().getBean(
                "testService");
        technologyService = (TechnologyService) ApplicationContext
                .getInstance().getBean("technologyService");
        mapper = new ObjectMapper();
        developerQAService = (DeveloperQAService) ApplicationContext
                .getInstance().getBean("developerQAService");
        developerService = (DeveloperService) ApplicationContext.getInstance()
                .getBean("developerService");
        userManager = (UserManager) ApplicationContext.getInstance().getBean(
                "userManager");
        orderingService = (OrderingService) ApplicationContext.getInstance().getBean("orderingService");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);
            switch (path) {
                case "dev/getalltests":
                    fillTestPage(request, response);
                    break;
                case "dev/gettestbyid":
                    sendTestById(request, response);
                    break;
                case "dev/works/all":
                    fillMyWorksPage(request, response);
                    break;
                case "dev/customerById":
                    sendCustomerById(request, response);
                    break;
                case "dev/workersByIdOrder":
                    sendWorkersByIdOrder(request, response);
                    break;
                case "dev/getPersonalData":
                    fillPersonalPage(request, response);
                    break;

                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
                case "dev/results":
                    sendResults(request, response);
                    break;
                case "dev/sendPersonalData":
                    updatePersonalData(request, response);
                    break;
                case "dev/changePassword":
                    changeDeveloperPassword(request, response);
                    break;
                case "dev/confirmChangePasswordAndEmail":
                    confirmChangePasswordAndEmail(request, response);
                    break;
                case "dev/changeEmail":
                    changeEmail(request, response);
                    break;
                case "dev/acceptOrdering":
                    acceptOrdering(request, response);
                    break;
                case "dev/rejectOrdering":
                    rejectOrdering(request, response);
                    break;
                default:
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }

    private void fillMyWorksPage(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");

        List<Worker> allWorks = developerService.getAllWorkersByDevId(user.getId());
        List<Ordering> subscribedWorks = developerService.getDeveloperSubscribedProjects(user.getId());
        List<Ordering> finishedWorks = new ArrayList<>();
        List<Ordering> worksInProgress = new ArrayList<>();
        List<Ordering> notAcceptedWorks = new ArrayList<>();

        allWorks.forEach(worker -> {
            Ordering order = orderingService.findById(worker.getOrderId());
            order.setTechnologies(technologyService.findTechnolodyByOrderingId(order.getId()));
            if (worker.getAccepted()!=null && worker.getAccepted()) {
                if (order.getStarted() && order.getEnded()) {
                    finishedWorks.add(order);
                } else {
                    worksInProgress.add(order);
                }
            } else {
                notAcceptedWorks.add(order);
            }
        });

        subscribedWorks.forEach(order ->{
            order.setTechnologies(technologyService.findTechnolodyByOrderingId(order.getId()));
        });



        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("finishedWorks", finishedWorks);
        resultMap.put("subscribedWorks", subscribedWorks);
        resultMap.put("processedWorks", worksInProgress);
        resultMap.put("notAcceptedWorks", notAcceptedWorks);
        sendResponse(response, resultMap, mapper);
    }

    private void fillTestPage(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        List<DeveloperQA> devQAs = developerQAService.findAllByDevId(user
                .getId());

        List<Test> tests = testService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(),
                technology));
        tests.forEach(test -> test.setTechnology(technologyMap.get(test.getTechId())));
        for (DeveloperQA developerQA : devQAs) {
            Test devQATest = testService.findById(developerQA.getTestId());
            devQATest.setTechnology(technologyMap.get(devQATest.getTechId()));
            developerQA.setTest(devQATest);
        }

        String devQAsJson = new Gson().toJson(devQAs);
        String testsJson = new Gson().toJson(tests);
        String resultJson = "{\"devQAs\":" + devQAsJson + ",\"tests\":"
                + testsJson + "}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
    }

    private void sendTestById(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        int testId = Integer.parseInt(request.getParameter("test_id"));
        TestService testService = (TestService) ApplicationContext
                .getInstance().getBean("testService");
        Test test = testService.findById(testId);
        test.setQuestions(testService.findQuestionsByTestId(testId));
        String testJson = new Gson().toJson(test);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(testJson);
    }

    private void sendCustomerById(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        Integer custId = Integer.parseInt(request.getParameter("customer_id"));
        CustomerService customerService = (CustomerService) ApplicationContext
                .getInstance().getBean("customerService");
        Customer cust = customerService.findById(custId);
        Map<String, Customer> resultMap = new HashMap<>();
        resultMap.put("cust", cust);

        sendResponse(response, resultMap, mapper);

    }

    private void sendWorkersByIdOrder(HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        Map<String, Object> resultMap = new HashMap<>();

        List<Worker> allWorkersOfOrder = developerService.getAllWorkersByOrderId(orderId);
        List<Developer> acceptedDevelopers = new ArrayList<>();

        allWorkersOfOrder.forEach(worker -> {
            if (worker.getAccepted()) {
                acceptedDevelopers.add(developerService.findById(worker.getDevId()));
            }
        });
        Worker worker = developerService.getWorkerByDevIdAndOrderId(
                user.getId(), orderId);
        resultMap.put("workerInfo", worker);
        resultMap.put("workers", acceptedDevelopers);

        sendResponse(response, resultMap, mapper);
    }

    private void sendResults(HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            String resultsJson = request.getParameter("results");
            List<Quest> results = mapper.readValue(resultsJson,
                    new TypeReference<List<Quest>>() {
                    });
            Map<Integer, List<Integer>> answersMap = new HashMap<>();
            results.forEach(quest -> answersMap.put(quest.getQuestionId(),
                    quest.getAnswersId()));
            String questionsJson = request.getParameter("questions");
            List<Question> questions = mapper.readValue(questionsJson,
                    new TypeReference<List<Question>>() {
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
                    if ((answer.getCorrect() && !answersId.contains(answer
                            .getId()))
                            || (!answer.getCorrect() && answersId
                            .contains(answer.getId()))) {
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
            // add in db results!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Map<String, String[]> map = createMapForDevQA(request, rate);
            developerQAService.create(map);
            // send result on frontend
            sendResultResponse(response, rate, errors, success);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String[]> createMapForDevQA(HttpServletRequest request,
                                                    double rate) {
        Map<String, String[]> map = new HashMap<>();
        UserEntity user = (UserEntity) request.getSession()
                .getAttribute("user");
        Integer userId = user.getId();
        map.put("dev_id", new String[]{String.valueOf(userId)});
        map.put("test_id", new String[]{request.getParameter("testId")});
        map.put("rate", new String[]{String.valueOf(rate)});
        map.put("expireDate",
                new String[]{request.getParameter("expireDate")});
        return map;
    }

    private void sendResultResponse(HttpServletResponse response, double rate,
                                    int errors, int success) throws IOException {
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
    }

    private void fillPersonalPage(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        Developer developer = (Developer) request.getSession().getAttribute("user");
        developer.setPassword(null);
        developer.setSalt(null);
        developer.setUuid(null);
        developer.setRegUrl(null);
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("dev", developer);
        resultMap.put("techs", developerService.getTechnologiesByDevId(developer.getId()));
        resultMap.put("contacts", developerService.getContactByDevId(developer.getId()));
        resultMap.put("allTechs", technologyService.findAll());
        sendResponse(response, resultMap, mapper);
    }

    private void updatePersonalData(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        String paramDev = request.getParameter("developer");
        String paramContact = request.getParameter("contact");

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        mapper.setDateFormat(format);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        if (paramDev == null || "".equals(paramDev)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Developer developer = mapper.readValue(paramDev, new TypeReference<Developer>() {
        });
        Developer dbDev = developerService.findById(((UserEntity) request.getSession().getAttribute("user")).getId());
        developer.setPassword(dbDev.getPassword());
        developer.setSalt(dbDev.getSalt());
        developer.setUuid(dbDev.getUuid());
        developer.setRegUrl(dbDev.getRegUrl());
        developerService.updateDeveloper(developer);
        if (paramContact == null || "".equals(paramContact)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Contact contact = mapper.readValue(paramContact, Contact.class);
        contact.setDevId(developer.getId());
        Contact oldContact = developerService.getContactByDevId(developer.getId());
        if (oldContact == null) {
            developerService.createContact(contact);
        } else {
            contact.setId(oldContact.getId());
            developerService.updateContact(contact);
        }
        updateDevTechnologies(request, response, developer.getId());
        developer.setPassword(null);
        developer.setSalt(null);
        developer.setUuid(null);
        developer.setRegUrl(null);
        developer.setRole("developer");
        request.getSession().setAttribute("user", developer);
    }

    private void updateDevTechnologies(HttpServletRequest request, HttpServletResponse response, Integer devId) throws IOException {
        String paramTechs = request.getParameter("technologies");
        if (paramTechs == null || "".equals(paramTechs)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        List<Integer> techIDs = mapper.readValue(paramTechs, new TypeReference<List<Integer>>() {
        });
        List<Technology> devTechs = developerService.getTechnologiesByDevId(devId);
        List<Integer> devTechIDs = new ArrayList<>();
        devTechs.forEach(devTech -> devTechIDs.add(devTech.getId()));
        for (int i = 0; i < techIDs.size(); i++) {
            Integer id = techIDs.get(i);
            if (devTechIDs.contains(id)) {
                techIDs.remove(id);
                devTechIDs.remove(id);
                i--;
            }
        }
        for (Integer devTechID : devTechIDs) developerService.deleteTechnologyInDev(devId, devTechID);
        for (Integer techID : techIDs) developerService.addTechnologyForDev(devId, techID);
    }


    private void changeDeveloperPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        Developer developer = (Developer) session.getAttribute("user");
        if (userManager.validCredentials(developer.getEmail(), password, developerService.findById(developer.getId()))) {
            generatePhoneCode(developer);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
        }
    }

    private void confirmChangePasswordAndEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String confirmCode = request.getParameter("confirmCode");
        HttpSession session = request.getSession();
        Developer developer = (Developer) session.getAttribute("user");
        if (password != null) {
            if (checkConfirmCode(developer, confirmCode, response)) {
                developer.setPassword(password);
                developerService.encodePassword(developer);
                developerService.updateDeveloper(developer);
            }
        }
        if (email != null) {
            if (checkConfirmCode(developer, confirmCode, response)) {
                developer = developerService.findById(developer.getId());
                developer.setSendEmail(email);
                developerService.updateDeveloper(developer);
                developer.setPassword(null);
                developer.setSalt(null);
                developer.setUuid(null);
                developer.setRegUrl(null);
                developer.setRole("developer");
                session.setAttribute("user", developer);
            }
        }
    }

    private void changeEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        HttpSession session = request.getSession();
        Developer developer = (Developer) session.getAttribute("user");
        if (developer != null) {
            if (userManager.isEmailAvailable(email) || developer.getEmail().equals(email)) {
                generatePhoneCode(developer);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user");
            return;
        }
    }

    private String generatePhoneCode(Developer developer) {
        StringBuilder confirmPhoneCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
        }
        developer.setConfirmCode(confirmPhoneCode.toString());
        try {
            String phoneNumber = developerService.getContactByDevId(developer.getId()).getPhone();
            SmsSender smsSender = new SmsSender();
            smsSender.sendSms(phoneNumber, "Confirm code: " + confirmPhoneCode.toString(),
                    "e-freelance");
        } catch (NullPointerException e) {
            LOG.warn("The phone number is empty");
        }
        return confirmPhoneCode.toString();
    }

    private Boolean checkConfirmCode(Developer developer, String confirmCode, HttpServletResponse response) throws IOException {
        if (!developer.getConfirmCode().equals(confirmCode)){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid code");
            response.flushBuffer();
            return false;
        }
        return true;
    }

    private void acceptOrdering(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Developer dev = (Developer) session.getAttribute("user");
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        Worker worker = developerService.getWorkerByDevIdAndOrderId(dev.getId(),orderId);
        worker.setAccepted(true);
        worker.setNewHourly(dev.getHourly());
        developerService.updateWorker(worker);

        Ordering ordering = orderingService.findById(orderId);
        ordering.setStarted(true);
        ordering.setStartedDate(new Timestamp(new java.util.Date().getTime()));
        orderingService.modify(ordering);

        orderingService.findOrderFollowers(orderId).forEach(follower -> {
           orderingService.deleteFollower(follower);
        });
    }

    private void rejectOrdering(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Developer dev = (Developer) session.getAttribute("user");
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        orderingService.findOrderFollowers(orderId).forEach(follower ->{
            if(follower.getDevId()==dev.getId()){
            orderingService.deleteFollower(follower);}
        });

        Worker worker = developerService.getWorkerByDevIdAndOrderId(dev.getId(),orderId);
        developerService.deleteWorker(worker);
    }
}
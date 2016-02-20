package com.epam.freelancer.web.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperQAService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.service.OrderingService;
import com.epam.freelancer.business.service.TechnologyService;
import com.epam.freelancer.business.service.TestService;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.Answer;
import com.epam.freelancer.database.model.Contact;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.DeveloperQA;
import com.epam.freelancer.database.model.Ordering;
import com.epam.freelancer.database.model.Question;
import com.epam.freelancer.database.model.Technology;
import com.epam.freelancer.database.model.Test;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.database.model.Worker;
import com.epam.freelancer.web.json.model.Quest;

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
        List<Long> expireDays = new ArrayList<>();
        List<Worker> workersToDelete = new ArrayList<>();

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
                LocalDate differExpireDate = LocalDate.ofEpochDay(
                    worker.getAcceptDate().toLocalDate().plusDays(5).toEpochDay() - LocalDate.now().toEpochDay());
                if(differExpireDate.toEpochDay()>=0){
                    notAcceptedWorks.add(order);
                    expireDays.add(differExpireDate.toEpochDay());
                }else{
                    workersToDelete.add(worker);
                }
            }
        });

        //Delete workers with date expired
        for(int i=0;i<workersToDelete.size();i++){
            developerService.deleteWorker(workersToDelete.get(i));
        }

        subscribedWorks.forEach(order ->{
            order.setTechnologies(technologyService.findTechnolodyByOrderingId(order.getId()));
        });



        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("finishedWorks", finishedWorks);
        resultMap.put("subscribedWorks", subscribedWorks);
        resultMap.put("processedWorks", worksInProgress);
        resultMap.put("notAcceptedWorks", notAcceptedWorks);
        resultMap.put("expireDays", expireDays);
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

        Map<String, List> resultMap = new HashMap<>();
        resultMap.put("devQAs", devQAs);
        resultMap.put("tests", tests);
        sendResponse(response, resultMap, mapper);
    }

    private void sendTestById(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        int testId = Integer.parseInt(request.getParameter("test_id"));
        TestService testService = (TestService) ApplicationContext
                .getInstance().getBean("testService");
        Test test = testService.findById(testId);
        test.setQuestions(testService.findQuestionsByTestId(testId));
        sendResponse(response,test,mapper);
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

    private void sendResultResponse(HttpServletResponse response, Double rate,
                                    Integer errors, Integer success) throws IOException {
        Map<String, Number> resultMap = new HashMap<>();
        resultMap.put("rate", rate);
        resultMap.put("errors", errors);
        resultMap.put("success", success);
        sendResponse(response, resultMap, mapper);

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
            String[] userEmail = new String[1];
            if(developer.getSendEmail() != null){
                userEmail[0] = developer.getSendEmail();
            } else {
                userEmail[0] = developer.getEmail();
            }
            SendMessageToEmail.sendHtmlFromGMail("onlineshopjava@gmail.com", "ForTestOnly", userEmail,
                    "OpenTask -  Confirmation code ", getConfirmCodeEmailMessage(generatePhoneCode(developer), developer.getFname()));
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
                StringBuilder confirmPhoneCode = new StringBuilder();
                SecureRandom random = new SecureRandom();
                for (int i = 0; i < 4; i++) {
                    confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
                }
                developer.setConfirmCode(confirmPhoneCode.toString());
                String[] userEmail = {email};
                SendMessageToEmail.sendHtmlFromGMail("onlineshopjava@gmail.com", "ForTestOnly", userEmail,
                        "OpenTask -  Confirmation code ", getConfirmCodeEmailMessage(confirmPhoneCode.toString(),developer.getFname()));
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

    private void acceptOrdering(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession();
        Developer dev = (Developer) session.getAttribute("user");
        Integer orderId = Integer.parseInt(request.getParameter("order_id"));

        //creating new worker
        Worker worker = developerService.getWorkerByDevIdAndOrderId(dev.getId(),orderId);
        worker.setAccepted(true);
        worker.setNewHourly(dev.getHourly());
        developerService.updateWorker(worker);

        //set started to ordering
        Ordering ordering = orderingService.findById(orderId);
        ordering.setStarted(true);
        ordering.setStartedDate(new Timestamp(new java.util.Date().getTime()));
        orderingService.modify(ordering);

        //get followers email and delete followers
        List<String> followersEmailsList = new ArrayList<>();
        orderingService.findOrderFollowers(orderId).forEach(follower -> {
            String email = developerService.findById(follower.getDevId()).getEmail();
             if(!email.equals(dev.getEmail())){followersEmailsList.add(email);}
            orderingService.deleteFollower(follower);
        });

        //send email to followers
        String arrayEmail[] = followersEmailsList.toArray(new String[followersEmailsList.size()]);
        SendMessageToEmail.sendFromGMail("onlineshopjava@gmail.com", "ForTestOnly",
                arrayEmail, "OpenTask - Notification about order", getAcceptedOrderMessageForFollowers(dev.getFname(),ordering.getTitle()));
    }
    private String getAcceptedOrderMessageForFollowers(String devName,String orderingName){
        return "Hello " +devName+"\n\n"+
                "Unfortunately you have not been chosen for ordering '"+orderingName+"'.";
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

    private String getConfirmCodeEmailMessage(String confirmCode,String userName){
        return "<div style=\"background-color:#dfdfdf;padding:0;margin:0 auto;width:100%\"> " +
                "<span style=\"display:none!important;font-size:1px;color:transparent;min-height:0;width:0\">Info about confirm code</span>" +
                " <table style=\"font-family:Helvetica,Arial,sans-serif;border-collapse:collapse;width:100%!important;font-family:Helvetica,Arial,sans-serif;margin:0;padding:0" +
                "\" bgcolor=\"#F5F5F5\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td colspan=\"3\">" +
                " <table style=\"font-family:Helvetica,Arial,sans-serif\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"1\"> " +
                "<tbody> <tr> <td> <div style=\"min-height:5px;font-size:5px;line-height:5px\"> &nbsp; </div></td></tr></tbody> </table>" +
                " </td></tr><tr> <td> <table style=\"table-layout:fixed\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "<tbody> <tr> <td align=\"center\"> <table style=\"font-family:Helvetica,Arial,sans-serif;min-width:290px\" border=\"0\" " +
                "cellpadding=\"0\" cellspacing=\"0\" width=\"540\"> <tbody> <tr> <td style=\"font-family:Helvetica,Arial,sans-serif\">" +
                " <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:8px;font-size:8px;line-height:8px\">" +
                " &nbsp; </div></td></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\" bgcolor=\"#DDDDDD\" border=\"0\" cellpadding=\"0\"" +
                " cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td align=\"left\" height=\"21\" valign=\"middle\" width=\"95\"> <a style=\"text-decoration:none;font-size:" +
                " 24pt;font-weight:bold;color:#273039;\" href=\"https://google.com\" target=\"_blank\">OpenTask</a> </td></tr></tbody> </table> <table border=\"0\"" +
                " cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:8px;font-size:8px;line-height:8px\"> &nbsp; </div>" +
                "</td></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\" bgcolor=\"#333333\" border=\"0\" cellpadding=\"0\"" +
                " cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td width=\"20\"> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"20\"> " +
                "<tbody> <tr> <td> <div style=\"min-height:0px;font-size:0px;line-height:0px\"> &nbsp; </div></td></tr></tbody> </table> </td><td width=\"100%\"> " +
                "<table style=\"table-layout:fixed\" border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"500\"> <tbody> <tr> <td width=\"500\"> " +
                "<div style=\"min-height:12px;font-size:12px;line-height:12px;width:500px\"> &nbsp; </div></td></tr></tbody> </table> </td><td width=\"20\">" +
                " <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"20\"> <tbody> <tr> <td> <div style=\"min-height:0px;font-size:0px;line-height:0px\">" +
                " &nbsp; </div></td></tr></tbody> </table> </td></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\" bgcolor=\"#FFFFFF\"" +
                " border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td width=\"20\"> <table border=\"0\" cellpadding=\"1\"" +
                " cellspacing=\"0\" width=\"20px\"> <tbody> <tr> <td> <div style=\"min-height:0px;font-size:0px;line-height:0px\"> &nbsp; </div></td></tr></tbody>" +
                " </table> </td><td style=\"color:#333333;font-family:Helvetica,Arial,sans-serif;font-size:15px;line-height:18px\" align=\"left\"> " +
                "<table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:20px;font-size:20px;line-height:20px\">" +
                " &nbsp; </div></td></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\" bgcolor=\"#FFFFFF\" border=\"0\" cellpadding=\"0\"" +
                " cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td>Hi "+userName+",</td></tr><tr> <td> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> " +
                "<tbody> <tr> <td> <div style=\"min-height:20px;font-size:20px;line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr><tr>" +
                " <td>Confirm code: <span style=\"text-decoration:none;color:#0077b5\">" + confirmCode + "</span>" +
                " </td></tr><tr> <td> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\">" +
                " <tbody> <tr> <td> <div style=\"min-height:20px;font-size:20px;line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr><tr> " +
                "<td>The confirm code will expire <span tabindex=\"0\" class=\"aBn\" data-term=\"goog_1735888483\"><span class=\"aQJ\">in 24 hours</span></span>," +
                " so be sure to use it right away.</td></tr><tr> <td> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> " +
                "<td> <div style=\"min-height:20px;font-size:20px;line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr><tr> <td>" +
                "The OpenTask Team</td></tr><tr> <td> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> " +
                "<div style=\"min-height:20px;font-size:20px;line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr></tbody> </table> </td><td width=\"20\">" +
                " <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"20px\"> <tbody> <tr> <td> <div style=\"min-height:0px;font-size:0px;line-height:0px\">" +
                " &nbsp; </div></td></tr></tbody> </table> </td></tr></tbody> </table> </td></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\"" +
                " border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"540\"> <tbody> <tr></tr></tbody> </table> <table style=\"font-family:Helvetica,Arial,sans-serif\"" +
                " border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"540\"> <tbody> <tr> <td> <table style=\"font-family:Helvetica,Arial,sans-serif\" border=\"0\"" +
                " cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> <tbody> <tr> <td> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"1\"> <tbody> <tr>" +
                " <td> <div style=\"min-height:10px;font-size:10px;line-height:10px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr><tr> <td align=\"center\">" +
                " <table style=\"color:#999999;font-size:11px;font-family:Helvetica,Arial,sans-serif\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> " +
                "<tbody> <tr> <td dir=\"ltr\" align=\"center\">© 2016 www.opentask.com | All rights reserved.</td></tr><tr> <td> <table border=\"0\" cellpadding=\"0\"" +
                " cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:10px;font-size:10px;line-height:10px\"> &nbsp; </div></td></tr></tbody> </table>" +
                " </td></tr><tr> <td align=\"center\"></td></tr><tr> <td align=\"center\"></td></tr><tr> <td> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"1\">" +
                " <tbody> <tr> <td> <div style=\"min-height:10px;font-size:10px;line-height:10px\"> &nbsp; </div></td></tr></tbody> </table> <table border=\"0\"" +
                " cellpadding=\"0\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:10px;font-size:10px;line-height:10px\"> &nbsp; </div>" +
                "</td></tr></tbody> </table> </td></tr><tr> <td align=\"center\"></td></tr><tr> <td> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"1\">" +
                " <tbody> <tr> <td> <div style=\"min-height:10px;font-size:10px;line-height:10px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr></tbody> </table>" +
                " </td></tr><tr> <td> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"1\"> <tbody> <tr> <td> <div style=\"min-height:20px;font-size:20px;" +
                "line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr></tbody> </table> </td></tr></tbody> </table> </td></tr></tbody> </table> </td>" +
                "</tr></tbody> </table></div>";
    }
}
package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.util.Paginator;
import com.epam.freelancer.database.model.AdminCandidate;
import com.epam.freelancer.database.model.OrderCounter;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rynik on 04.02.2016.
 */
public class AdminController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(CustomerController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private ObjectMapper mapper;
    private UserManager userManager;
    private AdminCandidateService adminCandidateService;
    private DeveloperService developerService;
    private CustomerService customerService;
    private AdminService adminService;

    private QuestionService questionService;
    private TestService testService;
    private AnswerService answerService;
    private TechnologyService technologyService;
    private Paginator paginator;
    private OrderCounterService orderCounterService;
    private OrderingService orderingService;
    private ComplaintService complaintService;


    public AdminController() {
        mapper = new ObjectMapper();
        userManager = (UserManager) ApplicationContext.getInstance().getBean("userManager");
        adminCandidateService = (AdminCandidateService) ApplicationContext.getInstance().getBean("adminCandidateService");
        developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
        customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
        adminService = (AdminService) ApplicationContext.getInstance().getBean("adminService");
        orderCounterService = (OrderCounterService) ApplicationContext.getInstance().getBean("orderCounterService");
        questionService = (QuestionService) ApplicationContext.getInstance().getBean("questionService");
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        answerService = (AnswerService) ApplicationContext.getInstance().getBean("answerService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
        paginator = new Paginator();
        orderingService = (OrderingService) ApplicationContext.getInstance().getBean("orderingService");
        complaintService = (ComplaintService) ApplicationContext.
                getInstance().getBean("complaintService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = FrontController.getPath(request);

            switch (path) {
                case "admin/statistics/devcust":
                    sendDevAndCustAmount(request, response);
                    break;
                case "admin/getPersonalData":
                    fillAdminPage(request, response);
                    break;
                case "admin/statistics/ordersCreation":
                    sendCreationOrdersAmount(request, response);
                    break;
                case "admin/statistics/orders":
                    sendOrderStatistic(request, response);
                    break;
                case "admin/statistics/tests":
                    sendCreationPopularTests(request, response);
                    break;
                case "admin/tests":
                    getTests(request, response);
                    break;
                case "admin/questions":
                    getQuestions(request, response);
                    break;
                case "admin/technologies":
                    getTechnologies(request, response);
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
                case "admin/create/new/admin":
                    sendLinkToCandidateMail(request, response);
                    break;
                case "admin/check/email":
                    checkAvailableEmail(request, response);
                    break;
                case "admin/check/uuid":
                    checkAvailableUUID(request, response);
                    break;
                case "admin/remove/uuid":
                    removeUUID(request, response);
                    break;
                case "admin/test/create":
                    createTest(request, response);
                    break;
                case "admin/question":
                    createQuestion(request, response);
                    break;
                case "admin/tech/questions":
                    getQuestionsByTechnologyId(request, response);
                    break;
                case "admin/test/delete":
                    deleteTest(request, response);
                    break;
                case "admin/question/delete":
                    deleteQuestion(request, response);
                    break;
                case "admin/sendPersonalData":
                    updatePersonalData(request, response);
                    break;
                case "admin/changePassword":
                    changeAdminPassword(request, response);
                    break;
                case "admin/confirmChangePasswordAndEmail":
                    confirmChangePasswordAndEmail(request, response);
                    break;
                case "admin/changeEmail":
                    changeEmail(request, response);
                    break;
                case "admin/orders/complained":
                    getComplainedOrders(request, response);
                    break;
                case "admin/order/ban":
                    banOrder(request, response);
                    break;
                case "admin/order/unban":
                    unbanOrder(request, response);
                    break;
                case "admin/orders/bans":
                    getBanOrders(request, response);
                    break;
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }


    private void sendLinkToCandidateMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String accessUUID = String.valueOf(UUID.randomUUID());
        Map<String, String[]> map = new HashMap<>();

        while (!userManager.isUUIDAvailable(accessUUID)) {
            accessUUID = String.valueOf(UUID.randomUUID());
        }

        String arrEmail[] = {email};
        String arrAccessUUID[] = {accessUUID};
        map.put("email", arrEmail);
        map.put("access_key", arrAccessUUID);
        AdminCandidate adminCandidate = adminCandidateService.create(map);
        if (adminCandidate != null) {
            startCountdownExpireTime(adminCandidate, 20);
        }

        String accessLink = request.getLocalAddr() + ":" + request.getLocalPort() + "/#/signup?uuid=" + accessUUID;
        SendMessageToEmail.sendFromGMail("onlineshopjava@gmail.com", "ForTestOnly", arrEmail, "OpenTask -  Admin Registration ", getAdminCreatingMessage() + accessLink);
    }

    private void startCountdownExpireTime(AdminCandidate candidate, int secDelay) {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() -> adminCandidateService.remove(candidate)
                , secDelay, TimeUnit.HOURS);
    }

    private String getAdminCreatingMessage() {
        return "Hey, you have permissions to sign up as administrator in Freelancer." +
                " Verifying this address will let you do this right now." +
                "If you was not notified about this message by persons of Freelancer, ignore this email, and contact us." + "\n\n" +
                " This offer will be available only 1 hour. If you wish to continue, please follow the link below: " + "\n";
    }

    private void checkAvailableUUID(HttpServletRequest request, HttpServletResponse response) {
        String uuid = request.getParameter("uuid");
        if (adminCandidateService.getAdminCandidateByKey(uuid) != null) {
            sendResponse(response, true, mapper);
        } else {
            sendResponse(response, false, mapper);
        }
    }

    private void checkAvailableEmail(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        Map<String, Boolean> map = new HashMap<>();

        if (adminCandidateService.getAdminCandidateByEmail(email) != null) {
            map.put("candidateEmailExists", true);
        }
        if (!customerService.emailAvailable(email) || !developerService.emailAvailable(email)) {
            map.put("otherUserEmailExists", true);
        }
        sendResponse(response, map, mapper);
    }

    private void removeUUID(HttpServletRequest request, HttpServletResponse response) {
        String uuid = request.getParameter("uuid");
        adminCandidateService.remove(adminCandidateService.getAdminCandidateByKey(uuid));
    }

    private void sendDevAndCustAmount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("devAmount", developerService.getAllWorkers().size());
        map.put("custAmount", customerService.findAll().size());
        sendResponse(response, map, mapper);
    }

    private void getTests(HttpServletRequest request, HttpServletResponse response) {
        List<Test> tests = testService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(),
                technology));
        for (int i = 0; i < tests.size(); i++) {
            tests.forEach(test -> {
                test.setTechnology(technologyMap.get(test.getTechId()));
            });
        }
        sendResponse(response, tests, mapper);
    }

    private void getQuestionsByTechnologyId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestData = request.getReader().readLine();
        JsonPaginator jsonPaginator = mapper.readValue(requestData, JsonPaginator.class);

        List<Question> questions = questionService.filterElements(jsonPaginator.getContent(), jsonPaginator.getPage().getStart() * jsonPaginator.getPage().getStep(), jsonPaginator.getPage().getStep());

        paginator.next(jsonPaginator.getPage(), response, questionService.getObjectAmount(), questions);
    }

    private void getTechnologies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendResponse(response, technologyService.findAll(), mapper);
    }

    private void createTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramTest = request.getParameter("test");
        String paramQuestions = request.getParameter("questions");
        Test test = mapper.readValue(paramTest, new TypeReference<Test>() {
        });
        List<Integer> questIDs = mapper.readValue(paramQuestions, new TypeReference<List<Integer>>() {
        });
        test = writeTestInDB(test, request);
        if (test == null)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        else {
            testService.saveTestQuestions(test.getId(), questIDs);
            response.getWriter().write(test.getId());
        }
    }

    private Test writeTestInDB(Test test, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        Map<String, String[]> testMap = new HashMap<>();
        testMap.put("name", new String[]{test.getName()});
        testMap.put("tech_id", new String[]{String.valueOf(test.getTechId())});
        testMap.put("admin_id", new String[]{String.valueOf(ue.getId())});
        testMap.put("sec_per_quest", new String[]{String.valueOf(test.getSecPerQuest())});
        testMap.put("pass_score", new String[]{String.valueOf(test.getPassScore())});
        return testService.create(testMap);
    }

    private void createQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramQuestion = request.getParameter("question");
        String paramAnswers = request.getParameter("answers");
        Question question = mapper.readValue(paramQuestion, new TypeReference<Question>() {
        });
        List<Answer> answers = mapper.readValue(paramAnswers, new TypeReference<List<Answer>>() {
        });
        int multiple = 0;
        for (Answer answer : answers) {
            if (answer.getCorrect()) multiple++;
        }
        question.setMultiple(multiple > 1);
        question = writeQuestionInDB(question, request);
        if (question == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            writeAnswersInDB(question.getId(), answers, request);
            response.getWriter().write(question.getId());
        }
    }

    private Question writeQuestionInDB(Question question, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserEntity ue = (UserEntity) session.getAttribute("user");
        Map<String, String[]> questionMap = new HashMap<>();
        questionMap.put("name", new String[]{question.getName()});
        questionMap.put("tech_id", new String[]{String.valueOf(question.getTechId())});
        questionMap.put("admin_id", new String[]{String.valueOf(ue.getId())});
        questionMap.put("multiple", new String[]{String.valueOf(question.getMultiple())});
        return questionService.create(questionMap);
    }

    private void writeAnswersInDB(Integer questionId, List<Answer> answers, HttpServletRequest request) {
        Map<String, String[]> answerMap;
        for (Answer answer : answers) {
            answerMap = new HashMap<>();
            answerMap.put("name", new String[]{answer.getName()});
            answerMap.put("question_id", new String[]{String.valueOf(questionId)});
            answerMap.put("correct", new String[]{String.valueOf(answer.getCorrect())});
            answerService.create(answerMap);
        }
    }

    private void deleteTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramTest = request.getParameter("test");
        if (paramTest == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Test test = mapper.readValue(paramTest, new TypeReference<Test>() {
        });
        test.setDeleted(true);
        testService.modify(test);
    }

    private void getQuestions(HttpServletRequest request, HttpServletResponse response) {
        List<Question> questions = questionService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(),
                technology));
        for (int i = 0; i < questions.size(); i++) {
            questions.forEach(test -> {
                test.setTechnology(technologyMap.get(test.getTechId()));
            });
        }
        sendResponse(response, questions, mapper);
    }


    private void deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paramQuestion = request.getParameter("question");
        if (paramQuestion == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Question question = mapper.readValue(paramQuestion, new TypeReference<Question>() {
        });
        question.setDeleted(true);
        questionService.modify(question);
    }


    private void sendCreationOrdersAmount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<OrderCounter> list = orderCounterService.getAllForLast30Days();
        Map<LocalDate, Integer> map = new TreeMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<Integer> listDays = new ArrayList<>();
        List<String> listMonth = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 30; i++) {
            cal.add(Calendar.DATE, -1);
            map.put(new java.sql.Date(cal.getTimeInMillis()).toLocalDate(), 0);
        }
        for (OrderCounter o : list) {
            if (map.containsKey(o.getDate().toLocalDate())) {
                map.put(o.getDate().toLocalDate(), o.getCount());
            }
        }
        for (LocalDate date : map.keySet()) {
            listDays.add(date.getDayOfMonth());
            listMonth.add(date.getMonth().toString());
        }
        resultMap.put("orderValues", map.values());
        resultMap.put("listDays", listDays);
        resultMap.put("listMonth", listMonth);

        sendResponse(response, resultMap, mapper);
    }

    private void fillAdminPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("user");
        admin.setPassword(null);
        admin.setSalt(null);
        admin.setUuid(null);
        admin.setRegUrl(null);
        sendResponse(response, admin, mapper);
    }

    private void sendCreationPopularTests(HttpServletRequest request, HttpServletResponse response) {
        Map<Test, Integer> testMap = testService.getPopularTests();
        Map<String, Object> resultMap = new HashMap<>();
        Set<Test> tests = testMap.keySet();
        tests.forEach(test -> {
            test.setTechnology(technologyService.findById(test.getTechId()));
        });
        resultMap.put("tests", tests);
        resultMap.put("amounts", testMap.values());

        sendResponse(response, resultMap, mapper);

    }

    private void updatePersonalData(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        String paramAdmin = request.getParameter("admin");

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        mapper.setDateFormat(format);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        if (paramAdmin == null || "".equals(paramAdmin)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Admin admin = mapper.readValue(paramAdmin, new TypeReference<Admin>() {
        });
        Admin dbAdmin = adminService.findById(((UserEntity) request.getSession().getAttribute("user")).getId());
        admin.setPassword(dbAdmin.getPassword());
        admin.setSalt(dbAdmin.getSalt());
        admin.setUuid(dbAdmin.getUuid());
        admin.setRegUrl(dbAdmin.getRegUrl());
        adminService.modify(admin);
        admin.setPassword(null);
        admin.setSalt(null);
        admin.setUuid(null);
        admin.setRegUrl(null);
        admin.setRole("admin");
        request.getSession().setAttribute("user", admin);
    }

    private void changeAdminPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("user");
        if (userManager.validCredentials(admin.getEmail(), password, adminService.findById(admin.getId()))) {
            generateEmailCode(admin);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid credentials");
        }
    }

    private void confirmChangePasswordAndEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String confirmCode = request.getParameter("confirmCode");
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("user");
        if (password != null) {
            if (checkConfirmCode(admin, confirmCode, response)) {
                admin.setPassword(password);
                adminService.encodePassword(admin);
                adminService.modify(admin);
            }
        }
        if (email != null) {
            if (checkConfirmCode(admin, confirmCode, response)) {
                admin = adminService.findById(admin.getId());
                admin.setSendEmail(email);
                adminService.modify(admin);
                admin.setPassword(null);
                admin.setSalt(null);
                admin.setUuid(null);
                admin.setRegUrl(null);
                admin.setRole("admin");
                session.setAttribute("user", admin);
            }
        }
    }

    private void changeEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("user");
        if (admin != null) {
            if (userManager.isEmailAvailable(email) || admin.getEmail().equals(email)) {
                generateEmailCode(admin);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user");
            return;
        }
    }

    private String generateEmailCode(Admin admin) {
        StringBuilder confirmCode = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            confirmCode.append(String.valueOf(random.nextInt(9)));
        }
        admin.setConfirmCode(confirmCode.toString());
        try {
            String email = admin.getEmail();
            String from = "maksym.rudevych.kn.2013@lpnu.ua";
            String pass = "12.04.1996";
            String[] to = new String[]{email};
            SendMessageToEmail.sendFromGMail(from, pass, to, "Confirm Code", confirmCode.toString());
        } catch (NullPointerException e) {
            LOG.warn("The phone number is empty");
        } catch (IOException e) {
            LOG.warn("Email sending crashed");
        }
        return confirmCode.toString();
    }

    private Boolean checkConfirmCode(Admin admin, String confirmCode, HttpServletResponse response) throws IOException {
        if (!admin.getConfirmCode().equals(confirmCode)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid code");
            response.flushBuffer();
            return false;
        }
        return true;
    }

     private void sendOrderStatistic(HttpServletRequest request,HttpServletResponse response){
        List<Ordering> allOrders = orderingService.findAll();
        Integer finishedCount = 0;
        Integer inProgressCount = 0;
        Integer availableCount = 0;
        if(allOrders != null) {
            for (Ordering ordering : allOrders) {
                if (ordering.getStarted() != null && ordering.getStarted() && ordering.getEnded() != null  && ordering.getEnded()) {
                    finishedCount++;
                } else {
                    if (ordering.getStarted()) {
                        inProgressCount++;
                    } else {
                        availableCount++;
                    }
                }
            }
        }

       Map<String,Integer> map = new HashMap<>();
       map.put("finishedCount",finishedCount);
       map.put("inProgressCount",inProgressCount);
       map.put("availableCount",availableCount);

       sendResponse(response,map,mapper);
    }

    private void getComplainedOrders(HttpServletRequest request,HttpServletResponse response){
        sendResponse(response, orderingService.getComplainedOrders(),mapper);
    }

    private void getBanOrders(HttpServletRequest request,HttpServletResponse response){
        sendResponse(response, orderingService.getBanOrders(),mapper);
    }

    private void banOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String paramId = request.getParameter("orderId");
        if(paramId == null || "".equals(paramId) || !paramId.matches("[0-9]*")){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Integer id = Integer.parseInt(paramId);
        Ordering order = orderingService.findById(id);
        order.setBan(true);
        orderingService.modify(order);
    }

    private void unbanOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String paramId = request.getParameter("orderId");
        if(paramId == null || "".equals(paramId) || !paramId.matches("[0-9]*")){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Integer id = Integer.parseInt(paramId);
        Ordering order = orderingService.findById(id);
        order.setBan(false);
        orderingService.modify(order);
    }
}

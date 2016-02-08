package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.json.model.Quest;
import com.epam.freelancer.web.util.Paginator;
import com.epam.freelancer.database.model.AdminCandidate;
import com.epam.freelancer.database.model.OrderCounter;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private QuestionService questionService;
    private TestService testService;
    private AnswerService answerService;
    private TechnologyService technologyService;
    private Paginator paginator;
    private OrderCounterService orderCounterService;

    public AdminController() {
        mapper = new ObjectMapper();
        userManager = (UserManager) ApplicationContext.getInstance().getBean("userManager");
        adminCandidateService = (AdminCandidateService) ApplicationContext.getInstance().getBean("adminCandidateService");
        developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
        customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
        orderCounterService = (OrderCounterService)ApplicationContext.getInstance().getBean("orderCounterService");
        questionService = (QuestionService) ApplicationContext.getInstance().getBean("questionService");
        testService = (TestService) ApplicationContext.getInstance().getBean("testService");
        answerService = (AnswerService) ApplicationContext.getInstance().getBean("answerService");
        technologyService = (TechnologyService) ApplicationContext.getInstance().getBean("technologyService");
        paginator = new Paginator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String path = FrontController.getPath(request);

            switch (path) {
                case "admin/statistics/devcust":
                    sendDevAndCustAmount(request, response);
                    break;
                case "admin/statistics/orders":
                    sendCreationOrdersAmount(request, response);
                    break;
                case "admin/tests":
                    getTests(request, response);
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
        SendMessageToEmail.sendFromGMail("onlineshopjava@gmail.com", "ForTestOnly", arrEmail, "Freelancer -  Admin Registration ", getAdminCreatingMessage() + accessLink);
    }

    private void startCountdownExpireTime(AdminCandidate candidate, int secDelay) {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() ->  adminCandidateService.remove(candidate)
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

    private void removeUUID(HttpServletRequest request, HttpServletResponse response) {
        String uuid = request.getParameter("uuid");
        adminCandidateService.remove(adminCandidateService.getAdminCandidateByKey(uuid));
    }

    private void sendDevAndCustAmount(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
        Map<String,Integer> map = new HashMap<>();
        map.put("devAmount",developerService.getAllWorkers().size());
        map.put("custAmount",customerService.findAll().size());

        sendResponse(response, map, mapper);
    }

    private void getTests(HttpServletRequest request, HttpServletResponse response) {
        List<Test> tests = testService.findAll();
        List<Technology> techs = technologyService.findAll();
        Map<Integer, Technology> technologyMap = new HashMap<>();
        techs.forEach(technology -> technologyMap.put(technology.getId(),
                technology));
        Map<Integer, Test> testMap = new HashMap<>();
        for (int i = 0; i < tests.size(); i++) {
            tests.forEach(test -> {
                test.setTechnology(technologyMap.get(test.getTechId()));
                testMap.put(test.getId(), test);
            });
        }
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
        for(Answer answer:answers){
            if(answer.getCorrect()) multiple++;
        }
        question.setMultiple(multiple>1);
        question = writeQuestionInDB(question, request);
        if(question == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }else{
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

    private void sendCreationOrdersAmount(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
        List<OrderCounter> list = orderCounterService.getAllForLast30Days();
        Map<LocalDate,Integer> map = new TreeMap<>();
        Map<String,Object> resultMap = new HashMap<>();
        List<Integer> listDays = new ArrayList<>();
        List<String> listMonth = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 30; i++) {
            cal.add(Calendar.DATE, -1);
            map.put(new Date(cal.getTimeInMillis()).toLocalDate(),0);
        }


        for (OrderCounter o:list){
            if(map.containsKey(o.getDate().toLocalDate())){
                map.put(o.getDate().toLocalDate(),o.getCount());
            }
        }


        for (LocalDate date:map.keySet()) {
            listDays.add(date.getDayOfMonth());
            listMonth.add(date.getMonth().toString());
        }

        resultMap.put("orderValues",map.values());
        resultMap.put("listDays",listDays);
        resultMap.put("listMonth",listMonth);

        sendResponse(response,resultMap,mapper);



    }



}

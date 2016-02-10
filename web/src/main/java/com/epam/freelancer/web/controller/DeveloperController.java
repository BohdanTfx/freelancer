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
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {

            String path = FrontController.getPath(request);

            switch (path) {
                case "dev/getalltests":
                    fillTestPage(request, response);
                    break;
                case "dev/gettestbyid":
                    sendTestById(request, response);
                    break;
                case "dev/getallworks":
                    fillMyWorksPage(request, response);
                    break;
                case "dev/getcustomerbyid":
                    sendCustomerById(request, response);
                    break;
                case "dev/getworkersbyidorder":
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
			HttpServletResponse response) throws ServletException, IOException
	{
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
                default:
            }

		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	private void fillMyWorksPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		UserEntity user = (UserEntity) session.getAttribute("user");

		List<Ordering> allProjects = developerService
				.getDeveloperPortfolio(user.getId());
		List<Ordering> devSubscribedProjects = developerService
				.getDeveloperSubscribedProjects(user.getId());

		for (Ordering order : devSubscribedProjects) {
			order.setTechnologies(technologyService
					.findTechnolodyByOrderingId(order.getId()));
		}

		List<Ordering> devFinishedProjects = new ArrayList<>();
		List<Ordering> devProjectsInProcess = new ArrayList<>();
		allProjects.forEach(ordering -> {
			if (ordering.getEnded()) {
				devFinishedProjects.add(ordering);
			} else {
				devProjectsInProcess.add(ordering);
			}
		});

		for (Ordering order : devFinishedProjects) {
			order.setTechnologies(technologyService
					.findTechnolodyByOrderingId(order.getId()));
		}

		for (Ordering order : devProjectsInProcess) {
			order.setTechnologies(technologyService
					.findTechnolodyByOrderingId(order.getId()));
		}

		Map<String,List> resultMap = new HashMap<>();
		resultMap.put("finishedWorks",devFinishedProjects);
		resultMap.put("subscribedWorks", devSubscribedProjects);
		resultMap.put("processedWorks", devProjectsInProcess);
		sendResponse(response, resultMap, mapper);
	}

	private void fillTestPage(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		UserEntity user = (UserEntity) session.getAttribute("user");
		List<DeveloperQA> devQAs = developerQAService.findAllByDevId(user
				.getId());

		List<Test> tests = testService.findAll();
		List<Technology> techs = technologyService.findAll();
		Map<Integer, Technology> technologyMap = new HashMap<>();
		techs.forEach(technology -> technologyMap.put(technology.getId(),
				technology));
		Map<Integer, Test> testMap = new HashMap<>();
		for (int i = 0; i < tests.size(); i++) {
		}
		tests.forEach(test -> {
			test.setTechnology(technologyMap.get(test.getTechId()));
			testMap.put(test.getId(), test);
		});
		for (DeveloperQA developerQA : devQAs) {
			developerQA.setTest(testMap.get(developerQA.getTestId()));
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
			HttpServletResponse response) throws IOException
	{
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
			HttpServletResponse response) throws IOException
	{
		Integer custId = Integer.parseInt(request.getParameter("cust_id"));
		CustomerService customerService = (CustomerService) ApplicationContext
				.getInstance().getBean("customerService");
		Customer cust = customerService.findById(custId);
		Map<String,Customer> resultMap = new HashMap<>();
		resultMap.put("cust", cust);

		sendResponse(response, resultMap, mapper);

	}

	private void sendWorkersByIdOrder(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		UserEntity user = (UserEntity) session.getAttribute("user");
		Integer orderId = Integer.parseInt(request.getParameter("order_id"));
		Worker worker = developerService.getWorkerByDevIdAndOrderId(
				user.getId(), orderId);
		List<Developer> developers = developerService
				.getDevelopersByIdOrder(orderId);
		if (developers.contains(worker))
			developers.remove(worker);

		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("workers",developers);
		resultMap.put("workerInfo",worker);

		sendResponse(response,resultMap,mapper);
	}

	private void sendResults(HttpServletRequest request,
			HttpServletResponse response)
	{
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
									.contains(answer.getId())))
					{
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
			double rate)
	{
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
			int errors, int success) throws IOException
	{
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
			HttpServletResponse response) throws IOException
	{
		List<Technology> technologies = null;
		HttpSession session = request.getSession();
		Developer developer = (Developer) session.getAttribute("user");
		Contact contact = null;
		List<String> listTechs = new ArrayList<>();
		try {
			technologies = developerService.
					getTechnologiesByDevId(developer.getId());
			contact = developerService.getContactByDevId(developer.getId());
			developer = developerService.findById(developer.getId());
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error when get data from developer table" + e.getMessage());
		}
		String devTechsJson = new Gson().toJson(technologies);
		String developerJson = new Gson().toJson(developer);
		String contactJson = new Gson().toJson(contact);
		technologies = technologyService.findAll();
		for(Technology tech : technologies){
			listTechs.add(tech.getName());
		}
		String allTechsJson = new Gson().toJson(listTechs);
		String resultJson = "{\"dev\":" + developerJson + ",\"techs\":" + devTechsJson +",\"contacts\":" + contactJson + ",\"allTechs\":" + allTechsJson +"}";
		if(devTechsJson.length() == 2){
			resultJson = "{\"dev\":" + developerJson + ",\"contacts\":" + contactJson + ",\"allTechs\":" + allTechsJson +"}";
		} if(contactJson.length() == 0) {
		resultJson = "{\"dev\":" + developerJson + ",\"techs\":" + devTechsJson + ",\"allTechs\":" + allTechsJson +"}";
	} if(contactJson.length() == 0 && devTechsJson.length() == 2) {
		resultJson = "{\"dev\":" + developerJson + ",\"allTechs\":" + allTechsJson +"}";
	}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson);
	}

	private void updatePersonalData(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{

		Developer developer = null;
		Contact contact = null;
		List<Technology> technologies = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
		List<Technology> allTechnology = technologyService.findAll();
		mapper.setDateFormat(format);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true );
		String developerJson = request.getParameter("developer");
		try {
			developer = mapper.readValue(developerJson, Developer.class);

		} catch(Exception e){
			LOG.warn("Some problem with mapper");
		}
		String technologiesJson = request.getParameter("technologies");
		if(technologiesJson.length() != 0) {
			technologies = mapper.readValue(technologiesJson, new TypeReference<List<Technology>>() {});
		}

		String contactJson = request.getParameter("contact");
		if(contactJson.length() != 0 ){
			contact = mapper.readValue(contactJson, Contact.class);
		}
		if(developerService.getContactByDevId(developer.getId()) == null){
			contact.setDevId(developer.getId());
			developerService.createContact(contact);
		} else {
			contact.setDevId(developer.getId());
			developerService.updateContact(contact);
		}

//        if(technologies != null){
//			List<Technology> addedTechnology = developerService.getTechnologiesByDevId(developer.getId());
//			for(Technology addedTechs : addedTechnology){
//				for(Technology technology : technologies){
//					if(!addedTechs.equals(technology)){
//						developerService.addTechnologyForDev(developer.getId(), technology.getId());
//					}
//				}
//			}
//
//        }
		developerService.updateDeveloper(developer);
	}

	private void changeDeveloperPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newPassword");
		HttpSession session = request.getSession();
		Developer developer = (Developer) session.getAttribute("user");

		if(developer != null){
			if(userManager.validCredentials(developer.getEmail(), password, developer)){
				String confirmCodeJson = new Gson().toJson(generatePhoneCode(developer));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(confirmCodeJson);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				response.flushBuffer();
				return;
			}
		}
	}

	private void confirmChangePasswordAndEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String confirmCode = request.getParameter("confirmCode");
		HttpSession session = request.getSession();
		Developer developer = (Developer) session.getAttribute("user");

		if(password != null){
			if(checkConfirmCode(developer, confirmCode, response)){
				developer.setPassword(password);
				developerService.encodePassword(developer);
				developerService.updateDeveloper(developer);
			}
		}
		if(email != null){
			if(checkConfirmCode(developer, confirmCode, response)){
				developer.setSendEmail(email);
				developerService.updateDeveloper(developer);
			}
		}
	}

	private void changeEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");
		HttpSession session = request.getSession();
		Developer developer = (Developer) session.getAttribute("user");
		if(developer != null){
			if(userManager.isEmailAvailable(email)){
				String confirmCodeJson = new Gson().toJson(generatePhoneCode(developer));
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(confirmCodeJson);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid email");
				response.flushBuffer();
				return;
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid user");
			response.flushBuffer();
			return;
		}
	}

	private String generatePhoneCode(Developer developer){
		StringBuilder confirmPhoneCode =  new StringBuilder();
		SecureRandom random = new SecureRandom();
		for(int i = 0; i < 4; i++){
			confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
		}
		developer.setConfirmCode(confirmPhoneCode.toString());
		try{
			String phoneNumber = developerService.getContactByDevId(developer.getId()).getPhone();
			SmsSender smsSender = new SmsSender();
			smsSender.sendSms(phoneNumber, "Confirm code: " + confirmPhoneCode.toString(),
					"e-freelance");
		} catch(NullPointerException e){
			LOG.warn("The phone number is empty");
		}
		return confirmPhoneCode.toString();
	}

	private Boolean checkConfirmCode(Developer developer, String confirmCode, HttpServletResponse response) throws IOException {
		if(developer.getConfirmCode().equals(confirmCode)){
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new Gson().toJson("good"));
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid code");
			response.flushBuffer();
			return false;
		}
		return true;
	}
}
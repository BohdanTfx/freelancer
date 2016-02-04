package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.service.*;
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
	private CustomerService customerService;

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
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
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
			case "dev/getTestByDevId":
				getTestByDevId(request, response);
			case "dev/personal":
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
			case "dev/getresults":
				sendResults(request, response);
				break;
			case "dev/sendpersonaldata":
				updatePersonalData(request, response);
				break;
			case "dev/uploadImage":
				uploadImage(request, response);
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
		System.out.println("USer" + user);
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

		String devFinishedWorksJson = new Gson().toJson(devFinishedProjects);
		String devProjectsInProcessJson = new Gson()
				.toJson(devProjectsInProcess);
		String devSubscribedWorksJson = new Gson()
				.toJson(devSubscribedProjects);
		String resultJson = "{\"processedWorks\":" + devProjectsInProcessJson
				+ ",\"finishedWorks\":" + devFinishedWorksJson
				+ ",\"subscribedWorks\":" + devSubscribedWorksJson + "}";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson);
	}

	public void getTestByDevId(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");

		try {
			Integer devId = Integer.parseInt(param);
			TestService ts = (TestService) ApplicationContext.getInstance()
					.getBean("testService");
			DeveloperQAService dQAs = (DeveloperQAService) ApplicationContext
					.getInstance().getBean("developerQAService");
			List<DeveloperQA> developerQAs = dQAs.findAllByDevId(devId);
			for (DeveloperQA developerQA : developerQAs) {
				developerQA.setTest(ts.findById(developerQA.getTestId()));
				if (developerQA.getTest().getPassScore() >= developerQA
						.getRate())
				{
					developerQA.setIsPassed(false);
				} else {
					developerQA.setIsPassed(true);
				}
			}
			TechnologyService technologyService = (TechnologyService) ApplicationContext
					.getInstance().getBean("technologyService");
			for (DeveloperQA developerQA : developerQAs) {
				developerQA.getTest().setTechnology(
						technologyService.findById(developerQA.getTestId()));
			}
			sendResponse(response, developerQAs, mapper);
		} catch (Exception e) {
			response.sendError(500);
			return;
		}
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
		String custJson = new Gson().toJson(cust);
		String resultJson = "{\"cust\":" + custJson + "}";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson);
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
		String devListJson = new Gson().toJson(developers);
		String workerInfoJson = new Gson().toJson(worker);
		String resultJson = "{\"workers\":" + devListJson + ", \"workerInfo\":"
				+ workerInfoJson + "}";
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson);

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
		map.put("dev_id", new String[] { String.valueOf(userId) });
		map.put("test_id", new String[] { request.getParameter("testId") });
		map.put("rate", new String[] { String.valueOf(rate) });
		map.put("expireDate",
				new String[] { request.getParameter("expireDate") });
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
		/*
		 * List<Technology> technologies = null; HttpSession session = null;
		 * Developer developer = null; Contact contact = null; String
		 * developerJson; String devTechsJson; String resultJson; String
		 * contactJson; String allTechsJson; List<String> listTechs = new
		 * ArrayList<>(); session = request.getSession(); try { developer =
		 * (Developer) session.getAttribute("user"); technologies =
		 * developerService. getTechnologiesByDevId(developer.getId());
		 * System.out.println(technologies); contact =
		 * developerService.getContactByDevId(developer.getId());
		 * System.out.println(contact); } catch (Exception e) {
		 * e.printStackTrace();
		 * LOG.error("Error when get data from developer table" +
		 * e.getMessage()); } devTechsJson = new Gson().toJson(technologies);
		 * developerJson = new Gson().toJson(developer); contactJson = new
		 * Gson().toJson(contact); technologies = technologyService.findAll();
		 * for(Technology tech : technologies){ listTechs.add(tech.getName()); }
		 * allTechsJson = new Gson().toJson(listTechs);
		 * System.out.println("ALL TECHNOLOGIES" + allTechsJson); resultJson =
		 * "{\"dev\":" + developerJson + ",\"techs\":" + devTechsJson
		 * +",\"contacts\":" + contactJson + ",\"allTechs\":" + allTechsJson
		 * +"}"; if(devTechsJson.length() == 2){ resultJson = "{\"dev\":" +
		 * developerJson + ",\"contacts\":" + contactJson + ",\"allTechs\":" +
		 * allTechsJson +"}"; } if(contactJson.length() == 0) { resultJson =
		 * "{\"dev\":" + developerJson + ",\"techs\":" + devTechsJson +
		 * ",\"allTechs\":" + allTechsJson +"}"; } if(contactJson.length() == 0
		 * && devTechsJson.length() == 2) { resultJson = "{\"dev\":" +
		 * developerJson + ",\"allTechs\":" + allTechsJson +"}"; }
		 * response.setContentType("application/json");
		 * response.setCharacterEncoding("UTF-8");
		 * response.getWriter().write(resultJson);
		 */
	}

	private void updatePersonalData(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{

		Developer developer = null;
		Contact contact;
		String developerJson;
		String technologiesJson;
		String contactJson;

		List<Technology> technologies;

		SimpleDateFormat format = new SimpleDateFormat(
				"MMM dd, yyyy hh:mm:ss a");
		mapper.setDateFormat(format);

		developerJson = request.getParameter("developer");
		System.out.println(developerJson);

		try {
			developer = mapper.readValue(developerJson, Developer.class);

		} catch (Exception e) {
			LOG.warn("Some problem with mapper");
		}

		System.out.println(developer);

		technologiesJson = request.getParameter("technologies");
		System.out.println(technologiesJson);

		System.out.println(technologiesJson.length());

		if (technologiesJson.length() != 0) {
			technologies = mapper.readValue(technologiesJson,
					new TypeReference<List<Technology>>() {
					});
			System.out.println(technologies);
		}

		contactJson = request.getParameter("contact");
		System.out.println(contactJson);

		if (contactJson.length() != 0) {
			contact = mapper.readValue(contactJson, Contact.class);
			System.out.println(contact);
		}

	}

	private void uploadImage(HttpServletRequest request,
			HttpServletResponse response){
		String imageJson= request.getParameter("image");
	}

}
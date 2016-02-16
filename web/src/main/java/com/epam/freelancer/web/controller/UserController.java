package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.resize.ImageResize;
import com.epam.freelancer.business.service.*;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.business.util.SmsSender;
import com.epam.freelancer.database.model.*;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.json.model.JsonPaginator;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.social.model.LinkedinProfile;
import com.epam.freelancer.web.util.Paginator;
import com.epam.freelancer.web.util.SignInType;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.exceptions.OAuthException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserController extends HttpServlet implements Responsable {
	public static final Logger LOG = Logger.getLogger(UserController.class);
	private static final long serialVersionUID = -2356506023594947745L;
	private AuthenticationProvider authenticationProvider;
	private FeedbackService feedbackService;
	private OrderingService orderingService;
	private AdminService adminService;
	private CustomerService customerService;
	private DeveloperService developerService;
	private TechnologyService technologyService;
	private ComplaintService complaintService;
	private UserManager userManager;
	private Linkedin linkedin;
	private ObjectMapper mapper;
	private Paginator paginator;

	public UserController() {
		init();
	}

	@Override
	public void init() {
		LOG.info(getClass().getSimpleName() + " - " + " loaded");
		linkedin = new Linkedin();
		mapper = new ObjectMapper();
		paginator = new Paginator();
		try {
			linkedin.initKeys("/social.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		orderingService = (OrderingService) ApplicationContext.getInstance()
				.getBean("orderingService");
		technologyService = (TechnologyService) ApplicationContext
				.getInstance().getBean("technologyService");
		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
		adminService = (AdminService) ApplicationContext.getInstance().getBean(
                "adminService");
		customerService = (CustomerService) ApplicationContext.getInstance()
				.getBean("customerService");
		developerService = (DeveloperService) ApplicationContext.getInstance()
				.getBean("developerService");
		authenticationProvider = AuthenticationProvider
				.createAuthenticationProvider();
		feedbackService = (FeedbackService) ApplicationContext.getInstance()
				.getBean("feedbackService");
		complaintService = (ComplaintService) ApplicationContext.getInstance()
				.getBean("complaintService");
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			switch (FrontController.getPath(request)) {
			case "user/email":
				sendResponse(response, userManager.isEmailAvailable(request
						.getParameter("email")), mapper);
				return;
			case "user/social":
				configSocials(request, response);
				return;
			case "user/signup/linkedin":
				sendResponse(response, getLinkedInProfile(request, response),
						mapper);
				return;
			case "user/signin/linkedin":
				signIn(request, response, SignInType.LINKEDIN);
				return;
			case "user/developers/payment/limits":
				getPaymentLimits(response);
				return;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void getPaymentLimits(HttpServletResponse response) {
		Map<String, Double> limits = new HashMap<>();
		limits.put("min", developerService.findPaymentLimit("min"));
		limits.put("max", developerService.findPaymentLimit("max"));

		sendResponse(response, limits, mapper);
	}

	private LinkedinProfile getLinkedInProfile(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String oauthVerifier = request.getParameter("verifier");
		try {
			linkedin.loadData(oauthVerifier);
			return linkedin.getProfile();
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		return null;
	}

	private void configSocials(HttpServletRequest request,
			HttpServletResponse response)
	{
		String callbackUrl = request.getParameter("callbackUrlLinkedIn");
		Map<String, Object> result = new HashMap<>();
		result.put("linkedinUrl", linkedin.getAuthentificationUrl(callbackUrl));
		sendResponse(response, result, mapper);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
			case "user/signin":
				signIn(request, response, SignInType.MANUAL);
				return;
			case "user/create":
				create(request, response);
				return;
			case "user/getById":
				getById(request, response);
				return;
			case "user/getTechById":
				getTechById(request, response);
				return;
			case "user/getContById":
				getContById(request, response);
				return;
			case "user/getPortById":
				getPortfolioById(request, response);
				return;
			case "user/getFeed":
				getFeedbackByIdForDev(request, response);
				return;
			case "user/getRateForCust":
				getRateForCust(request, response);
				break;
			case "user/send":
				send(request, response);
				return;
			case "user/comment":
				comment(request, response);
				return;
			case "user/sms":
				sendSmsAndFollowOrHire(request, response);
				return;
			case "user/isAuth":
				isAuth(request, response);
				return;
			case "user/logout":
				logout(request, response);
				return;
			case "user/orders/filter":
				filterOrders(request, response);
				break;
			case "user/orders/limits":
				sendResponse(response, orderingService.findPaymentLimits(),
						mapper);
				break;
			case "user/technologies":
				sendResponse(response, technologyService.findAll(), mapper);
				break;
			case "user/orders/isCompAlrEx":
				isComplainAlreadyExist(request, response);
				break;
			case "user/developers/filter":
				filterDevelopers(request, response);
				return;
			case "user/order":
				getOrderById(request, response);
				break;
			case "user/order/followers":
				getFollowersByOrderId(request, response);
				break;
			case "user/order/techs":
				getOrderTechs(request, response);
				break;
			case "user/order/subscribe":
				subscribe(request, response);
				break;
			case "user/order/unsubscribe":
				unsubscribe(request, response);
				break;
			case "user/type":
				getUserById(request, response);
				break;
			case "user/customer/history":
				getCustomerHistory(request, response);
				break;
			case "user/getAvailableCustOrders":
				getAvailableCustOrders(request, response);
				break;
			case "user/customer/feedbacks":
				getFeedbacksByIdForCust(request, response);
				break;
			case "user/contact":
				getUserContact(request, response);
				break;
			case "user/getTestByDevId":
				getTestByDevId(request, response);
				break;
			case "user/orders/complain":
				complain(request, response);
				break;
			case "user/getRate":
				getRate(request, response);
				return;
			case "user/confirmEmail":
				confirmEmail(request, response);
				break;
			case "user/confirmPhoneCode":
				confirmPhoneCode(request, response);
				break;
			case "user/changePassword":
				changePassword(request, response);
				break;
			case "user/deleteFeed":
				deleteFeed(request, response);
				return;
			case "user/uploadImage":
				uploadImage(request, response);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	private void uploadImage(HttpServletRequest request,
			HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		String imageJson = request.getParameter("image");
		byte[] encodeImage = Base64.decodeBase64(imageJson);
		File file = null;
		try {
			String applicationPath = request.getServletContext()
					.getRealPath("");
			String uploadFilePath = null;
			if ("developer".equals(ue.getRole())) {
				uploadFilePath = applicationPath + "uploads" + File.separator
						+ "developer" + File.separator + ue.getId();
				saveImage(uploadFilePath, encodeImage, response);
				Developer developer = (Developer) ue;
				developer.setImgUrl("uploads/developer/" + ue.getId() + "/");
				developerService.updateDeveloper(developer);
			}
			if ("customer".equals(ue.getRole())) {
				uploadFilePath = applicationPath + "uploads" + File.separator
						+ "customer" + File.separator + ue.getId();
				saveImage(uploadFilePath, encodeImage, response);
				Customer customer = (Customer) ue;
				customer.setImgUrl("uploads/customer/" + ue.getId() + "/");
				customerService.modify(customer);
			}
			if ("admin".equals(ue.getRole())) {
				uploadFilePath = applicationPath + "uploads" + File.separator
						+ "admin" + File.separator + ue.getId();
				saveImage(uploadFilePath, encodeImage, response);
				Admin admin = (Admin) ue;
				admin.setImgUrl("uploads/admin/" + ue.getId() + "/");
				adminService.modify(admin);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveImage(String uploadFilePath, byte[] encodImage,
			HttpServletResponse response) throws IOException
	{
		File file = new File(uploadFilePath + File.separator + "original"
				+ ".jpg");
		try {
			FileUtils.writeByteArrayToFile(file, encodImage);
			ImageResize.resizeImage(uploadFilePath + File.separator
					+ "original.jpg", uploadFilePath + File.separator
					+ "sm.jpg", 100, 100);
			ImageResize.resizeImage(uploadFilePath + File.separator
					+ "original.jpg", uploadFilePath + File.separator
					+ "md.jpg", 200, 200);
			ImageResize.resizeImage(uploadFilePath + File.separator
					+ "original.jpg", uploadFilePath + File.separator
					+ "lg.jpg", 500, 500);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	public void deleteFeed(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String role = request.getParameter("role");
		if (role != null) {
			if ("developer".equals(role)) {
				FeedbackService feedbackService = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				String devId = request.getParameter("devId");
				String feedId = request.getParameter("feedId");

				if (feedId != null && devId != null) {
					try {
						int res = feedbackService.deleteDevFeed(
								Integer.parseInt(devId),
								Integer.parseInt(feedId));
						if (res == 0)
							response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					} catch (Exception e) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					}
				}
			}
			if ("customer".equals(role)) {
				FeedbackService feedbackService = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				String custId = request.getParameter("custId");
				String feedId = request.getParameter("feedId");

				if (feedId != null && custId != null) {
					try {
						int res = feedbackService.deleteCustFeed(
								Integer.parseInt(custId),
								Integer.parseInt(feedId));
						if (res == 0)
							response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					} catch (Exception e) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					}
				}
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	public void getRate(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				FeedbackService fs = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				Integer avg = fs.getAvgRate(id);
				sendResponse(response, avg, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
		}
	}

	public void getRateForCust(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Feedback> feedbacks = feedbackService
						.findFeedbacksByCustId(id);
				Integer rate = 0;
				for (Feedback f : feedbacks) {
					rate += f.getRate();
				}

				rate = rate / feedbacks.size();
				sendResponse(response, rate, mapper);

			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	private void complain(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		HttpSession session = request.getSession();
		if (session != null) {
			UserEntity ue = (UserEntity) session.getAttribute("user");
			if (ue != null) {
				String param = request.getParameter("orderID");
				if (param != null) {
					try {
						Complaint complaint = new Complaint();
						complaint.setOrderId(Integer.parseInt(param));
						complaint.setDevId(ue.getId());
						complaintService.save(complaint);
					} catch (Exception e) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					}
				} else
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} else
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void isComplainAlreadyExist(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String orderId = request.getParameter("orderId");
		HttpSession session = request.getSession();

		if (session != null) {
			UserEntity ue = (UserEntity) session.getAttribute("user");
			if (ue != null) {
				if (ue.getId() != null && orderId != null) {
					try {
						if (complaintService.isAlreadyExist(ue.getId(),
								Integer.parseInt(orderId)) == null)
						{
							response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						}
					} catch (Exception e) {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST);
					}
				} else
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} else
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}

	private void filterOrders(HttpServletRequest request,
			HttpServletResponse response)
	{
		try {
			JsonPaginator result = mapper.readValue(request.getReader()
					.readLine(), JsonPaginator.class);
			List<Ordering> orderings = orderingService.filterElements(result
					.getContent(), result.getPage().getStart()
					* result.getPage().getStep(), result.getPage().getStep());

			addIsComplaintInOrderings(orderings, request);

			for (Ordering ordering : orderings) {
				ordering.setTechnologies(orderingService
						.findOrderingTechnologies(ordering.getId()));
			}

			paginator.next(result.getPage(), response, orderingService
					.getFilteredObjectNumber(result.getContent()), orderings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addIsComplaintInOrderings(List<Ordering> orderings,
			HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		if (session != null) {
			UserEntity ue = (UserEntity) session.getAttribute("user");
			if (ue != null) {
				String role = ue.getRole();
				if ("developer".equals(role)) {
					List<Complaint> complaints = complaintService.getByDevId(ue
							.getId());
					for (Complaint c : complaints) {
						for (Ordering o : orderings) {
							if (c.getOrderId().equals(o.getId())) {
								o.setIsComplaint(true);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void filterDevelopers(HttpServletRequest request,
			HttpServletResponse response)
	{
		try {
			JsonPaginator result = mapper.readValue(request.getReader()
					.readLine(), JsonPaginator.class);
			List<Developer> developers = developerService.filterElements(result
					.getContent(), result.getPage().getStart()
					* result.getPage().getStep(), result.getPage().getStep());

			for (Developer developer : developers) {
				developer.setTechnologies(developerService
						.getTechnologiesByDevId(developer.getId()));
				developer.setEmail(null);
				developer.setPassword(null);
				developer.setUuid(null);
				developer.setSalt(null);
				developer.setRegUrl(null);
			}

			paginator.next(result.getPage(), response, developerService
					.getFilteredObjectNumber(result.getContent()), developers);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "logout");
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
				"user");
		AuthenticationProvider authenticationProvider = (AuthenticationProvider) ApplicationContext
				.getInstance().getBean("authenticationProvider");
		authenticationProvider.invalidateUserCookie(response,
				"freelancerRememberMeCookie", userEntity);
		if (userEntity != null) {
			request.getSession().invalidate();
		}
	}

	public void isAuth(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue != null) {
			sendResponse(response, ue, mapper);
		} else {
			response.sendError(500);
			return;
		}
	}

	public void sendSmsAndFollowOrHire(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String phone = request.getParameter("phone");
		String author = request.getParameter("author");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");

		if (ue == null || phone == null || author == null) {
			response.sendError(303);
			return;
		}
		String sms = null;

		if ("dev".equals(author)) {
			sms = "This freelancer, " + ue.getFname() + " " + ue.getLname()
					+ ", followed you. See details in your cabinet.";

		} else
			sms = "This customer, " + ue.getFname() + " " + ue.getLname()
					+ ", would like to hire you. See details in your cabinet.";

		String[] str = new SmsSender().sendSms(phone, sms, ue.getFname());

		Follower f = null;
		if ("customer".equals(author)) {
			f = developerService.createFollowing(request.getParameterMap());
		} else {
			f = customerService.hireDeveloper(request.getParameterMap());
		}

		if (f == null) {
			response.sendError(303);
		}
	}

	public void comment(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String author = request.getParameter("role");
		String rate = request.getParameter("rate");
		String dev_id = null;
		String cust_id = null;
		if (author.equals("customer"))
			dev_id = request.getParameter("id");
		else
			cust_id = request.getParameter("id");

		String comment = request.getParameter("comment");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(404);
			return;
		}

		if (comment == null || rate == null) {
			response.sendError(500);
			return;
		}

		if ("".equals(comment)) {
			response.sendError(500);
			return;
		}

		if (author.equals("customer"))
			cust_id = ue.getId().toString();
		else
			dev_id = ue.getId().toString();

		if ("".equals(dev_id) || "".equals(cust_id) || "".equals(comment)
				|| "".equals(rate) || "".equals(author))
		{
			response.sendError(500);
			return;
		}
		if (comment.contains("<")) {
			response.sendError(500);
			return;
		}
		FeedbackService feedbackService = (FeedbackService) ApplicationContext
				.getInstance().getBean("feedbackService");
		Map<String, String[]> map = new HashMap<>();
		map.put("dev_id", new String[] { dev_id });
		map.put("cust_id", new String[] { cust_id });
		map.put("comment", new String[] { comment });
		map.put("rate", new String[] { rate });
		map.put("author", new String[] { author });

		feedbackService.create(map);
	}

	public void send(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String email = request.getParameter("email");
		String message = request.getParameter("message");
		String subject = request.getParameter("subject");

		if (email == null || message == null) {
			response.sendError(304);
			return;
		}

		if ("undefined".equals(email) || "undefined".equals(message)) {
			response.sendError(304);
			return;
		}

		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");

		if (ue == null) {
			response.sendError(304);
			return;
		}

		String fromAdmin = "onlineshopjava@gmail.com";
		String fromAdminPass = "ForTestOnly";
		String[] to = new String[] { email };

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean res = false;
				try {
					res = SendMessageToEmail.sendFromGMail(fromAdmin,
							fromAdminPass, to, subject, message);
					if (!res) {
						response.sendError(304);
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void getById(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				Developer developer = ds.findById(id);

				if (developer != null) {
					developer.setPassword(null);
					sendResponse(response, developer, mapper);
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

	public void getTechById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				List<Technology> list = ds.getTechnologiesByDevId(id);
				if (list != null)
					sendResponse(response, list, mapper);
				else
					response.sendError(404);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
			return;
		}
	}

	public void getContById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				Contact contact = ds.getContactByDevId(id);
				if (contact != null) {
					sendResponse(response, contact, mapper);
				} else {
					response.sendError(404);
				}

			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.setStatus(404);
		}
	}

	public void getPortfolioById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				DeveloperService ds = (DeveloperService) ApplicationContext
						.getInstance().getBean("developerService");
				OrderingService orderingService = (OrderingService) ApplicationContext
						.getInstance().getBean("orderingService");
				List<Ordering> orderings = ds.getDeveloperPortfolio(id);
				for (Ordering ordering : orderings) {
					ordering.setTechnologies(orderingService
							.findOrderingTechnologies(ordering.getId()));
				}
				if (orderings != null) {
					sendResponse(response, orderings, mapper);
				} else {
					response.sendError(500);
				}
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	public void getFeedbackByIdForDev(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				FeedbackService fs = (FeedbackService) ApplicationContext
						.getInstance().getBean("feedbackService");
				List<Feedback> feedbacks = fs.findFeedbacksByDevIdForHim(id);
				CustomerService customerService = (CustomerService) ApplicationContext
						.getInstance().getBean("customerService");
				for (Feedback f : feedbacks) {
					Customer customer = customerService.findById(f
							.getCustomerId());
					customer.setPassword(null);
					customer.setSalt(null);
					f.setCustomer(customer);
				}
				Collections.reverse(feedbacks);
				sendResponse(response, feedbacks, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		} else {
			response.sendError(404);
		}
	}

	private void create(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		String requestData = request.getReader().readLine();
		Map<String, String> data = mapper.readValue(requestData,
				new TypeReference<Map<String, String>>() {
				});
		String role = data.get("role");
		if (role == null || role.isEmpty()) {
			response.sendError(HttpServletResponse.SC_MULTIPLE_CHOICES);
			return;
		}

		if (!userManager.isEmailAvailable(data.get("email"))) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		try {
			userManager
					.createUser(
							data.entrySet()
									.stream()
									.collect(
                                            Collectors.toMap(Map.Entry::getKey,
                                                    e -> new String[]{e
                                                            .getValue()})),
							role);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setStatus(200);
	}

	private void signIn(HttpServletRequest request,
			HttpServletResponse response, SignInType type)
			throws ServletException, IOException
	{
		boolean remember = "true".equals(request.getParameter("remember"));
		String email = null;
		switch (type) {
		case MANUAL:
			email = request.getParameter("email");
			break;
		case LINKEDIN:
			try {
				email = getLinkedInProfile(request, response).getEmailAddress();
			} catch (OAuthException e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_CONFLICT);
			}
			break;
		case GOOGLE:
			break;
		}
		if (email == null || "".equals(email)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		UserEntity userEntity = userManager.findUserByEmail(email);
		if (userEntity == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid credentials");
			return;
		}

		String password = request.getParameter("password");
		switch (type) {
		case MANUAL:
			if (password == null
					|| !userManager.validCredentials(email, password,
							userEntity))
			{
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid credentials");
				return;
			}
		case LINKEDIN:
		case GOOGLE:
			request.getSession().setAttribute("user", userEntity);
			userEntity.setRole(getRole(userEntity));

			if (remember)
				authenticationProvider.loginAndRemember(response,
						"freelancerRememberMeCookie", userEntity);
			else
				authenticationProvider.invalidateUserCookie(response,
						"freelancerRememberMeCookie", userEntity);

			sendResponse(response, userEntity, mapper);
		}
	}

	private String getRole(UserEntity userEntity) {
		String result = null;
		if (userEntity instanceof Developer)
			result = "developer";
		if (userEntity instanceof Customer)
			result = "customer";
		if (userEntity instanceof Admin)
			result = "admin";
		return result;
	}

	private void getOrderById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer orderId = Integer.parseInt(param);
				Ordering order = orderingService.findById(orderId);
				sendResponse(response, order, mapper);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getFollowersByOrderId(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer orderId = Integer.parseInt(param);
				List<Follower> followers = orderingService
						.findOrderFollowers(orderId);
				followers.forEach(follower -> {
					follower.setDeveloper(developerService.findById(follower
							.getDevId()));
					follower.getDeveloper().setPassword(null);
					follower.getDeveloper().setSalt(null);
				});
				sendResponse(response, followers, mapper);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getCustomerById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("custId");
		if (param != null) {
			try {
				Integer custId = Integer.parseInt(param);
				Customer customer = customerService.findById(custId);
				customer.setPassword(null);
				customer.setSalt(null);
				sendResponse(response, customer, mapper);
			} catch (Exception e) {
				response.sendError(500);
			}
		}
	}

	private void getOrderTechs(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("orderId");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Technology> technologies = orderingService
						.findOrderingTechnologies(id);
				sendResponse(response, technologies, mapper);

			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void getCustomerHistory(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("custId");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Ordering> orders = customerService
						.getProjectsPublicHistory(id);
				for (Ordering ordering : orders) {
					ordering.setTechnologies(orderingService
							.findOrderingTechnologies(ordering.getId()));
				}
				if (orders != null) {
					sendResponse(response, orders, mapper);
				} else {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	private void subscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String order_id = request.getParameter("orderId");
		String message = request.getParameter("message");
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		if (ue == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		if (order_id == null || order_id.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Integer orderId;
		try {
			orderId = Integer.parseInt(order_id);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		Follower follower = developerService.subscribeOnProject(ue.getId(),
				orderId, message);
		follower.setDeveloper((Developer) ue);
		sendResponse(response, follower, mapper);

	}

	private void unsubscribe(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String follower_id = request.getParameter("followerId");

		try {
			developerService.unsubscribeFromProject(Integer
					.parseInt(follower_id));
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

	}

	private void getUserById(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String paramId = request.getParameter("id");
		String role = request.getParameter("role");

		if (paramId == null || "".equals(paramId)) {
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		Integer id = Integer.parseInt(paramId);

		try {
			UserEntity userEntity = null;
			switch (role) {
			case "admin":
				userEntity = adminService.findById(id);
				break;
			case "dev":
				userEntity = developerService.findById(id);
				break;
			case "customer":
				userEntity = customerService.findById(id);
				break;
			}
			if (userEntity != null) {
				userEntity.setPassword(null);
				userEntity.setEmail(null);
				userEntity.setSalt(null);
				userEntity.setUuid(null);
				userEntity.setRegUrl(null);

				sendResponse(response, userEntity, mapper);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void getUserContact(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String paramId = request.getParameter("id");
		String role = request.getParameter("role");

		if (paramId == null || "".equals(paramId) || !paramId.matches("[0-9]"))
		{
			response.sendError(HttpServletResponse.SC_CONFLICT);
			return;
		}
		Integer id = Integer.parseInt(paramId);

		try {
			Contact contact = null;
			switch (role) {
			case "dev":
				contact = developerService.getContactByDevId(id);
				break;
			case "customer":
				contact = customerService.getContactByCustomerId(id);
				break;
			}
			if (contact != null) {
				contact.setPhone(null);
				contact.setVersion(null);
				contact.setDeleted(null);

				sendResponse(response, contact, mapper);
			} else
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	private void getFeedbacksByIdForCust(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String param = request.getParameter("id");
		if (param != null) {
			try {
				Integer id = Integer.parseInt(param);
				List<Feedback> feedbacks = feedbackService
						.findFeedbacksByCustIdForHim(id);
				for (Feedback f : feedbacks) {
					f.setDeveloper(developerService.findById(f.getDevId()));
				}
				Collections.reverse(feedbacks);

				sendResponse(response, feedbacks, mapper);

			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
	}

	public void getAvailableCustOrders(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		HttpSession session = request.getSession();
		UserEntity ue = (UserEntity) session.getAttribute("user");
		String from = request.getParameter("from");

		if (ue == null) {
			response.setStatus(304);
			return;
		}

		if (!"dev".equals(from)) {
			OrderingService os = (OrderingService) ApplicationContext
					.getInstance().getBean("orderingService");
			List<Ordering> orderings = os.getAvailableCustOrders(ue.getId());

			sendResponse(response, orderings, mapper);
		} else {
			String custId = request.getParameter("id");
			OrderingService os = (OrderingService) ApplicationContext
					.getInstance().getBean("orderingService");
			List<Ordering> orderings = os.getAvailableCustOrders(Integer
					.parseInt(custId));

			sendResponse(response, orderings, mapper);
		}
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

	private void confirmEmail(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String email = request.getParameter("email");
		UserEntity userEntity = userManager.findUserByEmail(email);

		if (userEntity != null) {
			StringBuilder confirmPhoneCode = new StringBuilder();
			SecureRandom random = new SecureRandom();
			for (int i = 0; i < 4; i++) {
				confirmPhoneCode.append(String.valueOf(random.nextInt(9)));
			}
			userEntity.setConfirmCode(confirmPhoneCode.toString());
			try {
				String phoneNumber = null;
				if (userEntity instanceof Developer) {
					phoneNumber = developerService.getContactByDevId(
							userEntity.getId()).getPhone();
					Developer developer = (Developer) userEntity;
					developerService.updateDeveloper(developer);
				}
				if (userEntity instanceof Customer) {
					phoneNumber = customerService.getContactByCustomerId(
							userEntity.getId()).getPhone();
					Customer customer = (Customer) userEntity;
					customerService.modify(customer);
				}
				SmsSender smsSender = new SmsSender();
				smsSender.sendSms(phoneNumber, "Confirm code: "
						+ confirmPhoneCode.toString(), "e-freelance");
			} catch (NullPointerException e) {
				LOG.warn("The phone number is empty");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid email");
				response.flushBuffer();
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid email");
			response.flushBuffer();
			return;
		}
	}

	public void confirmPhoneCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String email = request.getParameter("email");
		String confirmPhoneCode = request.getParameter("phoneCode");
		UserEntity userEntity = userManager.findUserByEmail(email);

		if (userEntity != null) {
			String realPhoneCode = userEntity.getConfirmCode();
			try {
				if (realPhoneCode.equals(confirmPhoneCode)) {
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.getWriter().write(new Gson().toJson("good"));
				} else {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Invalid Phone Code");
					response.flushBuffer();
					return;
				}
			} catch (NullPointerException e) {
				LOG.warn("Some problem with Phone Code");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid Phone Code");
				response.flushBuffer();
				return;
			}

		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid email");
			response.flushBuffer();
			return;
		}
	}

	private void changePassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		UserEntity userEntity = userManager.findUserByEmail(email);

		if (userEntity != null) {
			userEntity.setPassword(password);
			if (userEntity instanceof Developer) {
				Developer developer = (Developer) userEntity;
				developerService.encodePassword(developer);
				developerService.updateDeveloper(developer);
			}
			if (userEntity instanceof Customer) {
				Customer customer = (Customer) userEntity;
				customerService.encodePassword(customer);
				customerService.modify(customer);
			}
		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"Invalid email");
			response.flushBuffer();
			return;
		}
	}

}

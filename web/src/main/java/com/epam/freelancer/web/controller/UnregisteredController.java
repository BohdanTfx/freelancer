package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.database.model.Admin;
import com.epam.freelancer.database.model.Customer;
import com.epam.freelancer.database.model.Developer;
import com.epam.freelancer.database.model.UserEntity;
import com.epam.freelancer.security.provider.AuthenticationProvider;
import com.epam.freelancer.web.social.Linkedin;
import com.epam.freelancer.web.social.model.LinkedinProfile;
import com.epam.freelancer.web.util.SignInType;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.scribe.exceptions.OAuthException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UnregisteredController extends HttpServlet implements Responsable {
	private final static Logger LOG = Logger
			.getLogger(UnregisteredController.class);
	private static final long serialVersionUID = 1L;
	private AuthenticationProvider authenticationProvider;
	private UserManager userManager;
	private Linkedin linkedin;
	private ObjectMapper mapper;

	public UnregisteredController() {
		init();
	}

	@Override
	public void init() {
		LOG.info(getClass().getSimpleName() + " - " + " loaded");
		linkedin = new Linkedin();
		mapper = new ObjectMapper();
		try {
			linkedin.initKeys("/social.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		userManager = (UserManager) ApplicationContext.getInstance().getBean(
				"userManager");
		authenticationProvider = AuthenticationProvider
				.createAuthenticationProvider();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "doGet");
		try {
			switch (FrontController.getPath(request)) {
			case "unreg/language/bundle":
				sendBundle(request, response);
				return;
			case "unreg/email":
				sendResponse(response, userManager.isEmailAvailable(request
						.getParameter("email")), mapper);
				return;
			case "unreg/social":
				configSocials(request, response);
				return;
			case "unreg/signup/linkedin":
				sendResponse(response, getLinkedInProfile(request, response),
						mapper);
				return;
			case "unreg/signin/linkedin":
				signIn(request, response, SignInType.LINKEDIN);
				return;
			case "unreg/authentication/auto":
				autoAuthenticateUser(request, response);
				return;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void configSocials(HttpServletRequest request,
			HttpServletResponse response)
	{
		String callbackUrl = request.getParameter("callbackUrlLinkedIn");
		Map<String, Object> result = new HashMap<>();
		result.put("linkedinUrl", linkedin.getAuthentificationUrl(callbackUrl));
		sendResponse(response, result, mapper);
	}

	private void autoAuthenticateUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException
	{
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
				"user");
		if (userEntity == null)
			userEntity = authenticationProvider.isAutoAuthenticationEnable(
					"freelancerRememberMeCookie", userManager, request);

		if (userEntity == null) {
			sendResponse(response, false, mapper);
			return;
		}

		request.getSession().setAttribute("user", userEntity);
		userEntity.setRole(getRole(userEntity));
		sendResponse(response, userEntity, mapper);
	}

	private void sendBundle(HttpServletRequest request,
			HttpServletResponse response)
	{
		LOG.info(getClass().getSimpleName() + " - " + "sendBundle");
		Locale locale = ((Locale) request.getSession().getAttribute("language"));
		ResourceBundle bundle = null;
		if (locale != null)
			bundle = ResourceBundle.getBundle("/i18n/language", locale);
		else
			bundle = ResourceBundle.getBundle("/i18n/language", Locale.ENGLISH);

		Map<String, String> bundleMap = new HashMap<>();

		for (String key : bundle.keySet()) {
			String value = bundle.getString(key);
			bundleMap.put(key, value);
		}

		sendResponse(response, bundleMap, mapper);
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

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			switch (FrontController.getPath(request)) {
			case "unreg/signin":
				signIn(request, response, SignInType.MANUAL);
				return;
				case "unreg/signin/google":
					signInGoogle(request, response);
					return;
			case "unreg/create":
				create(request, response);
				return;
			case "unreg/logout":
				logout(request, response);
				return;
			case "unreg/confirm/email":
				confirmEmailAfterRegistration(request, response);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		LOG.info(getClass().getSimpleName() + " - " + "logout");
		UserEntity userEntity = (UserEntity) request.getSession().getAttribute(
				"user");
		authenticationProvider.invalidateUserCookie(response,
				"freelancerRememberMeCookie", userEntity);
		if (userEntity != null) {
			request.getSession().invalidate();
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
													e -> new String[] { e
															.getValue() })),
							role);
			new Thread(new Runnable() {
				@Override
				public void run(){
					try {
						sendConfirmationToEmail(request,response,userManager.findUserByEmail(data.get("email")));
					} catch (IOException e) {
						e.printStackTrace();
						try {
							response.sendError(HttpServletResponse.SC_BAD_REQUEST);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		response.setStatus(200);
	}

	private void sendConfirmationToEmail(HttpServletRequest request, HttpServletResponse response,UserEntity entity) throws IOException{
		String arrEmail[] = {entity.getEmail()};
		String confirmLink = "http://localhost:8081/#/auth?confirmCode=" + entity.getRegUrl()+"&uuid="+entity.getUuid();
		SendMessageToEmail.sendHtmlFromGMail("onlineshopjava@gmail.com", "ForTestOnly", arrEmail, "OpenTask -  E-mail Confirmation ",
				getConfirmationEmailMessage(confirmLink, entity.getFname()));
	}

	private void signInGoogle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		signIn(request, response, SignInType.GOOGLE);
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
				return;
			}
			break;
		case GOOGLE:
			email = request.getParameter("email");
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
		if (userEntity.getRegUrl() != null) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
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
			userManager.modifyUser(userEntity);

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

	private void confirmEmailAfterRegistration(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String confirmCode = request.getParameter("confirmCode");
		String uuid = request.getParameter("uuid");

		if(uuid==null || confirmCode == null || uuid.equals("") || confirmCode.equals("")){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		UserEntity entity = userManager.findUserByUUID(uuid);
		if(entity == null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}else{
			if(entity.getRegUrl() != null && entity.getRegUrl().equals(confirmCode)){
				entity.setRegUrl(null);
				userManager.modifyUser(entity);
				sendResponse(response,true,mapper);
			}else{
				sendResponse(response,false,mapper);
			}
		}
	}

	private String getConfirmationEmailMessage(String link,String userName){
		return "Hello " +userName+"\n"+
				"You have to confirm your email. Please check this url "+link;
	}

}

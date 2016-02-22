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
		return "<div style=\"background-color:#dfdfdf;padding:0;margin:0 auto;width:100%\"> " +
				"<span style=\"display:none!important;font-size:1px;color:transparent;min-height:0;width:0\">Info about changing your password</span>" +
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
				" <td>To confirm your email, click <a style=\"text-decoration:none;color:#0077b5\" href=\""+link+"\" target=\"_blank\">here</a>" +
				" or paste the following link into your browser: <a style=\"text-decoration:none;color:#0077b5\" href=\""+link+"\" target=\"_blank\">" +
				" "+link+" </a> </td></tr><tr> <td> <table border=\"0\" cellpadding=\"1\" cellspacing=\"0\" width=\"1\">" +
				" <tbody> <tr> <td> <div style=\"min-height:20px;font-size:20px;line-height:20px\"> &nbsp; </div></td></tr></tbody> </table> </td></tr><tr> " +
				"<td>The link will expire <span tabindex=\"0\" class=\"aBn\" data-term=\"goog_1735888483\"><span class=\"aQJ\">in 24 hours</span></span>," +
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
				"<tbody> <tr> <td dir=\"ltr\" align=\"center\">� 2016 www.opentask.com | All rights reserved.</td></tr><tr> <td> <table border=\"0\" cellpadding=\"0\"" +
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

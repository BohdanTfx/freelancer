package com.epam.freelancer.web.social;

import com.epam.freelancer.web.social.model.LinkedinProfile;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Linkedin {
	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~"
			+ ":(id,first-name,last-name,email-address,location,"
			+ "picture-url)?format=json";
	private String publicKey;
	private String secretKey;
	private OAuthService service;
	private LinkedinProfile profile;
	private Token requestToken;

	public void initKeys(String propertiesPath) throws IOException {
		Properties properties = new Properties();
		try (InputStream file = Linkedin.class
				.getResourceAsStream(propertiesPath)) {
			properties.load(file);

			publicKey = properties.getProperty("LinkedinClientID");
			secretKey = properties.getProperty("LinkedinClientSecret");
		}
	}

	public String getAuthentificationUrl(String callbackPage) {
		service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(publicKey).apiSecret(secretKey).callback(callbackPage)
				.build();
		requestToken = service.getRequestToken();
		return service.getAuthorizationUrl(requestToken);
	}

	public void loadData(String value) throws
			IOException
	{
		Verifier verifier = new Verifier(value);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();

		profile = new ObjectMapper().readValue(response.getBody(),
				LinkedinProfile.class);
	}

	public LinkedinProfile getProfile() {
		return profile;
	}
}

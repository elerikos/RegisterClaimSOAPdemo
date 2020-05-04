package ey.client.rest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ey.client.rest.insis.claims.model.Fnol;
import ey.demo.Logger;
import ey.demo.RestClient;

public class InsisRestConsumer {

	private static String INSIS_REST_BASE_URI = "http://10.3.4.148:7809/insis";
	private final static String INSIS_REST_LOGIN_URI = "/nonfunctional/authentication/login";
	private final static String INSIS_REST_GET_CLAIM = "/claim-data/c-claim-type/";
	private final static String INSIS_FNOL_URI = "/claims-cst/fnol";
	private final static String NAME = "name";
	private final static String VALUE = "value";
	private final static String USERNAME = "username";
	private final static String PASSWORD = "password";
	private final static String COOKIE_HEADER_SEPARATOR = ";";
	private final static String COOKIE_NAME_VALUE_SEPARATOR = "=";
	private ObjectMapper mapper;
	
	public InsisRestConsumer() {
		this.mapper = new ObjectMapper();

	}
	
	/**
	 * Invokes /nonfunctional/authentication/login and returns the name and the value of a cookie in a Map
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String,String> getCookie(String username, String password) throws Exception {
		Map<String,String> cookieValues = new HashMap<String,String>();
		Client client = ClientBuilder.newClient(new ClientConfig());
		WebTarget webTarget = client.target(INSIS_REST_BASE_URI).path(INSIS_REST_LOGIN_URI);
		
		Logger.log("[EXEC]" + " POST invocation of " + webTarget.getUri().toString());
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(this.credentialsForLogin(username, password), MediaType.APPLICATION_FORM_URLENCODED));
		
		Logger.log("[EXEC]" + " Received " + response.getStatus() + " from " + webTarget.getUri().toString() + " with payload: " + response.readEntity(String.class));

		if(response.getStatus() == Status.OK.getStatusCode()) {
			
			cookieValues = this.fromCookieHeader(response.getHeaders().get(HttpHeaders.SET_COOKIE).iterator());
		}else {
			throw new RuntimeException("HTTP Return Code:" + response.getStatus());
		}
		return cookieValues;
	}
		
	/**
	 * Invokes /claims-cst/fnol to register the given fnol
	 * @param fnol
	 * @param cookie
	 * @throws JsonProcessingException
	 */
	public void registerFnol(Fnol fnol, Map<String,String> cookie) throws Exception {
		Client client = ClientBuilder.newClient(new ClientConfig());
		WebTarget webTarget = client.target(INSIS_REST_BASE_URI).path(INSIS_FNOL_URI);
		
		Logger.log("[EXEC]" + " POST invocation of " + webTarget.getUri().toString());
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).cookie(cookie.get(NAME),cookie.get(VALUE)).post(Entity.entity(mapper.writeValueAsString(fnol), MediaType.APPLICATION_JSON));
		
		RestClient.log("[EXEC]" + " Received " + response.getStatus() + " from " + webTarget.getUri().toString() + " with payload: " + response.readEntity(String.class));
	}
	
	/**
	 * Invokes /claim-data/c-claim-type/ to get the claim for the given id 
	 * @param claimId
	 * @param cookie
	 */
	public void getClaim(String claimId, Map<String,String> cookie) {
		Client client = ClientBuilder.newClient(new ClientConfig());
		WebTarget webTarget = client.target(INSIS_REST_BASE_URI).path(INSIS_REST_GET_CLAIM + claimId);
		
		Logger.log("[EXEC]" + " POST invocation of " + webTarget.getUri().toString());
		
		Response response = webTarget.request(MediaType.APPLICATION_JSON).cookie(cookie.get(NAME),cookie.get(VALUE)).get();
	
		Logger.log("[EXEC]" + " Received " + response.getStatus() + " from " + webTarget.getUri().toString() + " with payload: " + response.readEntity(String.class));
					
	}
	
	/**
	 * Cookie format expected is [Insis11Session=NBG2I67oRYZafM9tkWv_MEHuGjEXjq5A5yqxWuLt4wTJNeKlVgaF!534377255; path=/; HttpOnly]
	 * @param cookieIterator
	 * @return
	 */
	private Map<String,String> fromCookieHeader(Iterator<Object> cookieIterator) {
		Map<String,String> cookieValues = new HashMap<String,String>();
		while(cookieIterator.hasNext()) {
			StringTokenizer cookieHeaderTokenizer = new StringTokenizer((String)cookieIterator.next(),COOKIE_HEADER_SEPARATOR);
			StringTokenizer cookieTokenizer = new StringTokenizer(cookieHeaderTokenizer.nextToken(),COOKIE_NAME_VALUE_SEPARATOR);
			int cookieTokensAmount = cookieTokenizer.countTokens();
			for(int i = 0 ; i < cookieTokensAmount ; i++) {
			    cookieValues.put((i == 0 ? NAME : VALUE), cookieTokenizer.nextToken());
			}
		}
		return cookieValues;
	}

	/**
	 * Builds credentials as an application/x-www-form-urlencoded String
	 * @param username
	 * @param password
	 * @return
	 */
	private String credentialsForLogin(String username, String password) {
		return USERNAME + "=" + username + "&" + PASSWORD + "=" + password;
	}

	public static void setHOST_PORT(String tmp) {
		INSIS_REST_BASE_URI = "http://"+ tmp +"/insis";
	}
	
	
	
}


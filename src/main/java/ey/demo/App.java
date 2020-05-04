package ey.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import ey.client.rest.InsisRestConsumer;
import ey.client.rest.insis.claims.model.Fnol;

public class App {

	private final static String INSIS_HLT_V10 = "insis_hlt_v10";
	
	public static void main(String[] args) {
		try {
			List<Fnol> fnols = new ArrayList<Fnol>();
			InsisRestConsumer restConsumer = new InsisRestConsumer();
			Map<String,String> cookie = restConsumer.getCookie(INSIS_HLT_V10, INSIS_HLT_V10);
			for(Fnol fnol : fnols) {
				restConsumer.registerFnol(fnol, cookie);
			}
		} 
		catch(JsonProcessingException ex) {
			System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
		catch(IOException ex) {
			System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
		catch(Exception ex) {
			System.out.println("[ERROR]" + " " + ex.getClass() + " with message: " + ex.getMessage());
		}
	}
	
}

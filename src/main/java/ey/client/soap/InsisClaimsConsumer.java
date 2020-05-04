package ey.client.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.ws.rs.core.Response.Status;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ey.demo.Logger;
import ey.demo.RestClient;

public class InsisClaimsConsumer {
	
	private static String INSIS_CSM_PORT_ENDPOINT = "http://10.3.4.148:7808/insisws/InsisCSMPort";
	private final static String ENVELOPE_HEADER = "<soapenv:Header>" + "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" + "<wsse:UsernameToken wsu:Id=\"UsernameToken-1\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" + "<wsse:Username>insis_hlt_v10</wsse:Username>" + "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">insis_hlt_v10</wsse:Password>" + "</wsse:UsernameToken>" + "</wsse:Security>" + "</soapenv:Header>";
	private final static String ENVELOPE_OPEN = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:x=\"http://www.fadata.bg/Insurance_Messages/v3.0/xml/\">";
	private final static String ENVELOPE_CLOSE = "</soapenv:Envelope>";
	private final static String BODY_OPEN = "<soapenv:Body>";
	private final static String BODY_CLOSE = "</soapenv:Body>";
	private final static String FIND_CLAIM_HLT_RQ_OPEN = "<x:FindClaimHltRq>";
	private final static String FIND_CLAIM_HLT_RQ_CLOSE = "</x:FindClaimHltRq>";
	private final static String REGISTER_CLAIM_HLT_RQ_OPEN = "<RegisterClaimHltRq isPAR=\"false\">";
	private final static String REGISTER_CLAIM_HLT_RQ_CLOSE = "</RegisterClaimHltRq>";
	private final static String REQUESTS_REQUEST_OPEN = "<Requests><Request>";
	private final static String REQUESTS_REQUEST_CLOSE = "</Request></Requests>";
    private final static String CUSTOM_PROPERTIES_PROPERTY_OPEN = "<CustomProperties><CustomProperty>";
    private final static String CUSTOM_PROPERTIES_PROPERTY_CLOSE = "</CustomProperty></CustomProperties>";
    private final static String INSIS_CLAIM_ID_OPEN = "<insis:ClaimID>";
    private final static String INSIS_CLAIM_ID_CLOSE = "</insis:ClaimID>";
	private final static String CLAIM_ID = "<ClaimID>%s</ClaimID>";
	private final static String CARD_ID = "<CardID>%s</CardID>";
	private final static String CLAIM_STARTED = "<ClaimStarted>%s</ClaimStarted>";
	private final static String EVENT_TYPE = "<EventType>%s</EventType>";
    private final static String EVENT_DATE = "<EventDate>%s</EventDate>";
    private final static String EVENT_COUNTRY = "<EventCountry>%s</EventCountry>";
    private final static String PRIMARY_DIAGNOSIS_ID = "<PrimaryDiagnosisID>%s</PrimaryDiagnosisID>";
    private final static String FIELD_NAME = "<FieldName>SRC_CLAIM</FieldName>";
    private final static String FIELD_VALUE = "<Value>%s</Value>";
    private final static String FIND_CLAIM_HLT_RQ = "FindClaimHltRq";
    private final static String REGISTER_CLAIM_HLT_RQ = "RegisterClaimHltRq";
    
	public String getClaim(String claimId) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		return this.postSoapMessage(ENVELOPE_OPEN + ENVELOPE_HEADER + BODY_OPEN + FIND_CLAIM_HLT_RQ_OPEN + 
				String.format(CLAIM_ID, claimId) + 
				FIND_CLAIM_HLT_RQ_CLOSE + BODY_CLOSE + ENVELOPE_CLOSE, FIND_CLAIM_HLT_RQ);
	}
	
	public String registerClaim(String cardId, String claimStarted, String eventType, String eventDate, String eventCountry, String primaryDiagnosisId, String caseId) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		String serviceResponse = "";
	//	try {
		serviceResponse = this.getClaimId(this.postSoapMessage(ENVELOPE_OPEN + ENVELOPE_HEADER + BODY_OPEN + REGISTER_CLAIM_HLT_RQ_OPEN + 
				String.format(CARD_ID, cardId) +
				String.format(CLAIM_STARTED, claimStarted) +
				String.format(EVENT_TYPE, eventType) +
				String.format(EVENT_DATE, eventDate) +
				String.format(EVENT_COUNTRY, eventCountry) +
				this.prepareClaimCustomProperties(caseId) +
				this.prepareClaimRequests(primaryDiagnosisId) + 
				REGISTER_CLAIM_HLT_RQ_CLOSE + BODY_CLOSE + ENVELOPE_CLOSE, REGISTER_CLAIM_HLT_RQ));
	//	}
	//	catch(IOException ex) {
	//		serviceResponse = ex.getMessage();
	//	}
		return serviceResponse;
	}

	public String updateClaim() throws ParserConfigurationException, SAXException, TransformerException {
		String serviceResponse = "";
		try {
			serviceResponse = this.postSoapMessage("", REGISTER_CLAIM_HLT_RQ);
		}
		catch(IOException ex) {
			serviceResponse = ex.getMessage();
		}
		return serviceResponse;
	}
	
	/**
	 *
	 * Returns created claim's Id from INSIS's response
	 * @param claimPayloadResponse
	 * @return
	 */
	private String getClaimId(String claimPayloadResponse) {
		Logger.log("Response:");
		Logger.log(claimPayloadResponse);
		return claimPayloadResponse.substring(claimPayloadResponse.lastIndexOf(INSIS_CLAIM_ID_OPEN) + 15, claimPayloadResponse.indexOf(INSIS_CLAIM_ID_CLOSE));
	}	
	
	/**
	 * Returns an empty string or an XML element for claim requests based on value of input
	 * @param primaryDiagnosisId
	 * @return
	 */
	private String prepareClaimRequests(String primaryDiagnosisId) {
		String payloadClaimRequests = "";
		if(primaryDiagnosisId != null) {
			payloadClaimRequests = REQUESTS_REQUEST_OPEN + String.format(PRIMARY_DIAGNOSIS_ID, primaryDiagnosisId) + REQUESTS_REQUEST_CLOSE;
		}
		return payloadClaimRequests;
	}
	
	/**
	 * Returns an empty string or an XML element for claim custom properties based on value of input
	 * @param caseId
	 * @return
	 */
	private String prepareClaimCustomProperties(String caseId) {
		String payloadClaimCustomProperties = "";
		if(caseId != null) {
			payloadClaimCustomProperties = CUSTOM_PROPERTIES_PROPERTY_OPEN + FIELD_NAME + String.format(FIELD_VALUE, caseId) + CUSTOM_PROPERTIES_PROPERTY_CLOSE;
		}
		return payloadClaimCustomProperties;
	}
	/**
	 * POSTs a SOAP service's action with the given String as payload input
	 * @param xmlInput
	 * @param soapAction
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	private String postSoapMessage(String xmlInput, String soapAction) throws IOException, ParserConfigurationException, SAXException, TransformerException {
		HttpURLConnection httpUrlConnection = (HttpURLConnection)new URL(INSIS_CSM_PORT_ENDPOINT).openConnection();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byteArrayOutputStream.write(xmlInput.getBytes());
		byte[] bytes = byteArrayOutputStream.toByteArray();
		httpUrlConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
		httpUrlConnection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpUrlConnection.setRequestProperty("SOAPAction", soapAction);
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setDoInput(true);
		Logger.log("[EXEC]" + " Preparing to " + httpUrlConnection.getRequestMethod() + " " + httpUrlConnection.getURL().getHost() + httpUrlConnection.getURL().getPath() +" with XML payload:\n" + this.formatXML(xmlInput));
		OutputStream outputStream = httpUrlConnection.getOutputStream();
		outputStream.write(bytes);
		outputStream.close();
		return this.formatXML(this.getResponsePayload(httpUrlConnection));
	}

	
	private String getResponsePayload(HttpURLConnection httpUrlConnection) throws IOException {
		String responseString = "";
		String outputString = "";
		if(httpUrlConnection.getResponseCode() == Status.OK.getStatusCode()) {
			BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), Charset.forName("UTF-8")));
			while ((responseString = inputBufferedReader.readLine()) != null) {
				outputString = outputString + responseString;
			}
		}
		else if(httpUrlConnection.getResponseCode() == Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			BufferedReader inputBufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getErrorStream(), Charset.forName("UTF-8")));
			while ((responseString = inputBufferedReader.readLine()) != null) {
				outputString = outputString + responseString;
			}
			throw new IOException(outputString);
		}
		return outputString;
	}
	
	/**
	 * Formats String to pretty print of XML
	 * @param unformattedXml
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	private String formatXML(String unformattedXml) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", 3);
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult xmlOutput = new StreamResult(new StringWriter());
		transformer.transform(new DOMSource(parseXmlFile(unformattedXml)), xmlOutput);
		return xmlOutput.getWriter().toString();
	}
	
	/**
	 * Parses an XML String into an XML Document
	 * @param in
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private Document parseXmlFile(String in) throws ParserConfigurationException, SAXException, IOException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(in)));
	}
	
	
	public static void setHOST_PORT(String tmp) {
		INSIS_CSM_PORT_ENDPOINT = "http://"+ tmp +"/insisws/InsisCSMPort";
	}
	
}

package com.gofetch.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

// old imports - glassfish....
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.HttpResponseException;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;

// new GAE imports:
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 *
 * Utility Class to make a GET HTTP connection to the given url and pass the
 * output - GAE friendly version
 *
 * @author Alan Donohoe
 *
 */
public class ConnectionUtil {

	public static final String TAG = "ConnectionUtil"; // used for logging errors
	private static Logger log = Logger.getLogger(TAG);         // used for logging errors
	private static String errorMsg;                            // used for logging errorsÏ

	/**
	 *
	 * Method to make a GET HTTP connection to the given url and return the output
	 *
	 * @param urlToFetch url to be connected
	 * @return the http get response
	 * @throws IOException 
	 */
	public static String makeRequest(String urlToFetch) throws IOException {
		String responseBody = "";

		// new implementation - GAE version:

		try {

			URL url = new URL(urlToFetch);

			///////
			// 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				responseBody += line;
			}

			reader.close();

			/////////
			// this works - but there's a timeout from SEOMoz server....
			//            BufferedReader in = new BufferedReader(
			//            new InputStreamReader(url.openStream()));
			//
			//            String inputLine;
			//            while ((inputLine = in.readLine()) != null)
			//            	responseBody += inputLine;
			//            in.close();
			//
			//////////////

		} catch (MalformedURLException eMal) {
			errorMsg =	eMal.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			log.info(errorMsg);
			eMal.printStackTrace();

		} catch (IOException eIO) {

			errorMsg =	eIO.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			log.info(errorMsg);
			eIO.printStackTrace();
			
			// pass up to calling method to handle there...
			throw eIO;

		} catch (RuntimeException eRT){
			errorMsg =	eRT.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			log.info(errorMsg);
			eRT.printStackTrace();

		} 
		
		return responseBody;

	}
}

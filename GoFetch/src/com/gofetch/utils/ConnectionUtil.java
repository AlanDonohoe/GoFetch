package com.gofetch.utils;

import java.io.IOException;
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
     * Method to make a GET HTTP connecton to the given url and return the
     * output
     *
     * @param urlToFetch url to be connected
     * @return the http get response
     */
    public static String makeRequest(String urlToFetch) {
    	String responseBody = "";
    	
    	// new implmentation - GAE version:
    	
    	try {
            URL url = new URL(urlToFetch);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            if(connection.getResponseCode() != 200){
            	throw new RuntimeException("Failed : HTTP error code : "
    					+ connection.getResponseCode());
            }
            
            responseBody = connection.getResponseMessage();
            

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
        	
        } catch (RuntimeException eRT){
        	errorMsg =	eRT.getMessage();
        	errorMsg += " caused by: \n" + urlToFetch;
        	log.info(errorMsg);
        	eRT.printStackTrace();
        	
        }
    	
    	return responseBody;


    	
    }
}

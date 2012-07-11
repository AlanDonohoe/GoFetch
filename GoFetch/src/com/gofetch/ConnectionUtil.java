package com.gofetch;

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


    	
    	//old - glassfish implementation....
//        HttpClient httpclient = new DefaultHttpClient();
//
//        HttpGet httpget = new HttpGet(urlToFetch);
//
//        // Create a response handler
//        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//        
//        try {
//            responseBody = httpclient.execute(httpget, responseHandler);
//        }catch (HttpResponseException ex){
//            
//            errorMsg = "HttpResponseException caused by: \n" + urlToFetch;
//            log.info(errorMsg);
//            ex.printStackTrace();
//
//        } catch (Exception e) {
//
//            errorMsg = "Problem Connecting to SEOMoz server ";
//            errorMsg += ConnectionUtil.TAG + " threw " + e.getMessage();
//            errorMsg += "\n urlToFetch = " + urlToFetch;
//            log.info(errorMsg);
//            e.printStackTrace();
//
//        }
//        httpclient.getConnectionManager().shutdown();
//        return responseBody;
//    }
    	
    }
}

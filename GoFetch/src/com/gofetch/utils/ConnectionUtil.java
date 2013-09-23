package com.gofetch.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;


/**
 * 
 * Utility Class to make a GET HTTP connection to the given url and pass the
 * output - GAE friendly version
 * 
 * @author Alan Donohoe
 * 
 */
public class ConnectionUtil {

	//public static final String TAG = "ConnectionUtil"; // used for logging
	// errors
	private static Logger log = Logger.getLogger(ConnectionUtil.class.getName()); // used for logging errors
	private static int timeout = 60000; // no of milliseconds to wait before calling time out
	private static int retries = 10;

	/**
	 * 
	 * @param urlPage URL address to post to.
	 * @param postData data to post
	 * @return response from server
	 * @throws Exception
	 */
	public static String post(String urlPage, String postData) throws Exception {

		URL url = new URL(urlPage);
		StringBuilder response = new StringBuilder();
		HttpURLConnection connection = null;
		int http_status;
		String line;

		try{
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla");
			connection.addRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");

			connection.addRequestProperty("Content-Length", "" + postData.length());
			connection.setDoOutput(true);
			connection.setDoInput(true);

			DataOutputStream outputStream = new DataOutputStream(
					connection.getOutputStream());
			outputStream.writeBytes(postData.toString());	

			outputStream.flush();
			outputStream.close();

			InputStream input = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			http_status = connection.getResponseCode();

			if (http_status / 100 != 2) {
				log.warning(http_status + " status back from: " + urlPage);
			}

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			reader.close();
		}catch(Exception e){
			log.severe(e.getMessage());
			throw(e);
		}finally{
			connection.disconnect(); 
		}

		return response.toString();

	}

	/**
	 * 
	 * Method to make a GET HTTP connection to the given url and return the
	 * output
	 * Upon failure, the same get call is made to urlToFetch for up to "retries" times
	 * If it fails after this, an exception is thrown.
	 * 
	 * @param urlToFetch url to be connected
	 * @return the http get response
	 * @throws Exception 
	 */
	public static String makeRequest(String urlToFetch) throws Exception {

		String line, responseBody = "";
		URL url;
		HttpURLConnection connection = null;
		int http_status;
		BufferedReader reader = null;

		try{
			url = new URL(urlToFetch);
		}catch(MalformedURLException e){
			log.severe(e.getMessage());
			throw (e);
		}

		for (int i = 0 ; i < retries ; i++) {

			try {
				log.info("i: " + i);
				System.setProperty("http.keepAlive", "false"); // fix for seomoz.api/links  timeout issue....
				connection= (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);

				reader = new BufferedReader(new InputStreamReader(url.openStream()));

				http_status = connection.getResponseCode();

				if (http_status / 100 != 2) {
					log.warning(http_status + " status back from: " + urlToFetch);
				}

			} catch (IOException e) {
				log.warning(e.getMessage() + "fail no: " + i);
				connection.disconnect(); 
				reader.close();
				continue;
			}

			while ((line = reader.readLine()) != null) {
				responseBody += line;
			}

			connection.disconnect(); 
			reader.close();

			return responseBody;
		}


		//		try {


		//			System.setProperty("http.keepAlive", "false"); // fix for seomoz.api/links  timeout issue....
		//			connection= (HttpURLConnection) url.openConnection();
		//			connection.setConnectTimeout(timeout);
		//			connection.setReadTimeout(timeout);
		//
		//			reader = new BufferedReader(new InputStreamReader(url.openStream()));
		//		
		//			http_status = connection.getResponseCode();
		//
		//			if (http_status / 100 != 2) {
		//				log.warning(http_status + " status back from: " + urlToFetch);
		//			}

		//			while ((line = reader.readLine()) != null) {
		//				responseBody += line;
		//			}
		//
		//			reader.close();
		//
		//		}catch (IOException e){
		//						http_status = ((HttpURLConnection)connection).getResponseCode();
		//						log.severe("makeRequest Error:" + e.getMessage());
		//						throw (e);

		//			try {
		//				int ret = 0;
		//				String errorResp = "";
		//
		//				http_status = ((HttpURLConnection)connection).getResponseCode();
		//				log.severe("http_status: " + http_status);
		//				es = ((HttpURLConnection)connection).getErrorStream();
		//
		//				// read the error response body
		//				while ((ret = es.read(buf)) > 0) {
		//					errorResp += buf.toString();
		//				}
		//				log.severe(errorResp);
		//				// close the errorstream
		//				es.close();
		//				throw (e);

		//			} catch(IOException ex) {
		//				
		//				log.severe("Inner exception block: " +ex.getMessage());
		//				throw (ex);
		//			}


		//	} finally {
		//		connection.disconnect(); 
		//		reader.close();
		//		//es.close();
		//	}
		connection.disconnect(); 
		reader.close();

		throw(new Exception("Exceeded max no of retries: " + retries));
	}

	// see:
	// http://stackoverflow.com/questions/2295221/java-net-url-read-stream-to-byte
	public static byte[] getImage(String urlAddress) {

		ByteArrayOutputStream bais = new ByteArrayOutputStream();
		InputStream is = null;
		URL url = null;

		try {
			url = new URL(urlAddress);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			is = url.openStream();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to
			// read in at a time.
			int n;

			while ((n = is.read(byteChunk)) > 0) {
				bais.write(byteChunk, 0, n);
			}
		} catch (IOException e) {
			System.err.printf("Failed while reading bytes from %s: %s",
					url.toExternalForm(), e.getMessage());
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return bais.toByteArray();
	}
}

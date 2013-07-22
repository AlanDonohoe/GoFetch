package com.gofetch.utils;

import java.awt.image.BufferedImage;
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
	private static String errorMsg; // used for logging errors

	public static String post(String urlPage, String postData) throws Exception {

		URL url = new URL(urlPage);

		StringBuilder response = new StringBuilder();

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		reader.close();

		return response.toString();

	}

	/**
	 * 
	 * Method to make a GET HTTP connection to the given url and return the
	 * output
	 * 
	 * @param urlToFetch
	 *            url to be connected
	 * @return the http get response
	 * @throws IOException
	 */
	public static String makeRequest(String urlToFetch) throws IOException {
		
		log.info("Entering makeRequest(...)");
		
		String responseBody = "";

		// new implementation - GAE version:

		try {

			URL url = new URL(urlToFetch);

			// /////
			//
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				responseBody += line;
			}

			reader.close();

			// ///////
			// this works - but there's a timeout from SEOMoz server....
			// BufferedReader in = new BufferedReader(
			// new InputStreamReader(url.openStream()));
			//
			// String inputLine;
			// while ((inputLine = in.readLine()) != null)
			// responseBody += inputLine;
			// in.close();
			//
			// ////////////

		} catch (MalformedURLException eMal) {
			errorMsg = eMal.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			// .info(errorMsg);
			eMal.printStackTrace();

			// TODO: add throws to all these catch blocks to allow control
			// further up stream

		} catch (IOException eIO) {

			errorMsg = eIO.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			log.info(errorMsg);
			// eIO.printStackTrace();

			// pass up to calling method to handle there...
			throw eIO;

		} catch (RuntimeException eRT) {
			errorMsg = eRT.getMessage();
			errorMsg += " caused by: \n" + urlToFetch;
			log.info(errorMsg);
			// eRT.printStackTrace();

		}

		return responseBody;

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

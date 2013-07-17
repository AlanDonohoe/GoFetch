package com.gofetch.images;

/*
 * see: https://linkpeek.com/docs/snapshot-website-screenshots-with-java
 * - we are using this 3rd party image provider..
 * 
 * may need to move to storing images in the datastore or blobstore.
 * see:
 * https://developers.google.com/appengine/docs/java/blobstore/overview
 https://developers.google.com/appengine/articles/java/serving_dynamic_images
 https://developers.google.com/appengine/docs/java/blobstore/overview#using-blobstore-with-gcs


 */

import java.security.MessageDigest;

/*
 * Example use:
 * 
 *     Api_v1 api = new Api_v1( "your-key", "your-secret" );
        System.out.println( api.make( "lostquery.com", "336x336" ) );
        System.out.println( api.make( "lostquery.com", "400x200" ) );
        System.out.println( api.make( "http://lostquery.com", "200x400" ) );
 */
import java.math.BigInteger;

import com.gofetch.utils.ConnectionUtil;

public class LinkPeekImageWrapper {

	//String APIKEY, SECRET;
	
	public LinkPeekImageWrapper(){}
	

	/**
	 * 
	 * @param urlAddress - URL we want to get the image of 
	 * @param size - see: ImageConstants: thumbnail, large, etc
	 * @return - String that is the new URL, hosting the img of the url target passed to method
	 * @throws Exception
	 */
	public String makeImageURLString(String urlAddress, String size) throws Exception {
		String out, token;

		String message = ImageConstants.SECRET + urlAddress + size;

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(message.getBytes());
		BigInteger hash = new BigInteger(1, md5.digest());

		token = hash.toString(16);
		while (token.length() < 32) {
			token = "0" + token;
		} // pad with 0's

		out = "http://linkpeek.com/api/v1?uri=" + urlAddress + "&apikey="
				+ ImageConstants.API_KEY + "&token=" + token + "&size=" + size;

		return out;
	}
	
	/**
	 * 
	 * @param urlAddress - URL we want to get the image of 
	 * @param size - see: ImageConstants: thumbnail, large, etc
	 * @return - byte array which is the image of the urlAddress passed to method.
	 * @throws Exception
	 */
	public byte[] getURLsImage(String urlAddress, String size) throws Exception{
		
		String linkPeekCallURL;
		// get the image hosted on the linkpeek server:
		
		linkPeekCallURL = makeImageURLString(urlAddress, size);
		
		return ConnectionUtil.getImage(linkPeekCallURL);
		
	}
}

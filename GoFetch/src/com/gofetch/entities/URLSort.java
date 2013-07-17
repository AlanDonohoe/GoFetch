package com.gofetch.entities;

import java.util.Comparator;

/**
 * Helper class that can sort URLs based on their member data
 * @author alandonohoe
 *
 */
public class URLSort implements Comparator<URL>{

	public int compare(URL url1, URL url2) {
		
		return url1.getDomain().compareToIgnoreCase(url2.getDomain());
	}
	
}

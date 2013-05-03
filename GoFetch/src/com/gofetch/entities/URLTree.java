package com.gofetch.entities;

import java.util.ArrayList;

/**
 * Represents a tree of urls, which is implemented using a list of URLNodes
 *  - themselves = a URL and associated social data and a list of backlinks (= URLs & their associated social data)
 * @author alandonohoe
 *
 */
public class URLTree {
	
	private URLNode root;
	
	public URLTree(URLPlusSocialData urlPlusSocialData){
		
		root = new URLNode();
		
		root.setUrlPlusSocialData(urlPlusSocialData);
		root.setChildren(new ArrayList<URLNode>());
		root.setParent(null);
		
	}
	
	public URLNode getRoot(){
		return root;
	}
	
	@Override
    public String toString() {
	 
	//TODO: iterate through the urlplussocialdata turning all into nested strings....
		
		// start of json tree object
		String json = "{";
		
		json += root.toString();
		
		// childToString(json, root);
		
		// end of json tree object
		json += "}";
		
		
		
		return json;
 }
	
	
	
	
	
	

}

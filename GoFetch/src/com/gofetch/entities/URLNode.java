package com.gofetch.entities;

import java.util.ArrayList;
import java.util.List;

////////////
//
public class URLNode {

	private URLPlusSocialData urlPlusSocialData;
	private URLNode parent;
	private List<URLNode> children;
	
	public URLNode(){}

	//TODO: test this.. 
	//	what about when the urlAddress is a child of a child... ??
	public URLNode getNode(String urlAddress){
		// runs through the children arraylist but what about the next level...???
		//	need to make this recursive..???
		for (URLNode child : this.getChildren()) {
			if (child.getUrlPlusSocialData().getUrl().getUrl_address().equals(urlAddress)) { 
				return child; 
			}
		}
		return null;

	}

	// adds a new child to the current node and returns the new child node
	public URLNode addChild(URLPlusSocialData urlPlusSocialData){

		URLNode node = new URLNode();

		node.setUrlPlusSocialData(urlPlusSocialData);
		node.setParent(this);
		node.setChildren(new ArrayList<URLNode>());
		
		children.add(node);
		
		return node;

	}

	public URLPlusSocialData getUrlPlusSocialData() {
		return urlPlusSocialData;
	}

	public void setUrlPlusSocialData(URLPlusSocialData urlPlusSocialData) {
		this.urlPlusSocialData = urlPlusSocialData;
	}

	public List<URLNode> getChildren() {
		return children;
	}

	public void setChildren(List<URLNode> children) {
		this.children = children;
	}

	public URLNode getParent() {
		return parent;
	}

	public void setParent(URLNode parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		
		
		return null;
	}


} 

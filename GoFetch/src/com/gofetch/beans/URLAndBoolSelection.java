package com.gofetch.beans;

import com.gofetch.entities.URL;

/**
 * Wrapper for standard URL, but with boolean selection as field, useful in user selection operations
 * selected - default = false.
 */
public class URLAndBoolSelection {
	
	URL url;
	boolean selected;
	
	public URLAndBoolSelection() {
	
		this.selected = false;
	}
	
	public URLAndBoolSelection(URL url, boolean selected) {
		this.url = url;
		this.selected = selected;
	}

	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

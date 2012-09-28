package com.gofetch.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean (name="testBean")
public class TestBean {

	private String data;
	private List<String> dataList;
	
	public TestBean(){
		data = "TestBean Data";
		dataList = new ArrayList<String>();
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String homepage(){
		return "index";
	}
	
}

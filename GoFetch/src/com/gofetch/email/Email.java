package com.gofetch.email;

public class Email {
	
	private String toAddress; 
	private String toPersonalName;
	private String fromAddress;
	private String fromPersonalName;
	private	String msgText;
	private String msgSubject;
	
	public Email(){
		
	}
	
	public Email(String toAddress,String toPersonalName, String fromAddress, 
				String fromPersonalName, String msgText, String msgSubject){
		
		this.toAddress =toAddress; 
		this.toPersonalName =toPersonalName;
		this.fromAddress =fromAddress;
		this.fromPersonalName =fromPersonalName;
		this.msgText =msgText;
		this.msgSubject =msgSubject;
		
	}
	
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getToPersonalName() {
		return toPersonalName;
	}
	public void setToPersonalName(String toPersonalName) {
		this.toPersonalName = toPersonalName;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromPersonalName() {
		return fromPersonalName;
	}
	public void setFromPersonalName(String fromPersonalName) {
		this.fromPersonalName = fromPersonalName;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getMsgSubject() {
		return msgSubject;
	}
	public void setMsgSubject(String msgSubject) {
		this.msgSubject = msgSubject;
	}

	@Override
	public String toString() {
		return "toAddress :" + toAddress + "\n" +
		"toPersonalName :" + toPersonalName + "\n" +
		"fromAddress :" + fromAddress + "\n" +
		 "fromPersonalName :" + fromPersonalName + "\n" +
		 "msgText :" + msgText + "\n" +
		 "msgSubject :" + msgSubject;
	}
	
	
	
}

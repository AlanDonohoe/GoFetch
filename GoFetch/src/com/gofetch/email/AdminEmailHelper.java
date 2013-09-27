package com.gofetch.email;

import java.util.List;
import java.util.logging.Logger;

import com.gofetch.entities.User;
import com.gofetch.entities.UserDBService;

/**
 * Handles higher-level email functionality for sending emails to administrator
 * 
 * @author alandonohoe
 *
 */
public class AdminEmailHelper extends EmailWrapper{
	
	private static Logger log = Logger.getLogger(AdminEmailHelper.class.getName());
	
	private String emailInfoTitle = "GoFetch Info Email", emailWarningTitle = "GoFetch Warning Email", emailSevereTitle = "GoFetch Severe Email", 
			emailFromAddress = "gofetch@gofetchdata.appspotmail.com",fromPersonalName = "GoFetch";
	
	public AdminEmailHelper(){
		
		emailInfoTitle = "GoFetch Info Email"; 
		emailWarningTitle = "GoFetch Warning Email"; 
		emailSevereTitle = "GoFetch Severe Email"; 
		emailFromAddress = "alandonohoe123@gmail.com";
		fromPersonalName = "GoFetch";
		
	}
	
	public String getEmailInfoTitle() {
		return emailInfoTitle;
	}

	public void setEmailInfoTitle(String emailInfoTitle) {
		this.emailInfoTitle = emailInfoTitle;
	}

	public String getEmailWarningTitle() {
		return emailWarningTitle;
	}

	public void setEmailWarningTitle(String emailWarningTitle) {
		this.emailWarningTitle = emailWarningTitle;
	}

	public String getEmailSevereTitle() {
		return emailSevereTitle;
	}

	public void setEmailSevereTitle(String emailSevereTitle) {
		this.emailSevereTitle = emailSevereTitle;
	}

	public String getEmailFromAddress() {
		return emailFromAddress;
	}

	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}

	public String getFromPersonalName() {
		return fromPersonalName;
	}

	public void setFromPersonalName(String fromPersonalName) {
		this.fromPersonalName = fromPersonalName;
	}

	/**
	 * Sends a warning level email to the administrator's email address
	 * which is set in the goFetch database user's table
	 * @param msg - email message to send
	 * @throws Exception 
	 */
	public void sendWarningEmailToAdministrator(String msg) throws Exception{
		
		Email email = null;
		List<User> administrators = getAdministrators();
		
		if(null == administrators){
			log.warning("No administrators found in DB");
			return;
		}
		
		email = new Email(administrators.get(0).getEmail(),administrators.get(0).getUsername(),
				this.emailFromAddress, this.fromPersonalName, msg, this.emailWarningTitle);
		
		try {
			sendEmail(email);
		} catch (Exception e) {
			log.warning(email.toString() + "Not sent");
			log.warning(e.getMessage());
			throw(e);
		}
		
		log.info("Successfully send email: " + email.toString());
	}
	
	/**
	 * Sends a severe level email to the administrator's email address
	 * which is set in the goFetch database user's table
	 * @param msg - email message to send
	 * @throws Exception 
	 */
	public void sendSevereEmailToAdministrator(String msg) throws Exception{
		
		Email email = null;
		List<User> administrators = getAdministrators();
		
		if(null == administrators){
			log.warning("No administrators found in DB");
			return;
		}
		
		email = new Email(administrators.get(0).getEmail(),administrators.get(0).getUsername(),
				this.emailFromAddress, this.fromPersonalName, msg, this.emailSevereTitle);
		
		try {
			sendEmail(email);
		} catch (Exception e) {
			log.warning(email.toString() + "Not sent");
			log.warning(e.getMessage());
			throw(e);
		}
		
		log.info("Successfully send email: " + email.toString());
		
	}

	/**
	 * Sends a info level email to the administrator's email address
	 * which is set in the goFetch database user's table
	 * @param msg - email message to send
	 * @throws Exception 
	 */
	public void sendInfoEmailToAdministrator(String msg) throws Exception{
		
		Email email = null;
		List<User> administrators = getAdministrators();
		
		if(null == administrators){
			log.warning("No administrators found in DB");
			return;
		}
		
		email = new Email(administrators.get(0).getEmail(),administrators.get(0).getUsername(),
				this.emailFromAddress, this.fromPersonalName, msg, this.emailInfoTitle);
		
		try {
			sendEmail(email);
		} catch (Exception e) {
			log.warning(email.toString() + "Not sent");
			log.warning(e.getMessage());
			throw(e);
		}
		
		log.info("Successfully send email: " + email.toString());
		
	}
	
	/**
	 * 
	 * @return list of users who have administrator flag set to true
	 */
	private List<User> getAdministrators(){
		
		UserDBService UserDBService = new UserDBService();

		return(UserDBService.getAdministrator());
	}
}

package com.gofetch.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.gofetch.seomoz.SEOMozImplFreeAPI;

/**
 * Wraps standard email functionality
 * @author alandonohoe
 *
 */
public class EmailWrapper {

	private static Logger log = Logger.getLogger(EmailWrapper.class.getName());

	public static void sendEmail(Email email) throws Exception{
		
		sendEmail(email.getToAddress(), email.getToPersonalName(),
					email.getFromAddress(), email.getFromPersonalName(),
					email.getMsgText(), email.getMsgSubject());
	}

	public static void sendEmail(String toAddress, String toPersonalName, 
			String fromAddress, String fromPersonalName,
			String msgText,
			String msgSubject) throws Exception{
		
		if(toAddress.isEmpty() || toPersonalName.isEmpty() || fromAddress.isEmpty() || 
				fromPersonalName.isEmpty() || fromPersonalName.isEmpty() || msgText.isEmpty()
				|| msgSubject.isEmpty())
			throw(new Exception("One of more email fields are empty"));

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress(fromAddress, fromPersonalName));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(toAddress, toPersonalName));
			msg.setSubject(msgSubject);
			msg.setText(msgText);         

			Transport.send(msg);

		} catch (AddressException eAd) {
			log.warning(eAd.getMessage());
			throw(eAd);
		} catch (MessagingException eMsg) {
			log.warning(eMsg.getMessage());
			throw(eMsg);
		} catch (UnsupportedEncodingException e) {
			log.warning(e.getMessage());
			throw(e);
		}
	}
	
}

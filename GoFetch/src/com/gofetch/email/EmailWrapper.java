package com.gofetch.email;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailWrapper {

	public static void SendEmail(String toAddress, String toPersonalName, 
			String fromAddress, String fromPersonalName,
			String msgText,
			String msgSubject){

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);

			//New:
				msg.setFrom(new InternetAddress(fromAddress, fromPersonalName));
				msg.addRecipient(Message.RecipientType.TO,
						new InternetAddress(toAddress, toPersonalName));
				msg.setSubject(msgSubject);
				msg.setText(msgText);         

				// OLD
				//         msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
				//         msg.addRecipient(Message.RecipientType.TO,
				//                          new InternetAddress("alandonohoe123@gmail.com", "Mr. User"));
				//         msg.setSubject("Your Example.com account has been activated");
				//         msg.setText("Hello World");


				Transport.send(msg);

		} catch (AddressException eAd) {
			//TODO ...
		} catch (MessagingException eMsg) {
			//TODO ...
		} catch (UnsupportedEncodingException e) {
			//TODO ...
		}
	}
}

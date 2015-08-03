package com.blazebit.storage.modules.alert.email;

import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.blazebit.storage.core.api.spi.AlertContext;
import com.blazebit.storage.core.api.spi.AlertService;

public abstract class EmailAlertService implements AlertService {

	protected abstract Session getSession();
	
	protected abstract List<String> getRecipients(AlertContext alertContext);
	
	protected abstract String getSubject(AlertContext alertContext);

	protected abstract String getText(AlertContext alertContext);
	
	@Override
	public void alert(AlertContext alertContext) {
		Message message = new MimeMessage(getSession());
		List<String> recipients = getRecipients(alertContext);
		Address[] addresses = parseAddresses(recipients);
		
		try {
			message.setRecipients(RecipientType.TO, addresses);
			message.setSubject(getSubject(alertContext));
			message.setText(getText(alertContext));

			message.setHeader("X-Mailer", getMailer());
			Date timeStamp = new Date();
			message.setSentDate(timeStamp);
		     
			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Could not send alert email!", e);
		}
	}
	
	protected String getMailer() {
		return "Blaze Storage";
	}
	
	private Address[] parseAddresses(List<String> recipients) {
		Address[] addresses = new Address[recipients.size()];
		
		for (int i = 0; i < addresses.length; i++) {
			Address[] tempAddresses;
			try {
				tempAddresses = InternetAddress.parse(recipients.get(i));
				
				if (tempAddresses.length > 1) {
					throw new IllegalArgumentException("Invalid multiple recipients at index " + i + ": " + recipients.get(i));
				}
				
			} catch (AddressException e) {
				throw new IllegalArgumentException("Invalid recipient at index " + i + ": " + recipients.get(i), e);
			}
			
			addresses[i] = tempAddresses[0];
		}
		
		return addresses;
	}

}

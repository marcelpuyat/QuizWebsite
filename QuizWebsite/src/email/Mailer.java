package email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import user.User;
 
public class Mailer {
	
	/* Included mail.jar (in Tomcat server), which was downloaded from Oracle: http://www.oracle.com/technetwork/java/index-138643.html */
	
	// Code here adopted from mkyong.com (copied from an example shown by user: mkyong)
	//
	// http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
	
	public static boolean emailChallenge(User user_to, User user_from, int score, double time_taken, String quiz_name) {
		if (user_to.getEmailAddress() == null) return false;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("stanfordquizbook@gmail.com","stanford11");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@no-spam.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user_to.getEmailAddress()));
			message.setSubject("Quiz Challenge from " +user_from.getDisplayName());
			message.setText("Dear "+user_to.getDisplayName()+"," +
					"\n\n "+user_from.getDisplayName()+" challenges you to beat his/her score of "+score+"% in "+time_taken+" seconds on "+quiz_name+" on QuizBook!" +
					"\n\n Login to accept the challenge: http://localhost:8080/QuizWebsite/Login.jsp");
 
			Transport.send(message);
 
			System.out.println("Done");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
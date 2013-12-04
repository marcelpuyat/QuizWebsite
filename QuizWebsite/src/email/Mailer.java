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
	public static boolean emailChallenge(User user_to, User user_from, int score, double time_taken, String quiz_name) {
		if (user_from.getEmailAddress() == null) return false;
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
					InternetAddress.parse("marcelp@stanford.edu")); // CHANGE TO user_to email
			message.setSubject("Quiz Challenge from " +user_from.getDisplayName());
			message.setText("Dear "+user_to.getDisplayName()+"," +
					"\n\n "+user_from.getDisplayName()+" challenges you to beat his/her score of "+score+"% in "+time_taken+" seconds on "+quiz_name+" on QuizBook!");
 
			Transport.send(message);
 
			System.out.println("Done");
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
package quizPckg;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */

/**
 * This whole class is a stub and will be deleted eventually.
 * Creates a quiz and fakes getting a quiz from SQL.
 */
@WebListener
public class StubListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public StubListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	
    	// All stub testing.
    	
    	// Test multiple choice question.
    	ArrayList<String> options = new ArrayList<String>(2);
		options.add("Stanford");
		options.add("Cal");
		int answer = 0;
		String prompt = "Which University would you rather attend?";
		
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt, options, answer);
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(question);
		String id = "000000";
		Quiz quiz1 = new Quiz(questions, 0);
		
		SingleAnswerQuestion question2 = new SingleAnswerQuestion("What is the cake?", "A lie");
		ArrayList<Question> questions2 = new ArrayList<Question>();
		questions2.add(question2);
		String id2 = "000001";
		Quiz quiz2 = new Quiz(questions2, 1);
		
		ServletContext context = arg0.getServletContext();
		context.setAttribute(id, quiz1);
		context.setAttribute(id2, quiz2);

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}

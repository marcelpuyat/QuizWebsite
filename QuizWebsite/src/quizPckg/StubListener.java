package quizPckg;

import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import questionPckg.MultipleChoiceQuestion;
import questionPckg.PictureQuestion;
import questionPckg.Question;
import questionPckg.SingleAnswerQuestion;

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
    	
    	// Stub testing of a multiple choice question, and a single answer question.
    	    	
    	/* These are the options */
    	ArrayList<String> options = new ArrayList<String>(2);
		options.add("Stanford");
		options.add("Cal");
		
		/* Answer index */
		int answer = 0;
		
		/* Question prompt */
		String prompt = "Which University would you rather attend?";
		
		MultipleChoiceQuestion question = new MultipleChoiceQuestion(prompt, options, answer);
		
		/* Another question */
		ArrayList<String> options2 = new ArrayList<String>(4);
		options2.add("CS110");
		options2.add("CS108");
		options2.add("CS12035");
		
		int answer2 = 1;
		
		String prompt2 = "What class is this assignment for?";
		
		MultipleChoiceQuestion question2 = new MultipleChoiceQuestion(prompt2, options2, answer2);
		
		/* 3rd question */
		ArrayList<String> options3 = new ArrayList<String>(4);
		options3.add("O(n)");
		options3.add("O(n log(n))");
		options3.add("O(log(n))");
		
		int answer3 = 1;
		
		String prompt3 = "Merge-sort runs in...";
		
		MultipleChoiceQuestion question3 = new MultipleChoiceQuestion(prompt3, options3, answer3);
		
		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(question);
		questions.add(question2);
		questions.add(question3);
		
		String id = "000000";
		Quiz quiz1 = new Quiz("Multiple-Choice Quiz", questions, id, true, true, true, false);
		
		
		/* Another quiz... not yet being tested */
		HashSet<String> possibleAnswers = new HashSet<String>(3);
		possibleAnswers.add("Barack");
		possibleAnswers.add("Obama");
		possibleAnswers.add("Barack Obama");
		SingleAnswerQuestion question4 = new SingleAnswerQuestion("Who is the president of the United States?", possibleAnswers);
		ArrayList<Question> questions2 = new ArrayList<Question>();
		questions2.add(question4);
		
		/* Image question test */
		HashSet<String> possibleAnswers2 = new HashSet<String>(2);
		possibleAnswers.add("Google");
		possibleAnswers.add("GOOGLE");
		PictureQuestion question5 = new PictureQuestion("What company invented a web-browser with this logo?", possibleAnswers2, "https://lh3.ggpht.com/7O3H3V0fEBumwJlqDLD03t1fmwl8fH9YoBsPwB2UQ_aiBilM7OAOe2gkFB3wrojJqbM=w300");
		questions2.add(question5);
		
		String id2 = "000001";
		Quiz quiz2 = new Quiz("SingleAnswer & Image Quiz", questions2, id2, false, true, true, false);
		
		ServletContext context = arg0.getServletContext();
		context.setAttribute(quiz1.getID(), quiz1);
		context.setAttribute(quiz2.getID(), quiz2);
		/* These test quizzes are now accessible by the QuizServlet, taking
		 * the place of a MySQL database of created quizzes
		 */

    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}

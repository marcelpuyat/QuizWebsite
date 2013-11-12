package quizPckg;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import databasesPckg.QuizTakingDBHandler;

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
    	QuizTakingDBHandler quizDBHandler = new QuizTakingDBHandler();
    	ServletContext context = arg0.getServletContext();
    	context.setAttribute("quiz_handler", quizDBHandler);
    }
    
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}

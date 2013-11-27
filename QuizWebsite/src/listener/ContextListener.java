package listener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import customObjects.SelfRefreshingConnection;
import databases.DBInfo;

/**
 * This whole class is a stub and will be deleted eventually.
 * Creates a quiz and fakes getting a quiz from SQL.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	ServletContext context = arg0.getServletContext();
    	context.setAttribute("database_connection", new SelfRefreshingConnection(this.createConnection()));
    }
    
    /**
     * Creates connection and returns it
     * @return
     */
    private Connection createConnection() {
    	try { 
			Class.forName("com.mysql.jdbc.Driver"); 

			Connection con = DriverManager.getConnection 
					( "jdbc:mysql://" + DBInfo.MYSQL_DATABASE_SERVER, DBInfo.MYSQL_USERNAME ,DBInfo.MYSQL_PASSWORD);
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + DBInfo.MYSQL_DATABASE_NAME);
			return con;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        ServletContext context = arg0.getServletContext();
        SelfRefreshingConnection con = (SelfRefreshingConnection)context.getAttribute("database_connection");
        try {
        	con.close();
        } catch (Exception ignored) { ignored.printStackTrace(); }
    }
	
}

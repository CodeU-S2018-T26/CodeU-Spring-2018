package codeu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the activity feed page. */
public class ActivityFeedServlet extends HttpServlet 
{
	
	/**
	 * This function fires when a user requests the /activity URL. It simply
	 * forwards the request to activity.jsp.
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	    request.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp").forward(request, response);
	}
}

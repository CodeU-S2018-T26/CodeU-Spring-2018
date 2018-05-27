package codeu.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class ActivityFeedServletTest 
{
	  private ActivityFeedServlet activityfeedServlet;
	  private HttpServletRequest mockRequest;
	  private HttpServletResponse mockResponse;
	  private RequestDispatcher mockRequestDispatcher;

	  @Before
	  public void setup() 
	  {
	    activityfeedServlet = new ActivityFeedServlet();
	    mockRequest = Mockito.mock(HttpServletRequest.class);
	    mockResponse = Mockito.mock(HttpServletResponse.class);
	    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
	    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/activityfeed.jsp"))
	        .thenReturn(mockRequestDispatcher);
	  }
	  
	  @Ignore
	  public void testDoGet() throws IOException, ServletException {
	    activityfeedServlet.doGet(mockRequest, mockResponse);

	    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
	  }
	  
	  
}

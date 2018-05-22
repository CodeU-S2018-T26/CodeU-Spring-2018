package codeu.controller;

import codeu.model.data.Conversation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminServletTest {
  private AdminServlet AdminServlet;
  private HttpServletRequest mockRequest;
  private HttpSession mockSession;
  private HttpServletResponse mockResponse;
  private RequestDispatcher mockRequestDispatcher;

  @Before
  public void setUp() {
    AdminServlet = new AdminServlet();
    mockRequest = Mockito.mock(HttpServletRequest.class);
    mockSession = Mockito.mock(HttpSession.class);
    Mockito.when(mockRequest.getSession()).thenReturn(mockSession);

    mockResponse = Mockito.mock(HttpServletResponse.class);
    mockRequestDispatcher = Mockito.mock(RequestDispatcher.class);
    Mockito.when(mockRequest.getRequestDispatcher("/WEB-INF/view/admin.jsp"))
      .thenReturn(mockRequestDispatcher);
  }

  @Test
  public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
    Mockito.when(mockSession.getAttribute("user")).thenReturn(null);

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockResponse).sendRedirect("/login");
  }

  @Test
  public void testDoGet_UserNotAdmin() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("User1");

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequest).setAttribute("error", "Only admins can access this page.");
    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }

  @Test
  public void testDoGet_UserAdmin() throws IOException, ServletException{
    Mockito.when(mockSession.getAttribute("user")).thenReturn("ayliana");

    AdminServlet.doGet(mockRequest, mockResponse);

    Mockito.verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
  }
}

package codeu.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminServlet extends HttpServlet {
  static final List<String> ADMIN_USERNAMES = Arrays.asList("ayliana", "Marouane", "jeremy", "marissa", "raymond");
  //This function fires when a user requests the /admin page.
  //It forwards the request to admin.jsp.
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them try to access the admin page
      response.sendRedirect("/login");
      return;
    }
    else if(! ADMIN_USERNAMES.contains(username)) {
      // user is not an admin, show error message
      request.setAttribute("error", "Only admins can access this page.");
    }
    request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
  }
}

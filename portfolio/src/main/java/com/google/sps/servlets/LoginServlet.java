package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Display a login url if the user is logged out and a logout url if otherwise
 */
@WebServlet("/userlogin")
public class LoginServlet extends HttpServlet {
	 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    response.setContentType("text/html");

    // If the user is logged in, display a hello and a logout URL
    if(userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();	
      String urlRedirectLogout = "/index.html";
      String logoutUrl = userService.createLogoutURL(urlRedirectLogout);

      response.getWriter().println("<p> Hello " + userEmail + "!</p>");
      response.getWriter().println("<p>Logout <a href = \"" + logoutUrl + "\">here</a>.</p>");
    } else { // If the user is logged out, display a login URL 
      String urlRedirectLogin = "/index.html";
      String loginUrl = userService.createLoginURL(urlRedirectLogin);

      response.getWriter().println("Logged out");
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
  }
}

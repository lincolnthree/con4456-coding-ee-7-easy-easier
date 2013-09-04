package org.example.websocket.proprietary;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("")
public class IndexServlet extends HttpServlet
{
   @Override
   protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException, IOException
   {
      String socketURL = String.format("%s:%s%s%s",
               request.getServerName(),
               request.getServerPort(),
               request.getContextPath(),
               ChatServlet.SOCKET_PATH);

      request.setAttribute("socketURL", socketURL);

      RequestDispatcher rd = getServletContext().getRequestDispatcher("/WEB-INF/index.jsp");
      rd.forward(request, response);
   }
}

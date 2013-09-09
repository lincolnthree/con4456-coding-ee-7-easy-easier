package org.javaee7.samples.cdi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@WebServlet(urlPatterns = { "/" })
public class TestServlet extends HttpServlet
{
   private static final long serialVersionUID = -6642422622622245892L;

   @Inject
   private Pojo pojo;

   @Inject
   private AnnotatedPojo annotated;

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
   {
      response.setContentType("text/html;charset=UTF-8");
      try (PrintWriter out = response.getWriter())
      {
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>CDI Example</title>");
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>CDI Example - All OK</h1>");

         out.println(pojo + "<br/>");
         out.println(annotated + "<br/>");

         out.println("</body>");
         out.println("</html>");
      }
   }

   @Override
   public String getServletInfo()
   {
      return "CDI Example Servlet";
   }
}

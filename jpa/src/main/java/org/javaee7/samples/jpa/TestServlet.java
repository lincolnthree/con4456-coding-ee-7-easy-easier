package org.javaee7.samples.jpa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

   @PersistenceContext
   private EntityManager em;

   private static int count = 0;

   @PostConstruct
   public void initNamedQuery()
   {
      em.getEntityManagerFactory().addNamedQuery("customersOrderByName",
               em.createQuery("from Customer c ORDER BY c.name DESC"));
   }

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
         out.println("<title>JDBC Connection Resource</title>");
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>JDBC Connection Resource</h1>");

         Customer customer = new Customer();
         customer.setName("Person " + count);
         em.persist(customer);
         em.flush();

         List<Customer> customers = em.createNamedQuery("customersOrderByName", Customer.class).getResultList();
         Iterator<Customer> iterator = customers.iterator();

         out.println("<ul>");
         while (iterator.hasNext())
         {
            out.println("<li>" + iterator.next().getName() + "</li>");
         }
         out.println("</ul>");

         out.println("</body>");
         out.println("</html>");
      }
   }

   @Override
   public String getServletInfo()
   {
      return "JPA Example Servlet";
   }
}

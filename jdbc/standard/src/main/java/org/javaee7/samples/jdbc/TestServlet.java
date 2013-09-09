package org.javaee7.samples.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@WebServlet(urlPatterns = { "/" })
@DataSourceDefinition(
         url = "jdbc:h2:mem:",
         className = "org.h2.jdbcx.JdbcDataSource",
         name = "java:global/jdbc/h2db",
         user = "sa",
         password = "")
public class TestServlet extends HttpServlet
{
   private static final long serialVersionUID = -6642422622622245892L;

   @Resource(lookup = "java:global/jdbc/h2db")
   private DataSource ds;

   @PostConstruct
   public void postConstruct() throws Exception
   {
      try (Connection connection = ds.getConnection())
      {
         Statement stmt = connection.createStatement();
         stmt.executeUpdate("CREATE TABLE hits ( id INT AUTO_INCREMENT PRIMARY KEY )");
      }
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

         out.println(ds.toString());

         try (Connection connection = ds.getConnection())
         {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO hits() values()");
            try (ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM hits"))
            {
               resultSet.next();
               int rows = resultSet.getInt(1);
               out.println("<h2>Rows: " + rows + "</h2>");
            }
         }
         catch (SQLException e)
         {
            e.printStackTrace();
            out.println(e.getMessage());
         }

         out.println("</body>");
         out.println("</html>");
      }
   }

   @Override
   public String getServletInfo()
   {
      return "JDBC Example Servlet";
   }
}

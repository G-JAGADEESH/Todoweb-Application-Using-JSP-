import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	Connection con;
	Statement stmt;
	ResultSet rs;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("LoginServlet init()");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "pat", "jagadhi123");
			
			stmt=con.createStatement();
		} catch(Exception e) {
			e.printStackTrace();
		}// catch()
	}// init()
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		HttpSession session=request.getSession();
		ServletContext context=getServletContext();
		try {
			// read form data
			String email=request.getParameter("email").trim();
			String pass=request.getParameter("pass").trim();
			
			// verify email, pass in database
			ResultSet rs=stmt.executeQuery("select * from register where email='"+email+"' and pass='"+pass+"'");
			
			// if login successful, display ViewTasks.jsp
			if(rs.next()) {
			session.setAttribute("user", email);	
			context.getRequestDispatcher("/ViewTasks.jsp").forward(request, response);
			} else {
			// otherwise display index.jsp back on browser
			context.getRequestDispatcher("/index.html").forward(request, response);
			} // else
		} catch(Exception e) {
			e.printStackTrace();
		}// catch()
	}// doPost()
}// class

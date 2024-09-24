import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AddTaskServlet")
public class AddTaskServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session=request.getSession();
		ServletContext context=getServletContext();
		
		try {
			// loading JDBC driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "pat", "jagadhi123");
			Statement stmt=con.createStatement();
			PreparedStatement pstmt=con.prepareStatement("INSERT INTO tasks VALUES (?,?,?,?,?)");
			
			// read HTML form data
			String taskName=request.getParameter("taskName").trim();
			String taskDate=request.getParameter("taskDate").trim();
			int taskStatus=Integer.parseInt(request.getParameter("taskStatus").trim());
			
			String email=session.getAttribute("user").toString();
			int regid=0;
			ResultSet rs=stmt.executeQuery("select regid from register where email='"+email+"'");
			if(rs.next()) {
				regid=rs.getInt(1);
			}
			
			// generate pk taskid
			int taskId=0;
			rs=stmt.executeQuery("select max(taskid) from tasks");
			if(rs.next()) {	
				taskId=rs.getInt(1);
			}
			taskId++;
			
			// insert record using PreparedStat
			pstmt.setInt(1, taskId);
			pstmt.setString(2, taskName);
			pstmt.setString(3, taskDate);
			pstmt.setInt(4, taskStatus);
			pstmt.setInt(5, regid);
			int i=pstmt.executeUpdate();
			if(i==1)
				context.getRequestDispatcher("/ViewTasks.jsp").forward(request, response);
			rs.close(); pstmt.close(); stmt.close(); con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

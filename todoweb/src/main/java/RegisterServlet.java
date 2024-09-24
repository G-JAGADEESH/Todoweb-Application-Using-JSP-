import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	Connection con;
	Statement  stmt;
	PreparedStatement pstmt;
	ResultSet rs;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("RegisterServlet init()");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "pat", "jagadhi123");
			
			stmt=con.createStatement();
			
			pstmt=con.prepareStatement("INSERT INTO register VALUES (?,?,?,?,?,?,?)");
			System.out.println("init() in RegisterServlet "+con+"\n"+stmt);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // init() method
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("RegisterServlet service()");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		ServletContext sctxt=getServletContext();
		System.out.println("RegisterServlet after sctxt");
		
		try {
			// reading form data
			int regid=0;
			String fname=request.getParameter("fname").trim();
			String lname=request.getParameter("lname").trim();
			String email=request.getParameter("email").trim();
			String pass=request.getParameter("pass").trim();
			String mobile=request.getParameter("mobile").trim();
			String address=request.getParameter("address").trim();
			System.out.println("RegisterServlet after reading form data()");
			
			// pk generation
			rs=stmt.executeQuery("select max(regid) from register");
			System.out.println("RegisterServlet after rs()");
			if(rs.next()) {
				regid=rs.getInt(1);
			}
			regid++;
			System.out.println("RegisterServlet "+regid);
			
			// record insertion
			pstmt.setInt(1, regid);
			pstmt.setString(2, fname);
			pstmt.setString(3, lname);
			pstmt.setString(4, email);
			pstmt.setString(5, pass);
			pstmt.setLong(6,Long.parseLong(mobile));
			pstmt.setString(7, address);
			pstmt.executeUpdate();
			
			// response to browser
			sctxt.getRequestDispatcher("/index.html").forward(request, response);
		} catch(Exception e) {
			out.println(e);
		} // catch()
	} // doPost()
}// class

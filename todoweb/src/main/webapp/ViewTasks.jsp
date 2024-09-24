<%-- 
select * from task where regid=(select regid from register where email='abc@gmail.com') 
--%>
<%@ page import="java.sql.*"%>
<%
	// loading JDBC driver class
	Class.forName("oracle.jdbc.driver.OracleDriver");
	
	// establish connection to db
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "pat", "jagadhi123");
	
	// create a Statement
	Statement stmt=con.createStatement();
	
	String email=session.getAttribute("user").toString();
	
	// execute select statement to obtain ResultSet object
	ResultSet rs=stmt.executeQuery("select * from tasks where regid=(select regid from register where email='"+email+"')");
	
	// iterate over ResultSet to fetch records from db
%>
<table align='center' width="70%" border="1">
	<tr>
		<th>Task ID</th>
		<th>Name</th>
		<th>Date</th>
		<th>Status</th>
	</tr>
<%
	while(rs.next()) {
	%>
	<tr>
		<td>
		<%	
			int id=rs.getInt(1);
		%>
		<%=id%>
		</td>
		<td><%=rs.getString(2)%></td>
		<td><%=rs.getString(3)%></td>
		<td>
			<%
				int status=rs.getInt(4);
				if(status==1) {
			%>	
				<font color='red'><a href='./TaskCompletedServlet?taskid=<%=id%>'>Completed</a></font>
			<%	
				} else {
			%>	
					<font color='black'><strike>Completed</strike></font>
			<%		
				} // else
			%>
		</td>
	</tr>	
	<%
	}// while()
	%>
	</table>
<%
	// close the db objects
	rs.close();
	stmt.close();
	con.close();
%>
<form method="post" action="./AddTaskServlet">
	<table align="center" border="1" width="40%">
		<tr>
			<th>Task Name</th>
			<td><input type="text" name="taskName"></td>
		</tr>
		<tr>
			<th>Task Date</th>
			<td><input type="text" name="taskDate" placeholder="dd-mm-yyyy"></td>
		</tr>
		<tr>
			<th>Task Status</th>
			<td>
				<select name="taskStatus" size="1">
					<option value="1">Not Yet Started</option>
					<option value="2">In Progress</option>
					<option value="3">Completed</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><input type="submit" name="submit" value="Register"></td>
			<td><input type="reset" name="reset" value="Clear Form"></td>
		</tr>
	</table>
</form>


package authentication;
import java.sql.*;
import javax.swing.*;

public class MysqlConnection {
	Connection conn;
	static Statement stmt1;
	public MysqlConnection() {
		
	}
	
	public Connection dbConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		try {
			Connection conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/facedb","root","Chamalie$1995");
			//stmt1=conn.createStatement();
			//Statement st =conn.createStatement();
			//st.executeUpdate("insert into user(name, email) values('mika','mi@gmail.com');");
			//Statement st =conn.createStatement();
			//st.executeUpdate("insert into user(name, email) values('tee','tp@gmail.com');");
			if(conn!=null) {
				JOptionPane.showMessageDialog(null, "Connection successfull.");}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static void main(String[] args) {
		
		
		
		
		

	}

}

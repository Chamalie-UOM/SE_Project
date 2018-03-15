package GUAclasses;

import java.sql.*;
import javax.swing.*;

public class MysqlConnection {
	private static MysqlConnection connection = null;
	static Connection conn;
	
	
	private MysqlConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/facedb","root","Chamalie$1995");
			if(conn!=null) {
				JOptionPane.showMessageDialog(null, "Connection successfull.");
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static MysqlConnection getConnInstance() {
		if(connection == null) {
			synchronized(MysqlConnection.class) {
				if(connection == null) {
					connection = new MysqlConnection();
				}
			}
		}
		return connection;
	}
	
	public void saveData(User user) {
		
		String query ="insert into user (name, email, tpnum, recovPass, face) values(?,?,?,?,?);";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(query);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getMail());
			stmt.setString(3, user.getTpnum());
			stmt.setString(4, user.getRecovPass());
			stmt.setNString(5, user.getFace().toString());
			stmt.execute();
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
	}
	
	public static void main(String[] args) {	

	}

}

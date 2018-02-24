package authentication;
import java.sql.*;
import javax.swing.*;

public class MysqlConnection {
	static Connection conn;
	static Statement stmt1;
	
	public MysqlConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/facedb","root","Chamalie$1995");
			if(conn!=null) {
				JOptionPane.showMessageDialog(null, "Connection successfull.");}
				
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		if(conn!=null){
			JOptionPane.showMessageDialog(null, "Connection reached here.");}
	}
	
	public void saveData(User user) {
		
		String query ="insert into user (name, email, tpnum, recovPass) values(?,?,?,?);";
		PreparedStatement stmt;
		try {
			stmt = this.conn.prepareStatement(query);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getMail());
			stmt.setString(3, user.getTpnum());
			stmt.setString(4, user.getRecovPass());
			stmt.execute();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	//stmt1=conn.createStatement();
	//Statement st =conn.createStatement();
	//st.executeUpdate("insert into user(name, email) values('plz','mi@gmail.com');");
	//Statement st =conn.createStatement();
	//st.executeUpdate("insert into user(name, email) values('tee','tp@gmail.com');");
	
	public static void main(String[] args) {
		
		
		
		
		

	}

}

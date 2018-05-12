package GUAclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MysqlConnection {
	private static MysqlConnection connection = null;
	Connection conn;
	
	
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
			File file = new File(user.getName() + ".jpg");
			InputStream fis = new FileInputStream(file); 
			stmt.setBinaryStream(5, fis);
			stmt.execute();
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
	}
	
	public Mat getFace(User user) {
		String query = "SELECT face FROM user WHERE name =?;";
		PreparedStatement stmt;
		Mat img= null;
		File imFile = new File("testFile.jpg");
		try {
			stmt = this.conn.prepareStatement(query);
			stmt.setString(1,user.getName());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Blob imgBlob = rs.getBlob(1); 
				InputStream in = imgBlob.getBinaryStream();
				OutputStream out = new FileOutputStream(imFile);
				byte[] buff =new byte[4096];
				int len=0;
				
				while ((len = in.read(buff))!= -1) {
					out.write(buff, 0, len);
				}
				img = Imgcodecs.imread(imFile.getAbsolutePath());
				System.out.println(img);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return img;
	}
	
	public void savePassword(String name,String pass) {
		String query ="insert into account (name, grph_pass) values(?,?);";
		PreparedStatement stmt;
			try {
				stmt = this.conn.prepareStatement(query);
				stmt.setString(1, name);
				stmt.setString(2, pass);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	}
	
	public String getPassword(String name) {
		String query = "SELECT grph_pass FROM account WHERE name =?;";
		PreparedStatement stmt;
		String pass=null;
		try {
			stmt = this.conn.prepareStatement(query);
			stmt.setString(1,name);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				pass = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pass;
	}
	
	public String getRecoveryPass(String name) {
		String query = "SELECT recovPass FROM user WHERE name =?;";
		PreparedStatement stmt;
		String pass=null;
		try {
			stmt = this.conn.prepareStatement(query);
			stmt.setString(1,name);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				pass = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pass;
	}
	
	
	public void updatePassword(String name,String pass) {
		String query ="update account set grph_pass=? where name=?;";
		PreparedStatement stmt;
			try {
				stmt = this.conn.prepareStatement(query);
				stmt.setString(1, pass);
				stmt.setString(2, name);
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	}
	
	public void deleteEntry(String name) {
		String query ="delete from user where name =?;";
		PreparedStatement stmt;
		try {
			stmt =this.conn.prepareStatement(query);
			stmt.setString(1,name);
			stmt.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

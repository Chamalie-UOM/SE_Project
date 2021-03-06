package GUAclasses;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import static org.opencv.imgcodecs.Imgcodecs.imencode; 
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JInternalFrame;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.UIManager;
public class FaceDetector extends JFrame {
	
	//definitions
	private Mat frame;
	private MatOfByte byteFrame;
	private CascadeClassifier faceTracker;
	MatOfRect trackedFaces;
	DaemonThread thread1 = null;
	VideoCapture webSource;
	MysqlConnection db = MysqlConnection.getConnInstance();
	User newuser;
	
	//validate email
	public static boolean validateEmailAddress(String email) {
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-z)]{2,3}$");
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
	
	//validate telephone number
	public static boolean validateTpnum(String tpnum) {
	    java.util.regex.Pattern p1 = java.util.regex.Pattern.compile("^\\d{3}\\s*\\d{7}$");
	    java.util.regex.Matcher m1 = p1.matcher(tpnum);
	    return m1.matches();
	} 
	
	//inner class implementing runnable for a thread to get feed from webcam
	class DaemonThread implements Runnable{
		volatile boolean Runnable= false;
		boolean dbflag = false;
		public void run() {
			synchronized(this) {
				while(Runnable) {
					if(webSource.grab()) {
						try {
							this.detectFace();
							Graphics g = contentPane.getGraphics();
							this.drawRectangle(trackedFaces);
							this.displayFrame(frame, g);	
							// adding new user data to the database.
							if (db.conn!= null && !dbflag) {
								System.out.println("reached here.");
								dbflag =true;
								newuser = new User();
								newuser.setName(textFieldname.getText());
								newuser.setMail(textFieldmail.getText());
								newuser.setTpnum(textFieldtpnum.getText());
								newuser.setRecovPass(textFieldPass.getText());
								newuser.setFace(extractFace(trackedFaces,newuser));
								db.saveData(newuser);
							}
							
						}catch (Exception ex) {
							ex.printStackTrace();;
						}
					}
				}
				
			}
		}
		
		
		//draw rectangles around the detected faces.
		public void drawRectangle(MatOfRect trackedFaces) {
			for(Rect rect : trackedFaces.toArray()) {
				Imgproc.rectangle(frame, new Point(rect.x,rect.y), new Point(rect.x + rect.width,rect.y+rect.height), new Scalar(255, 255, 0), 2);
				
			}
		}
		
		//detect faces in the frame.
		public void detectFace() {
			frame =new Mat();
			webSource.retrieve(frame);
			trackedFaces =new MatOfRect();
			faceTracker = new CascadeClassifier(FaceDetector.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
			while(trackedFaces.empty()) {
				faceTracker.detectMultiScale(frame, trackedFaces);
			}
			
		}
		
		//Display the captured frames from the webcam in the panel.
		public void displayFrame(Mat f, Graphics g) {
			//Convert Mat to MatOfByte.
			byteFrame =new MatOfByte();
			imencode(".bmp",f,byteFrame);
			//Convert it to a byte array and pass it to InputStream and read, to obtain a BufferedImage object.
			Image im;
			try {
				im = ImageIO.read(new ByteArrayInputStream(byteFrame.toArray()));
				BufferedImage buff =(BufferedImage) im;
				//display the frames on the panel
				g.drawImage(buff, 10, 10, getWidth()-30, getHeight() - 185, 0, 0, buff.getWidth(),buff.getHeight(), null);
				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		//extract the face part from the frame and resize
		public Mat extractFace(MatOfRect faces, User user) throws IOException {
			Mat face=null;
			for(Rect rect : faces.toArray()) {
				face = frame.submat(rect);
				Imgproc.cvtColor(face, face, Imgproc.COLOR_BGR2GRAY);
				Size sz = new Size(240,240);
				Imgproc.resize(face, face, sz);
				String filename= user.getName()+".jpg" ;
				Imgcodecs.imwrite(filename,face);
			}
			System.out.print(face);
			return face;
		}

	}
	
	JPanel contentPane;
	private JTextField textFieldname;
	private JTextField textFieldmail;
	private JTextField textFieldtpnum;
	private JTextField textFieldPass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FaceDetector frame = new FaceDetector();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FaceDetector() {
		setTitle("Registration");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 629, 503);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton setPass = new JButton("Set Password");
		setPass.setBackground(UIManager.getColor("Button.background"));
		setPass.setVisible(false);
		setPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				thread1.Runnable=false;
				webSource.release();
				FaceDetector.this.dispose();
				CreatePass frame1 = new CreatePass(newuser);
				frame1.btnLogin.setVisible(false);
				frame1.btnUpdate.setVisible(false);
				frame1.setVisible(true);
				JOptionPane.showMessageDialog(null,"Select a sequence of segments in a specified order to be used as your graphical password.");
			}
		});
		setPass.setFont(new Font("Georgia", Font.PLAIN, 15));
		setPass.setBounds(143, 355, 147, 31);
		contentPane.add(setPass);
		
		JButton Nextbtn = new JButton("Next");
		Nextbtn.setFont(new Font("Georgia", Font.PLAIN, 15));
		Nextbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			if (textFieldname.getText().trim().isEmpty() || textFieldmail.getText().trim().isEmpty() || 
			textFieldtpnum.getText().trim().isEmpty()||textFieldPass.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please complete all fields.");
			}else if(!validateEmailAddress(textFieldmail.getText())){
				JOptionPane.showMessageDialog(null, "Please enter a valid email.");
			}else if(!validateTpnum(textFieldtpnum.getText())){
				JOptionPane.showMessageDialog(null, "Please enter a valid telephone number.");
			}else {
				webSource = new VideoCapture();
				webSource.open(0);
				
				if(webSource.isOpened()) {
					System.out.println("webcam switched on");
				}
				thread1 = new DaemonThread();
				Thread t = new Thread(thread1);
				t.setDaemon(true);
				thread1.Runnable=true;
				t.start();
				Nextbtn.setEnabled(false);
				textFieldname.setVisible(false);
				textFieldmail.setVisible(false);
				textFieldtpnum.setVisible(false);
				textFieldPass.setVisible(false);
				Nextbtn.setVisible(false);
				setPass.setVisible(true);
			}
			}
		});
		Nextbtn.setBounds(143, 355, 147, 31);
		contentPane.add(Nextbtn);
		
		textFieldname = new JTextField();
		textFieldname.setFont(new Font("Georgia", Font.PLAIN, 15));
		textFieldname.setBounds(221, 89, 286, 36);
		contentPane.add(textFieldname);
		textFieldname.setColumns(10);
		
		textFieldmail = new JTextField();
		textFieldmail.setFont(new Font("Georgia", Font.PLAIN, 15));
		textFieldmail.setName("emailInput");
		textFieldmail.setColumns(10);
		textFieldmail.setBounds(221, 136, 286, 36);
		contentPane.add(textFieldmail);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblUserName.setBounds(122, 88, 89, 36);
		contentPane.add(lblUserName);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblEmail.setBounds(122, 135, 89, 36);
		contentPane.add(lblEmail);
		
		textFieldtpnum = new JTextField();
		textFieldtpnum.setFont(new Font("Georgia", Font.PLAIN, 15));
		textFieldtpnum.setColumns(10);
		textFieldtpnum.setBounds(221, 183, 286, 36);
		contentPane.add(textFieldtpnum);
		
		textFieldPass = new JPasswordField();
		textFieldPass.setFont(new Font("Georgia", Font.PLAIN, 15));
		textFieldPass.setColumns(10);
		textFieldPass.setBounds(221, 230, 286, 36);
		contentPane.add(textFieldPass);
		
		JLabel lblTelephoneNumber = new JLabel("Telephone number");
		lblTelephoneNumber.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblTelephoneNumber.setBounds(86, 182, 125, 36);
		contentPane.add(lblTelephoneNumber);
		
		JLabel lblRecoveryPassword = new JLabel("Recovery password");
		lblRecoveryPassword.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblRecoveryPassword.setBounds(78, 229, 133, 36);
		contentPane.add(lblRecoveryPassword);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(UIManager.getColor("Button.background"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!Nextbtn.isVisible()) {
					db.deleteEntry(newuser.getName());
					thread1.Runnable=false;
					webSource.release();
				}
				FaceDetector.this.dispose();
				HomePage frame6 = new HomePage();
				frame6.setVisible(true);
			}
		});
		btnCancel.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnCancel.setBounds(313, 356, 147, 30);
		contentPane.add(btnCancel);
	
		
	}
}

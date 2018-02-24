package authentication;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import static org.opencv.imgcodecs.Imgcodecs.imencode; 
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
public class FaceDetector extends JFrame {
	
	//definitions
	private DaemonThread thread1 = null;
	int count =0;
	Mat frame;
	MatOfByte byteFrame;
	CascadeClassifier faceTracker;
	MatOfRect trackedFaces;
	VideoCapture webSource;
	MysqlConnection db = new MysqlConnection();
	User newuser;
	//Connection con = db.dbConnect();
	
	class DaemonThread implements Runnable{
		protected volatile boolean Runnable= false;
		
		public void run() {
			synchronized(this) {
				while(Runnable) {
					if(webSource.grab()) {
						try {
							this.detectFace();
							Graphics g = contentPane.getGraphics();
							this.drawRectangle(trackedFaces);
							this.displayFrame(frame, g);
							if (db.conn!= null) {
								//Statement st =con.createStatement();
								//st.executeUpdate("insert into user(name, email) values('what','the@gmail.com');");
								System.out.println("reached here.");
								newuser = new User();
								newuser.setName(textFieldname.getText());
								newuser.setMail(textFieldmail.getText());
								newuser.setTpnum(textFieldtpnum.getText());
								newuser.setRecovPass(textFieldPass.getText());
								db.saveData(newuser);	
							}/*else {
								System.out.println("connection is null.");
							}
							*/
						}catch (Exception ex) {
							System.out.println(ex);
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
			faceTracker.detectMultiScale(frame, trackedFaces);
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
		
	}
	
	private JPanel contentPane;
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 629, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(78, 11, 411, 36);
		contentPane.add(panel);
		
		JButton Nextbtn = new JButton("Next");
		Nextbtn.setFont(new Font("Georgia", Font.PLAIN, 15));
		Nextbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
			}
		});
		Nextbtn.setBounds(249, 430, 147, 23);
		contentPane.add(Nextbtn);
		
		textFieldname = new JTextField();
		textFieldname.setBounds(221, 89, 286, 36);
		contentPane.add(textFieldname);
		textFieldname.setColumns(10);
		
		textFieldmail = new JTextField();
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
		textFieldtpnum.setColumns(10);
		textFieldtpnum.setBounds(221, 183, 286, 36);
		contentPane.add(textFieldtpnum);
		
		textFieldPass = new JTextField();
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
	}
}

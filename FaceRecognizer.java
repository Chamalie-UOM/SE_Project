package GUAclasses;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import GUAclasses.FaceDetector.DaemonThread;

import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;


public class FaceRecognizer extends JFrame {

	private JPanel contentPane;
	private JTextField userName;
	
	MysqlConnection db = MysqlConnection.getConnInstance();
	
	FaceDetector faceDet = new FaceDetector();
	FaceDetector.DaemonThread thread1 = faceDet.new DaemonThread();
	User user =new User();
	
	//detect face and capture test image
	public void recognize() {
		thread1.dbflag = true;
		Thread t = new Thread(thread1);
		t.setDaemon(true);
		thread1.Runnable=true;
		t.start();
		//user.setName("test");
		Boolean faceFlag = false;
		while(thread1.Runnable) {
			try {
				if(!faceDet.trackedFaces.empty() && !faceFlag) {
					FaceRecognizer.this.getTestImg(user);
					faceFlag=true;
				}
			}catch(Exception e) {}
		}
	}
	
	//Get the user test image to recognize face.
	public void getTestImg(User user) {
		try {
			thread1.extractFace(faceDet.trackedFaces, user);
			thread1.Runnable=false;
			faceDet.webSource.release();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FaceRecognizer frame = new FaceRecognizer();
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
	public FaceRecognizer() {
		setTitle("Login Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 662, 482);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 230, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		panel.setBounds(98, 0, 456, 316);
		contentPane.add(panel);
		
		userName = new JTextField();
	
		userName.setBounds(267, 327, 270, 34);
		contentPane.add(userName);
		userName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblUsername.setBounds(181, 327, 85, 34);
		contentPane.add(lblUsername);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				faceDet.webSource = new VideoCapture();
				faceDet.webSource.open(0);
				faceDet.contentPane = FaceRecognizer.this.contentPane;
				if(faceDet.webSource.isOpened()) {
					System.out.println("webcam switched on");
					FaceRecognizer.this.recognize();
					user.setName(userName.getText());
					user.setFace(db.getFace(user));
					CreatePass frame1 = new CreatePass(user);
					frame1.btnRegister.setVisible(false);
					frame1.setVisible(true);
				}else {
					System.out.println("code not working");
				}
				
			}
		});
		btnNext.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnNext.setBounds(267, 383, 121, 34);
		contentPane.add(btnNext);
		
		
		
	}
}

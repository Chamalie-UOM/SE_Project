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
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;


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
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 0, 626, 225);
		contentPane.add(panel);
		
		JLabel pic = new JLabel("");
		pic.setIcon(new ImageIcon(FaceRecognizer.class.getResource("/pics/795605146ca6394.jpg")));
		panel.add(pic);
		
		userName = new JTextField();
		userName.setFont(new Font("Georgia", Font.PLAIN, 15));
	
		userName.setBounds(249, 279, 270, 34);
		contentPane.add(userName);
		userName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblUsername.setBounds(154, 279, 85, 34);
		contentPane.add(lblUsername);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBackground(UIManager.getColor("Button.background"));
		btnNext.setToolTipText("Press to enter the graphical password to login");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(userName.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter the username");
				}else if(!db.checkUserExistance(userName.getText())) {
					JOptionPane.showMessageDialog(null, "Please enter a valid username");
				}else {
					faceDet.webSource = new VideoCapture();
					faceDet.webSource.open(0);
					faceDet.contentPane = FaceRecognizer.this.contentPane;
					if(faceDet.webSource.isOpened()) {
						System.out.println("webcam switched on");
						FaceRecognizer.this.recognize();
						user.setName(userName.getText());
						user.setFace(db.getFace(user));
						FaceRecognizer.this.dispose();
						CreatePass frame1 = new CreatePass(user);
						frame1.btnRegister.setVisible(false);
						frame1.btnUpdate.setVisible(false);
						frame1.setVisible(true);
						JOptionPane.showMessageDialog(null,"Enter your password sequence to login.");
						
					}else {
						System.out.println("code not working");
					}
				}
			}
		});
		btnNext.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnNext.setBounds(472, 383, 121, 34);
		contentPane.add(btnNext);
		
		JButton btnPass = new JButton("Forgot password?");
		btnPass.setBackground(UIManager.getColor("Button.background"));
		btnPass.setToolTipText("Press to reset your graphical password");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(userName.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter the username.");
				}else if(!db.checkUserExistance(userName.getText())) {
					JOptionPane.showMessageDialog(null, "Please enter a valid username");
				}else {
					user.setName(userName.getText());
					user.setFace(db.getFace(user));
					ForgetPassword frame2 =new ForgetPassword(user);
					frame2.setVisible(true);
					FaceRecognizer.this.dispose();
				}
			}
		});
		btnPass.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnPass.setBounds(10, 383, 167, 34);
		contentPane.add(btnPass);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(UIManager.getColor("Button.background"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FaceRecognizer.this.dispose();
				HomePage frame7 = new HomePage();
				frame7.setVisible(true);
			}
		});
		btnCancel.setToolTipText("Press to return to Home Page");
		btnCancel.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnCancel.setBounds(330, 383, 121, 34);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel = new JLabel("Enter the username to login to your account");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
		lblNewLabel.setBounds(10, 224, 505, 19);
		contentPane.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 254, 511, 2);
		contentPane.add(separator);
	}
}

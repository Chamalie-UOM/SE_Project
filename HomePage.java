package GUAclasses;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.SystemColor;

public class HomePage extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomePage frame1 = new HomePage();
					frame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public HomePage() {
		setTitle("Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 482);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 230, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		panel.setBounds(22, 11, 316, 421);
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblNewLabel.setIcon(new ImageIcon(HomePage.class.getResource("/pics/maxresdefault.jpg")));
		lblNewLabel.setBounds(10, 0, 306, 421);
		panel.setLayout(null);
		panel.add(lblNewLabel);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setToolTipText("Click to register to the system");
		btnRegister.setForeground(new Color(0, 0, 0));
		btnRegister.setBackground(SystemColor.control);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					FaceDetector frame = new FaceDetector();
					frame.setVisible(true);
					HomePage.this.dispose();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnRegister.setFont(new Font("Gabriola", Font.BOLD, 28));
		btnRegister.setBounds(389, 97, 271, 52);
		contentPane.add(btnRegister);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(392, 179, 271, 2);
		contentPane.add(separator);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setToolTipText("Click to login to the system");
		btnLogin.setBackground(SystemColor.control);
		btnLogin.setFont(new Font("Gabriola", Font.BOLD, 28));
		btnLogin.setBounds(389, 218, 271, 52);
		contentPane.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					FaceRecognizer frame = new FaceRecognizer();
					frame.setVisible(true);
					HomePage.this.dispose();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
			
	}
}

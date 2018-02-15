package authentication;

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

public class FirstPage extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FirstPage frame1 = new FirstPage();
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
	public FirstPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 11, 316, 421);
		contentPane.add(panel);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblNewLabel.setIcon(new ImageIcon(FirstPage.class.getResource("/images/0b350b39b80c2c6cf3f7a6ace18bacc1.jpg")));
		lblNewLabel.setBounds(10, 0, 306, 421);
		//ImageIcon Im1;
		panel.setLayout(null);
		panel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					FaceDetector frame = new FaceDetector();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnNewButton.setFont(new Font("Georgia", Font.PLAIN, 17));
		btnNewButton.setBounds(389, 97, 271, 52);
		contentPane.add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(392, 179, 271, 2);
		contentPane.add(separator);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Georgia", Font.PLAIN, 17));
		btnLogin.setBounds(389, 218, 271, 52);
		contentPane.add(btnLogin);
		//Image image = Im1.getImage(); // transform it 
		//Image newimg = image.getScaledInstance(350, 427,  java.awt.Image.SCALE_SMOOTH);
		
		
	}
}

package GUAclasses;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;

public class FaceRecognizer extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		panel.setBounds(84, 0, 456, 316);
		contentPane.add(panel);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(267, 327, 270, 34);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblUsername.setBounds(181, 327, 85, 34);
		contentPane.add(lblUsername);
		
		JButton btnNext = new JButton("Next");
		btnNext.setFont(new Font("Georgia", Font.PLAIN, 15));
		btnNext.setBounds(267, 383, 121, 34);
		contentPane.add(btnNext);
	}
}

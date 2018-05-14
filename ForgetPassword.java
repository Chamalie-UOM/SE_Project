package GUAclasses;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;

public class ForgetPassword extends JFrame {

	private JPanel contentPane;
	private JTextField textpass;
	User user;
	MysqlConnection db = MysqlConnection.getConnInstance();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//ForgetPassword frame = new ForgetPassword();
					//frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ForgetPassword(User user) {
		setTitle("Reset Password");
		this.user =user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 657, 465);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter the recovery text password entered at registration.");
		lblNewLabel.setBounds(10, 224, 470, 22);
		lblNewLabel.setFont(new Font("Georgia", Font.PLAIN, 16));
		contentPane.add(lblNewLabel);
		
		textpass = new JPasswordField();
		textpass.setFont(new Font("Georgia", Font.PLAIN, 15));
		textpass.setBounds(264, 277, 305, 30);
		contentPane.add(textpass);
		textpass.setColumns(10);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setBackground(UIManager.getColor("Button.background"));
		btnEnter.setToolTipText("Press to reset your graphical password");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if((db.getRecoveryPass(user.getName()).equals(textpass.getText()))) {
					ForgetPassword.this.dispose();
					CreatePass frame3= new CreatePass(user);
					frame3.btnLogin.setVisible(false);
					frame3.btnRegister.setVisible(false);
					frame3.setVisible(true);
					JOptionPane.showMessageDialog(null,"Select a sequence of segments in a specified order to be used as your graphical password.");
				}else {
					JOptionPane.showMessageDialog(null, "The recovery passsword you entered is incorrect.");
				}
			}
		});
		btnEnter.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
		btnEnter.setBounds(266, 349, 153, 38);
		contentPane.add(btnEnter);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBackground(UIManager.getColor("Button.background"));
		btnCancel.setToolTipText("Press to return to Login page");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ForgetPassword.this.dispose();
				FaceRecognizer frame = new FaceRecognizer();
				frame.setVisible(true);
			}
		});
		btnCancel.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
		btnCancel.setBounds(451, 349, 153, 38);
		contentPane.add(btnCancel);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 11, 621, 202);
		contentPane.add(panel);
		
		JLabel pic1 = new JLabel("New label");
		pic1.setIcon(new ImageIcon(ForgetPassword.class.getResource("/pics/795605146ca6394.jpg")));
		panel.add(pic1);
		
		JLabel lblNewLabel_1 = new JLabel("Recovery text");
		lblNewLabel_1.setFont(new Font("Georgia", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(147, 281, 118, 21);
		contentPane.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 257, 559, 2);
		contentPane.add(separator);
	}
}

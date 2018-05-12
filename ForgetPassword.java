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
		this.user =user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 400);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 230, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter the recovery text password entered at registration.");
		lblNewLabel.setBounds(10, 27, 525, 22);
		lblNewLabel.setFont(new Font("Lucida Fax", Font.PLAIN, 18));
		contentPane.add(lblNewLabel);
		
		textpass = new JPasswordField();
		textpass.setBounds(122, 154, 305, 30);
		contentPane.add(textpass);
		textpass.setColumns(10);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.setToolTipText("Press to reset your graphical password");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if((db.getRecoveryPass(user.getName()).equals(textpass.getText()))) {
					CreatePass frame3= new CreatePass(user);
					frame3.btnLogin.setVisible(false);
					frame3.btnRegister.setVisible(false);
					frame3.setVisible(true);
					JOptionPane.showMessageDialog(null,"Select a sequence of segments in a specified order to be used as your graphical password.");
					ForgetPassword.this.dispose();
				}else {
					JOptionPane.showMessageDialog(null, "The recovery passsword you entered is incorrect.");
				}
				
			}
		});
		btnEnter.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
		btnEnter.setBounds(188, 275, 153, 38);
		contentPane.add(btnEnter);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setToolTipText("Press to return to Login page");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ForgetPassword.this.dispose();
				FaceRecognizer frame = new FaceRecognizer();
				frame.setVisible(true);
			}
		});
		btnCancel.setFont(new Font("Lucida Fax", Font.PLAIN, 14));
		btnCancel.setBounds(372, 275, 153, 38);
		contentPane.add(btnCancel);
	}
}

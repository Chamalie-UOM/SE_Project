package GUAclasses;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FinalPage extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public FinalPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 230, 140));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Welcome Back!");
		lblNewLabel.setFont(new Font("Lucida Fax", Font.BOLD, 30));
		lblNewLabel.setBounds(214, 11, 296, 50);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		panel.setBounds(10, 66, 682, 369);
		contentPane.add(panel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(FinalPage.class.getResource("/pics/welcome-text-animation-over-bokeh-background_r7aij_yx__F0006.png")));
		panel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Logout");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FinalPage.this.dispose();
				HomePage frame1 = new HomePage();
				frame1.setVisible(true);
			}
		});
		btnNewButton.setBounds(616, 11, 89, 23);
		contentPane.add(btnNewButton);
	}
}

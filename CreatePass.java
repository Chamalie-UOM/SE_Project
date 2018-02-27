package GUAclasses;

import static org.opencv.imgcodecs.Imgcodecs.imencode;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreatePass extends JFrame {

	private JPanel cpane;
	User user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//CreatePass frame = new CreatePass();
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
	public CreatePass(User user) {
		this.user =user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 532, 435);
		cpane = new JPanel();
		cpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cpane);
		cpane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(99, 23, 308, 284);
		cpane.add(panel);
		
		JLabel lblNewLabel = new JLabel("");
		panel.add(lblNewLabel);
		displayFace(lblNewLabel);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Registered to the system successfully.");
			}
		});
		btnRegister.setBounds(210, 339, 89, 23);
		cpane.add(btnRegister);
		
	}
	
	
	public void displayFace(JLabel lblNewLabel) {
		BufferedImage b=null;
		Mat img1 = user.getFace();
		MatOfByte mob =new MatOfByte();
		if (img1!=null) {
			imencode(".jpg",img1,mob);
			
			Image img;
			try {
				img = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
				b =(BufferedImage) img;	
				System.out.println("here");
				Graphics g= cpane.getGraphics();
				if(b!=null) {
					lblNewLabel.setIcon(new ImageIcon(b));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}

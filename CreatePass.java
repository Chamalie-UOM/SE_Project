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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;
public class CreatePass extends JFrame {
	
	private final int rows = 3; //You should decide the values for rows and cols variables
    private final int cols = 3;
    private final int chunks = rows * cols;
    private final int SPACING = 2;//spacing between split images
    private JLabel[] labels; //for the image chunks
    


	JPanel cpane;
	User user;
	Password ps =new Password();
	MysqlConnection db = MysqlConnection.getConnInstance();
	JButton btnRegister;
	JButton btnLogin;
	JButton btnUpdate;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
		setBounds(100, 100, 451, 447);
		cpane = new JPanel();
		cpane.setBackground(new Color(240, 230, 140));
		cpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cpane);
		cpane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		panel.setBounds(10, 11, 355, 352);
		cpane.add(panel);
		
		JLabel lblNewLabel = new JLabel("");
		panel.add(lblNewLabel);
		displayFace(lblNewLabel);
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(ps.getPassString());
				if(ps.getPassString().length()>=4) {
					db.savePassword(user.getName(), ps.getPassString());
					JOptionPane.showMessageDialog(null, "Registered to the system successfully.");
					CreatePass.this.dispose();
					HomePage frame4 = new HomePage();
					frame4.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(null, "Your password should contain minimum four clicks.");
				}
				
			}
		});
		btnRegister.setBounds(336, 374, 89, 23);
		cpane.add(btnRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(db.getPassword(user.getName()));
				System.out.println(ps.getPassString());
				if (ps.getPassString().equals(db.getPassword(user.getName()))) {
					JOptionPane.showMessageDialog(null, "Logged in successfully.");
				}else {
					JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.");
				}
			}
		});
		btnLogin.setBounds(336, 374, 89, 23);
		cpane.add(btnLogin);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				db.updatePassword(user.getName(), ps.getPassString());
				JOptionPane.showMessageDialog(null, "Password updated successfully.");
				HomePage frame1 = new HomePage();
				frame1.setVisible(true);
				CreatePass.this.dispose();
			}
		});
		btnUpdate.setBounds(336, 374, 89, 23);
		cpane.add(btnUpdate);
		
	}
	
	MouseListener ml = new MouseListener() {
		 public void mouseClicked(MouseEvent e) {
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			 ps.PassSeq.add(((JLabel) e.getSource()).getText());
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
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
				//System.out.println("here");
				/*Graphics g= cpane.getGraphics();
				if(b!=null) {
					lblNewLabel.setIcon(new ImageIcon(b));
				}*/
				initComponents(b);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private void initComponents(BufferedImage im) {

        BufferedImage[] imgs = getImages(im);

        //set contentpane layout for grid
        this.getContentPane().setLayout(new GridLayout(rows, cols, 2, 2));

        labels = new JLabel[imgs.length];
        
        //create JLabels with split images and add to frame contentPane
        for (int i = 0; i < imgs.length; i++) {
            labels[i] = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(imgs[i].getSource())));
            labels[i].setText(String.valueOf(i));
            labels[i].setFont(new java.awt.Font("Lucida Grande",1,0));
            this.getContentPane().add(labels[i]);
            labels[i].addMouseListener(ml);
        }
    }

    private BufferedImage[] getImages(BufferedImage image) {
       
        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                
                gr.dispose();
            }
        }
        return imgs;
    }
}

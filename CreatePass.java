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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JInternalFrame;
public class CreatePass extends JFrame {
	
	private final int rows = 3; //You should decide the values for rows and cols variables
    private final int cols = 3;
    private final int chunks = rows * cols;
    private final int SPACING = 2;//spacing between split images
    private JLabel[] labels; //for the image chunks
    private JButton btnCancel;
	private JLabel lblpassword;
	private JLabel countDown;
	private JLabel waitinglbl;
    
	JPanel cpane;
	User user;
	Password ps =new Password();
	MysqlConnection db = MysqlConnection.getConnInstance();
	JButton btnRegister;
	JButton btnLogin;
	JButton btnUpdate;
	int count;
	int confirm;
	String firstTry;
	int delay;


	/**
	 * Create the frame.
	 */
	public CreatePass(User user) {
		setTitle("Graphical Password");
		this.user =user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 447);
		cpane = new JPanel();
		cpane.setBackground(Color.LIGHT_GRAY);
		cpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cpane);
		cpane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 11, 292, 176);
		cpane.add(panel);
		
		lblpassword = new JLabel("Password");
		lblpassword.setFont(new Font("Lucida Fax", Font.BOLD, 16));
		panel.add(lblpassword);
		
		JLabel lblNewLabel = new JLabel("");
		panel.add(lblNewLabel);
		displayFace(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(312, 320, 113, 77);
		cpane.add(panel_1);
		panel_1.setLayout(null);
		
		
		//Registration process
		confirm=0;
		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Georgia", Font.PLAIN, 12));
		btnRegister.setBounds(0, 54, 84, 23);
		panel_1.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(ps.getPassString());
				if(ps.getPassString().length()>=4 && confirm==1) {
					if(firstTry.equals(ps.getPassString())) {
						db.savePassword(user.getName(), ps.getPassString());
						JOptionPane.showMessageDialog(null, "Registered to the system successfully.");
						CreatePass.this.dispose();
						HomePage frame4 = new HomePage();
						frame4.setVisible(true);
					}else {
						ps.PassSeq.clear();
						confirm=0;
						firstTry=null;
						JOptionPane.showMessageDialog(null, "The password you entered does not match the confirmation. Please re-enter.");
						btnRegister.setText("Register");
					}
				}else if(confirm<1){
					firstTry =ps.getPassString();
					ps.PassSeq.clear();
					JOptionPane.showMessageDialog(null, "pleasse enter your password again for confirmation.");
					btnRegister.setText("Confirm");
					confirm=1;	
				}else {
					JOptionPane.showMessageDialog(null, "Your password should contain minimum four clicks.");
				}
			}
		});
		
		//Counting Down process for unauthorized login
		delay =60;
		 ActionListener taskPerformer = new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		    	  btnLogin.setVisible(false);
		    	  btnCancel.setVisible(false);
		    	  countDown.setVisible(true);
		    	  waitinglbl.setVisible(true);
		    	  if (delay <= 0) {
		    		  countDown.setVisible(false);
		    		  waitinglbl.setVisible(false);
		    		  ps.PassSeq.clear();
		    		  btnLogin.setVisible(true);
		    		  btnCancel.setVisible(true);
		    		  delay =60;
		              ((Timer)evt.getSource()).stop(); 
		              
		          } else {
		        	  countDown.setText(Integer.toString(delay));
		              delay--;
		          }
		      }
		  };
		  Timer timer =new Timer(1000, taskPerformer);
		  timer.setRepeats(true);
		
		//Login process
		count=0;
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Georgia", Font.PLAIN, 12));
		btnLogin.setBounds(0, 54, 84, 23);
		panel_1.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(db.getPassword(user.getName()));
				System.out.println(ps.getPassString());
				if (ps.getPassString().equals(db.getPassword(user.getName()))) {
					CreatePass.this.dispose();
					JOptionPane.showMessageDialog(null, "Logged in successfully.");
					FinalPage frame5 = new FinalPage();
					frame5.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(null, "Incorrect password. Please try again.");
					count++;
					if(count>2) {
						
						try {
							JOptionPane.showMessageDialog(null, "You have incorrectly entered the password three times. System will be locked for one minute.");
							timer.start();
							count =0;
							
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					ps.PassSeq.clear();
				}
			}
		});
		
		//Forgot password process
		confirm=0;
		btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Georgia", Font.PLAIN, 12));
		btnUpdate.setBounds(0, 54, 84, 23);
		panel_1.add(btnUpdate);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ps.getPassString().length()>=4 && confirm==1) {
					if(firstTry.equals(ps.getPassString())) {
						db.updatePassword(user.getName(), ps.getPassString());
						JOptionPane.showMessageDialog(null, "Password updated successfully.");
						HomePage frame1 = new HomePage();
						frame1.setVisible(true);
						CreatePass.this.dispose();
					}else {
						ps.PassSeq.clear();
						confirm=0;
						firstTry=null;
						JOptionPane.showMessageDialog(null, "The password you entered does not match the confirmation. Please re-enter.");
						btnUpdate.setText("Update");
					}	
				}else if(confirm<1){
					firstTry =ps.getPassString();
					ps.PassSeq.clear();
					JOptionPane.showMessageDialog(null, "pleasse enter your password again for confirmation.");
					btnUpdate.setText("Confirm");
					confirm=1;	
				}else {
					JOptionPane.showMessageDialog(null, "Your password should contain minimum four clicks.");
				}
			}
		});
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		panel_2.setBounds(174, 320, 128, 77);
		cpane.add(panel_2);
		panel_2.setLayout(null);
		
		waitinglbl = new JLabel("Counting:");
		waitinglbl.setFont(new Font("Lucida Fax", Font.BOLD, 14));
		waitinglbl.setBounds(0, 20, 84, 23);
		panel_1.add(waitinglbl);
		waitinglbl.setVisible(false);
		
		countDown = new JLabel("60");
		countDown.setBounds(41, 11, 29, 32);
		panel_2.add(countDown);
		countDown.setFont(new Font("Lucida Fax", Font.BOLD, 17));
		countDown.setVisible(false);
		
		
		//cancelling process
		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Georgia", Font.PLAIN, 12));
		btnCancel.setBounds(0, 54, 87, 23);
		panel_2.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnRegister.isVisible()) {
					db.deleteEntry(user.getName());
				}
				CreatePass.this.dispose();
				HomePage frame1 = new HomePage();
				frame1.setVisible(true);
			}
		});	
		
	}
	
	//Tracking the password
	MouseListener ml = new MouseListener() {
		 public void mouseClicked(MouseEvent e) {	
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			 ps.PassSeq.add(((JLabel) e.getSource()).getText());
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
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
				initComponents(b);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initComponents(BufferedImage im) {
        BufferedImage[] imgs = getImages(im);
        //set content pane layout for grid
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

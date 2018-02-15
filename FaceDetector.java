package authentication;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import static org.opencv.imgcodecs.Imgcodecs.imencode; 
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.sql.*;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
public class FaceDetector extends JFrame {
	
	
	//definitions
	private DaemonThread thread1 = null;
	int count =0;
	Mat frame = new Mat();
	MatOfByte new1 = new MatOfByte();
	CascadeClassifier faceTracker = new CascadeClassifier(FaceDetector.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
	MatOfRect trackedFaces= new MatOfRect();
	VideoCapture webSource;
	MysqlConnection db = new MysqlConnection();
	Connection con = db.dbConnect();
	class DaemonThread implements Runnable{
		protected volatile boolean Runnable= false;
		
		public void run() {
			synchronized(this) {
				while(Runnable) {
					if(webSource.grab()) {
						try {
							webSource.retrieve(frame);
							Graphics g = contentPane.getGraphics();
							//detect faces in the frame.
							faceTracker.detectMultiScale(frame, trackedFaces);
							//draw rectangles around the detected faces.
							for(Rect rect : trackedFaces.toArray()) {
								Imgproc.rectangle(frame, new Point(rect.x,rect.y), new Point(rect.x + rect.width,rect.y+rect.height), new Scalar(255, 255, 0), 2);
							}
							//Convert Mat to MatOfByte.
							imencode(".bmp",frame,new1);
							//Convert it to a byte array and pass it to InputStream and read, to obtain a BufferedImage object.
							Image im= ImageIO.read(new ByteArrayInputStream(new1.toArray()));
							BufferedImage buff =(BufferedImage) im;	
							//display the frames on the panel
							if(g.drawImage(buff, 10, 10, getWidth()-30, getHeight() - 185, 0, 0, buff.getWidth(),buff.getHeight(), null)) {
								if(Runnable==false) {
									System.out.println("paused.");
									this.wait();
								}
							} 
							//Statement st =db.conn.createStatement();
							
							if (con!= null) {
								//Statement st =con.createStatement();
								//st.executeUpdate("insert into user(name, email) values('what','the@gmail.com');");
								System.out.println("reached here.");
								String query ="insert into user (name, email, face) values(?,?, buff);";
								PreparedStatement stmt =db.conn.prepareStatement(query);
								stmt.setString(1, textFieldname.getText());
								stmt.setString(2, textFieldmail.getText());
								stmt.execute();
								
							}/*else {
								System.out.println("connection is null.");
							}
							*/
						}catch (Exception ex) {
							System.out.println("Error");
						}
					
					}
				}
			}
		}
	}
	
	private JPanel contentPane;
	private JTextField textFieldname;
	private JTextField textFieldmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FaceDetector frame = new FaceDetector();
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
	public FaceDetector() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 629, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(78, 11, 410, 295);
		contentPane.add(panel);
		
		JButton login = new JButton("Next");
		login.setFont(new Font("Georgia", Font.PLAIN, 15));
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				webSource = new VideoCapture();
				webSource.open(0);
				
				if(webSource.isOpened()) {
					System.out.println("should work");
				}
				thread1 = new DaemonThread();
				Thread t = new Thread(thread1);
				t.setDaemon(true);
				thread1.Runnable=true;
				t.start();
				login.setEnabled(false);
			}
		});
		login.setBounds(249, 430, 147, 23);
		contentPane.add(login);
		
		textFieldname = new JTextField();
		textFieldname.setBounds(252, 317, 286, 36);
		contentPane.add(textFieldname);
		textFieldname.setColumns(10);
		
		textFieldmail = new JTextField();
		textFieldmail.setColumns(10);
		textFieldmail.setBounds(252, 370, 286, 36);
		contentPane.add(textFieldmail);
		
		JLabel lblUserName = new JLabel("User Name");
		lblUserName.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblUserName.setBounds(153, 317, 89, 36);
		contentPane.add(lblUserName);
		
		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setFont(new Font("Georgia", Font.PLAIN, 15));
		lblEmail.setBounds(153, 370, 89, 36);
		contentPane.add(lblEmail);
	}
}

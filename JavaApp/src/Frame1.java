import java.awt.EventQueue;
//import com.sql,DriverManager;
//import com.mysql.jdbc.Connection;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Frame1 {
//	public static 
	private JFrame frame;
	static int week = 0;
	boolean renderLibraryUI = true;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementDB", "root" , "Iviundhacthi8987m");
			
			// 2. Create a statement
			myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			myRs = myStmt.executeQuery("select * from Book");
			
			// 4. Process the result set
			while (myRs.next()) {
				System.out.println(myRs.getString("name") + ", " + myRs.getString("author"));
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}

		// uncomment block below to run UI execute 50 exercise Java
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		if (!renderLibraryUI) {
			initialize();
		} else {
			initLibraryManagementUI();
		}
	}
	private void initLibraryManagementUI() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 131);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton insertBookFormButton = new JButton("Thêm sách vào thư viện");
		insertBookFormButton.setBounds(0, 0, 200, 50);
		insertBookFormButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				week = 1;
				InsertBookForm week1 = new InsertBookForm();
				week1.pack();
				week1.setSize(800, 800);
				week1.setVisible(true);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(insertBookFormButton);
		

		JButton insertReaderButton = new JButton("Đăng kí thẻ độc giả");
		insertReaderButton.setBounds(200, 0, 200, 50);
		insertReaderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				week = 1;
				InsertReader week1 = new InsertReader();
				week1.pack();
				week1.setSize(800, 800);
				week1.setVisible(true);
			}
		});
		frame.getContentPane().add(insertReaderButton);
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 131);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("Bài tập Java tuần 1");
		btnNewButton.setBounds(0, 60, 200, 50);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				week = 1;
				Week1Dialog week1 = new Week1Dialog();
				week1.pack();
				week1.setSize(800, 800);
				week1.setVisible(true);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Bài tập Java tuần 2");
		btnNewButton_1.setBounds(200, 60, 200, 50);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				week = 2;
				Week1Dialog week1 = new Week1Dialog();
				week1.pack();
				week1.setSize(800, 800);
				week1.setVisible(true);
			}
		});
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Bài tập Java tuần 3");
		btnNewButton_2.setBounds(400, 60, 200, 50);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				week = 3;
				Week1Dialog week1 = new Week1Dialog();
				week1.pack();
				week1.setSize(800, 800);
				week1.setVisible(true);
			}
		});
		frame.getContentPane().add(btnNewButton_2);

		JLabel infoStudentName = new JLabel("Vũ Quý Đạt - MSSV: 20176082");
		infoStudentName.setBounds(10, -20, 600, 80);
		frame.getContentPane().add(infoStudentName);
		JLabel infoStudentClass = new JLabel("Lớp: Vuwit16b");
		infoStudentClass.setBounds(10, 0, 600, 80);
		frame.getContentPane().add(infoStudentClass);
	}
}
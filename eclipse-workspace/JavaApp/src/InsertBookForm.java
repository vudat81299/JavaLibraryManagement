import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumnModel;
//import javax.swing.table.TableModel;
//import javax.swing.table.DefaultTableCellRenderer;

//import java.util.Arrays; 
//import java.util.ArrayList; 
//import java.util.List;

import java.sql.*;
import java.util.Vector;
//import java.util.*; 
//import java.util.Arrays; 
import java.util.Collections;
//import java.util.Comparator; 

public class InsertBookForm extends JDialog {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// variables, definitions
//	private static final TableModelListener InsertBookForm = null;
	
	// UI
	JLabel name;
	JTextField nameInput;
	JLabel type;
	JTextField typeInput;
	JLabel author;
	JTextField authorInput;
	JLabel nxb;
	JTextField nxbInput;
	JLabel publisedhDate;
	JTextField publisedhDateInput;
	JLabel dataType;
	JTextField dataTypeInput;
	JButton addBookButton;
	JButton removeBookButton;
	JButton cancelRemove;
	JButton confirmRemove;
	JLabel askRemove;

	JTextField searchInput;
	JButton searchButton;
	JButton comfirmBorrowButton;
	JButton addBookIntoCartButton;
	JButton removeBookFromCartButton;
	JButton doneSearchButton;
	JButton reload;

	JLabel errorLabel;
	JLabel titleTable;
	JScrollPane sp;
	JTable jt;
	JLabel ownerMark;
	
	// init default data for variables, definitions
	boolean pressingCTRL=false;//flag, if pressing CTRL it is true, otherwise it is false.
//	Vector<int[]> selectedCells = new Vector<int[]>();
//	Vector<int> selectedCells = new Vector<int>();
    Vector<String> selectedCells = new Vector<String>();

	static DefaultTableModel model;
	int countRow = 0; // total row of table 
	int countSelectedBook = 0;
	String searchField;
	
	
	// info of DB
	String listColumn[] = {"id", "name", "type", "author", "publisher", "publishedDate", "dataType", "state"}; // list column of DB
	String url;
	String username;
	String password;
	String tableName;
	// DB instance
	Connection myConn;
	Statement myStmt;
	ResultSet myRs;
	
	boolean isAddition;
	boolean isRemoval;
	boolean isSelectedBook;
	boolean isRemoveBookFromCart;
	boolean isSearching;
	String insertQuery;
	String removeQuery;
	String selectAllQuery;
	
	public InsertBookForm() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle("Quản lý đầu sách thư viện - Vũ Quý Đạt 20176082");
		// initializers - not for array constants

		url = "jdbc:mysql://localhost:3306/";
		username = "root";
		password = "Iviundhacthi8987m";
		tableName = "LibraryManagementDB";
		isAddition = false;

		insertQuery = "insert into Book (id, name, type, author, publisher, publishedDate, dataType)"
					+ " values (?, ?, ?, ?, ?, ?, ?)";
		removeQuery = "DELETE FROM Book WHERE id = ?";
		selectAllQuery = "select * from Book";
		
		// force call 
		prerequisiteSetting();
		renderUIElement();
		loadDataFromDB();
		
		//
		KeyListener tableKeyListener = (KeyListener) new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			    isRemoval = true;
				if(e.getKeyCode()==157){//check if user is pressing CTRL key
					pressingCTRL=true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			    isRemoval = true;
				if(e.getKeyCode()==157){//check if user released CTRL key
					pressingCTRL=false;
				}
			}
		};
		
		MouseListener tableMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("keyevent: " + e);
				if (pressingCTRL == true) { //check if user is pressing CTRL key
				    isRemoval = true;
					int row = jt.rowAtPoint(e.getPoint()); //get mouse-selected row
					String newEntry = "" + row; //{row,col}=selected cell
					if (selectedCells.contains(newEntry)) {
						//cell was already selected, deselect it
						selectedCells.remove(newEntry);
//						print("is contain!");
					} else {
						//cell was not selected
						selectedCells.add(newEntry);
//						print("is not contain!");
					}
				} else {
				    isRemoval = false;
//					print("is single select mode!");
				    selectedCells.clear();
					int row = jt.rowAtPoint(e.getPoint());
					String newEntry = "" + row;
				    selectedCells.add(newEntry);
				}
			    System.out.println("list cell selected" + selectedCells);
			}
		};
		jt.addKeyListener(tableKeyListener);
		jt.addMouseListener(tableMouseListener);
		
		//
		addBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validate("")) {
					System.out.print(validate(""));
					return;
				}

				try {
					// 1. Get a connection to database
					myConn = DriverManager.getConnection(url + tableName, username, password);
					
					// 2. Create a statement
					myStmt = myConn.createStatement();
					
					// the mysql insert statement
				    String query = insertQuery;

				    // create the mysql insert preparedstatement
				    PreparedStatement preparedStmt = myConn.prepareStatement(query);
				    preparedStmt.setInt    (1, nextCreatingIDBook());
				    preparedStmt.setString (2, nameInput.getText());
				    preparedStmt.setString (3, typeInput.getText());
				    preparedStmt.setString (4, authorInput.getText());
				    preparedStmt.setString (5, nxbInput.getText());
				    preparedStmt.setString (6, publisedhDateInput.getText());
				    preparedStmt.setString (7, dataTypeInput.getText());
	
				    // execute the preparedstatement
				    System.out.println(preparedStmt);
				    preparedStmt.execute();
				    isAddition = true;
				    model.addRow(new Object[]{nextCreatingIDBook(), nameInput.getText(), typeInput.getText(), authorInput.getText(), 
				    		nxbInput.getText(), publisedhDateInput.getText(), dataTypeInput.getText()});	

					// force call last
				    myConn.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		
		removeBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				cancelRemove.setVisible(true);
				confirmRemove.setVisible(true);
				askRemove.setVisible(true);
				print("-----");
//				cancelRemove.setVisible(true);
//				confirmRemove.setVisible(true);
				removeGroupRemove();
			}
		});
		
		cancelRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				print("-confirmRemove");
				cancelRemove.setVisible(false);
				confirmRemove.setVisible(false);
				askRemove.setVisible(true);
			}
		});
		
		confirmRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedCells.size() > 0) {
					
				} else {
					return;
				}

				try {
					// 1. Get a connection to database
					myConn = DriverManager.getConnection(url + tableName, username, password);
					
					// 2. Create a statement
					myStmt = myConn.createStatement();
					
					// the mysql insert statement
				    String query = removeQuery;
				    System.out.println(selectedCells);
				    print("removing");
				    // create the mysql insert preparedstatement
//				    PreparedStatement preparedStmt = myConn.prepareStatement(query);
//				    PreparedStatement preparedStmt = myConn.prepareStatement(query);
				    
				    // execute the preparedstatement
//				    model.addRow(new Object[]{countRow, nameInput.getText(), typeInput.getText(), authorInput.getText(), 
//				    		nxbInput.getText(), publisedhDateInput.getText(), dataTypeInput.getText()});	


//				    Comparator<Object> comparator = Collections.reverseOrder();
//				    Collections.sort(selectedCells,comparator);
			        Collections.sort(selectedCells);  
//				    while (true) {
//				    	int loop = 0;
//				    	for (loop = 0; loop < selectedCells.size(); loop++) {
//				    		if (Integer.parseInt(model.getValueAt(Integer.parseInt(selectedCells.get(loop)), 0).toString()) < Integer.parseInt(model.getValueAt(Integer.parseInt(selectedCells.get(loop)), 0).toString())) {
//				    			
//				    		}
//				    		
//				    		if (loop == selectedCells.size() - 1) {
//				    			break;
//				    		}
//				    	}
//				    	break;
//				    }
		            for (int i = selectedCells.size() - 1; i > -1; i--) {
		            	int cellIndex = 0;
		            	String cellIndexString = "";
		            	for (int j = 0; j < selectedCells.size(); j++) {
		            		if (Integer.parseInt(selectedCells.get(j)) > cellIndex) {
		            			cellIndex = Integer.parseInt(selectedCells.get(j));
		            			cellIndexString = selectedCells.get(j);
		            		}
		            	}
		            	selectedCells.remove(cellIndexString);
					    isRemoval = true;
					    PreparedStatement preparedStmt = myConn.prepareStatement(query);
		            	int foo;
		            	try {
		            	   foo = Integer.parseInt(cellIndexString);
		            	}
		            	catch (NumberFormatException er)
		            	{
		            	   foo = 0;
		            	}
		            	if (model.getValueAt(foo, 7) == null) {
		            		
		            	} else if (model.getValueAt(foo, 7).equals("Chọn")) {
		            		countSelectedBook--;
		                	comfirmBorrowButton.setText("Mượn (" + countSelectedBook + ")");
		            	}
			            if (i == 0 && selectedCells.size() > 0) {
			            	preparedStmt.setInt (1, Integer.parseInt(model.getValueAt(Integer.parseInt(selectedCells.get(0)), 0).toString()));
			            	preparedStmt.executeUpdate();
				            model.removeRow(foo);
			            	selectedCells.remove(selectedCells.get(0));
						    isRemoval = false;
			            } else {
			            	preparedStmt.setInt (1, Integer.parseInt(model.getValueAt(foo, 0).toString()));
			            	preparedStmt.executeUpdate();
				            model.removeRow(foo);
			            }
		            	print("---------");
		            }

	            
					// force call last
				    myConn.close();
				} catch (Exception exc) {
					System.out.println(exc);
					exc.printStackTrace();
				}

				cancelRemove.setVisible(false);
				confirmRemove.setVisible(false);
				askRemove.setVisible(false);
			}
		});

		doneSearchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = model.getRowCount() - 1; i > -1 ; i--) {
					isSearching = true;
					model.removeRow(i);
				}
				loadDataFromDB();
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = model.getRowCount() - 1; i > -1 ; i--) {
					isSearching = true;
					model.removeRow(i);
				}

				try {
					// 1. Get a connection to database
					myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementDB", "root" , "Iviundhacthi8987m");
					
					// 2. Create a statement
					myStmt = myConn.createStatement();
					getSearchText();
					// the mysql insert statement
				    String query = "SELECT * FROM Book WHERE " + searchField + " LIKE " + getSearchText();

					System.out.println(query);
					// 3. Execute SQL query
					myRs = myStmt.executeQuery("select * from Book WHERE " + searchField + " LIKE '" + getSearchText() + "'");// 4. Process the result set
					while (myRs.next()) {
						System.out.println(myRs.getString("name") + ", " + myRs.getString("name"));
						isSearching = true;
					    String a = myRs.getString("id");
					    String b = myRs.getString("name");
					    String c = myRs.getString("type");
					    String d = myRs.getString("author");
					    String ee = myRs.getString("publisher");
					    String f = myRs.getString("publishedDate");
					    String g = myRs.getString("dataType");
					    String h = myRs.getString("state");
					    model.addRow(new Object[]{a, b, c, d, ee, f, g, h});

					}
					countRow = model.getRowCount();

		            myConn.close();
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});


		comfirmBorrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RentManagement rentManagement = new RentManagement();
				rentManagement.pack();
				rentManagement.setBounds(0, 0, 1200, 800);
				rentManagement.setVisible(true);
			}
		});
		setLayout(null);

		//
		addBookIntoCartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	int foo;
            	String cellIndexString = "";
				if (selectedCells.size() == 0) {
					
					return;
				} else {
		            for (int i = selectedCells.size() - 1; i > -1; i--) {

		            	cellIndexString = selectedCells.get(i);
//		            	selectedCells.remove(cellIndexString);
		            	
		            	try {
		            	   foo = Integer.parseInt(cellIndexString);
		            	}
		            	catch (NumberFormatException er)
		            	{
		            	   foo = 0;
		            	}
		            	
						if (model.getValueAt(foo, 7) == null || model.getValueAt(foo, 7).equals("")) { 
							countSelectedBook++;
							isSelectedBook = true;
							model.setValueAt("Chọn", foo, 7);
//							listSelectedItem.addRow(model.getDataVector().elementAt(0));
							print("++++");
//							System.out.println(listSelectedItem.getValueAt(0, 0));
			            	comfirmBorrowButton.setText("Giỏ sách (" + countSelectedBook + ")");
		            	}
		            }
				}
			}
		});
		
		removeBookFromCartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	            for (int i = selectedCells.size() - 1; i > -1; i--) {
	            	int cellIndex = 0;
	            	String cellIndexString = "";
//	            	for (int j = 0; j < selectedCells.size(); j++) {
//	            		if (Integer.parseInt(selectedCells.get(j)) > cellIndex) {
	            			cellIndex = Integer.parseInt(selectedCells.get(i));
	            			cellIndexString = selectedCells.get(i);
//	            		}
//	            	}
//	            	selectedCells.remove(cellIndexString);
	            	int foo;
	            	try {
	            	   foo = Integer.parseInt(cellIndexString);
	            	}
	            	catch (NumberFormatException er)
	            	{
	            	   foo = 0;
	            	}
	            	
	            	if (model.getValueAt(foo, 7) == null || model.getValueAt(foo, 7).equals("")) {
	            	} else if (model.getValueAt(foo, 7).equals("Chọn")) {
	            		isRemoveBookFromCart = true;
	            		countSelectedBook--;
						model.setValueAt("", foo, 7);
	                	comfirmBorrowButton.setText("Giỏ sách (" + countSelectedBook + ")");
	            	}
	            }
			}
		});
		
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataFromDB();
				comfirmBorrowButton.setText("Giỏ sách (0)");
			}
		});

	    jt.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
            	print("---------");
				System.out.println(e);
				if (isAddition || isRemoval || isSelectedBook || isRemoveBookFromCart || isSearching) {
					System.out.println(isAddition);
	            	print("--1-------");
					System.out.println(isRemoval);
	            	print("---2------");
					System.out.println(isSelectedBook);
	            	print("----3-----");
					System.out.println(isRemoveBookFromCart);
	            	print("---4------");
					System.out.println(isSearching);
	            	print("---5------");
					
					print("can not edit row!");
					isAddition = false;
					isRemoval = false;
					isSelectedBook = false;
					isRemoveBookFromCart = false;
					isSearching = false;
				} else {
					// TODO Auto-generated method stub
					print("editting row!");
			        int row = e.getFirstRow();
			        int column = e.getColumn();
			        if (column == 0) {
						print("Can not edit id!");
			        	return;
			        }
			        
			        String sqlupdate = "UPDATE Book SET " + listColumn[column] + " = '" + model.getValueAt(row, column) + "' WHERE id = " + model.getValueAt(row, 0);
			        print(sqlupdate);
					if(column != 0) {
						try {
							// 1. Get a connection to database
							myConn = DriverManager.getConnection(url + tableName, username, password);
							
							// 2. Create a statement
							myStmt = myConn.createStatement();
							
				            PreparedStatement pst = myConn.prepareStatement(sqlupdate);
				            pst.executeUpdate();
	
							// force call last
				            myConn.close();
						} catch (Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			}
        });
	}
	
void removeGroupRemove() {

	new java.util.Timer().schedule( 
	        new java.util.TimerTask() {
	            @Override
	            public void run() {
	                // your code here
	cancelRemove.setVisible(false);
	confirmRemove.setVisible(false);
	askRemove.setVisible(false);
	            }
	        }, 
	        5000 
	);
}

	// methods
	String getSearchText() {
		if (nameInput.getText() != null && !nameInput.getText().contentEquals("")) {
			searchField = "name";
			return nameInput.getText();
		} else if (typeInput.getText() != null && !typeInput.getText().contentEquals("")) {
			searchField = "type";
			return typeInput.getText();
		} else if (authorInput.getText() != null && !authorInput.getText().contentEquals("")) {
			searchField = "author";
			return authorInput.getText();
		} else if (nxbInput.getText() != null && !nxbInput.getText().contentEquals("")) {
			searchField = "publisher";
			return nxbInput.getText();
		} else if (publisedhDateInput.getText() != null && !publisedhDateInput.getText().contentEquals("")) {
			searchField = "publishedDate";
			return publisedhDateInput.getText();
		} else if (dataTypeInput.getText() != null && !dataTypeInput.getText().contentEquals("")) {
			searchField = "dataType";
			return dataTypeInput.getText();
		}
		return "";
	}
	
	int nextCreatingIDBook () {
		if (model.getRowCount() == 0) {
			return 1;
		}
		return Integer.parseInt(model.getValueAt(model.getRowCount() - 1, 0).toString()) + 1;
	}
	
	public boolean validate(String string) {
	    if (nameInput.getText().length() == 0 || 
	    		typeInput.getText().length() == 0 || 
	    		authorInput.getText().length() == 0 || 
	    		nxbInput.getText().length() == 0 ||
	    		publisedhDateInput.getText().length() == 0 || 
	    		dataTypeInput.getText().length() == 0) {
	    	print("Validate false!");
	    	return false;
	    } else {
	    	print("Validate true!");
			return true;
	    }
	}

	void print(String string) {
		System.out.println(string);
	}
	void loadDataFromDB() {
		while (model.getRowCount() > 0) {
			isAddition = true;
			model.removeRow(0);
		}
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection(url + tableName, username, password);
			
			// 2. Create a statement
			myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			myRs = myStmt.executeQuery(selectAllQuery);
			
			// 4. Process the result set
			while (myRs.next()) {

				isAddition = true; 
			    String a = myRs.getString("id");
			    String b = myRs.getString("name");
			    String c = myRs.getString("type");
			    String d = myRs.getString("author");
			    String e = myRs.getString("publisher");
			    String f = myRs.getString("publishedDate");
			    String g = myRs.getString("dataType");
			    String h = myRs.getString("state");
			    model.addRow(new Object[]{a, b, c, d, e, f ,g, h});

			}
			countRow = model.getRowCount();
			
			// force call last
            myConn.close();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	// prerequisite setting for this dialog type view
	void prerequisiteSetting() {
		pack();
		setModal(true);
		setResizable(true);
		getContentPane().setLayout(null);
	}
	
	void renderUIElement() {
		//
		name = new JLabel("Tên đầu sách");
		name.setBounds(10, 0, 100, 50);
		nameInput = new JTextField("", 5);
		nameInput.setBounds(130, 0, 200, 50);

		//
		type = new JLabel("Loại sách");
		type.setBounds(10, 50, 100, 50);
		typeInput = new JTextField("", 5);
		typeInput.setBounds(130, 50, 200, 50);
		
		//
		author = new JLabel("Tác giả");
		author.setBounds(10, 100, 100, 50);
		authorInput = new JTextField("", 5);
		authorInput.setBounds(130, 100, 200, 50);

		//
		nxb = new JLabel("Nhà xuất bản");
		nxb.setBounds(400, 0, 100, 50);
		nxbInput = new JTextField("", 5);
		nxbInput.setBounds(510, 0, 200, 50);

		//
		publisedhDate = new JLabel("Ngày xuất bản");
		publisedhDate.setBounds(400, 50, 100, 50);
		publisedhDateInput = new JTextField("", 5);
		publisedhDateInput.setBounds(510, 50, 200, 50);

		//
		dataType = new JLabel("Kiểu sách");
		dataType.setBounds(400, 100, 100, 50);
		dataTypeInput = new JTextField("", 5);
		dataTypeInput.setBounds(510, 100, 200, 50);

		// button
		addBookButton = new JButton("Thêm đầu sách vào thư viện");
		addBookButton.setBounds(800, 0, 225, 50);
		removeBookButton = new JButton("Xoá");
		removeBookButton.setBounds(1025, 0, 85, 50);
		
		//
		searchButton = new JButton("Tìm kiếm");
		searchButton.setBounds(800, 50, 90, 50);
		comfirmBorrowButton = new JButton("Giỏ sách (" + countSelectedBook + ")");
		comfirmBorrowButton.setBounds(1006, 50, 104, 50);
		doneSearchButton = new JButton("Tìm kiếm xong");
		doneSearchButton.setBounds(890, 50, 116, 50);
		
		//
		addBookIntoCartButton = new JButton("Thêm sách vào giỏ sách");
		addBookIntoCartButton.setBounds(800, 100, 225, 50);
		removeBookFromCartButton = new JButton("Bỏ chọn");
		removeBookFromCartButton.setBounds(1025, 100, 85, 50);
		
		// 
		
		errorLabel = new JLabel("", SwingConstants.CENTER);
		errorLabel.setBounds(10, 170, 1180, 50);
		errorLabel.setForeground(Color.RED);
		titleTable = new JLabel("Bảng danh sách các đầu sách trong thư viện");
		reload =  new JButton("Reload danh sách");
		reload.setBounds(1040, 220, 150, 30);
		titleTable.setBounds(10,-20,780,500);
		model = new DefaultTableModel(new Object[]{"Mã sách", "Tên sách", "Thể loại", "Tác giả", "Nhà xuất bản", "Ngày xuất bản", "Kiểu sách", "Trạng thái"}, 0);
//		listSelectedItem = new DefaultTableModel(new Object[]{"Mã sách", "Tên sách", "Thể loại", "Tác giả", "Nhà xuất bản", "Ngày xuất bản", "Kiểu sách", "Trạng thái"}, 0);
	    jt = new JTable(model) {
	    	private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				if (column == 0 || column == 7) {
	    	        return false;
				}
				return true;
	   	    }
	    };
	    jt.getTableHeader().setReorderingAllowed(false);
	    jt.setBounds(10,250,1180,500); 
	    jt.getColumnModel().getColumn(0).setPreferredWidth(20);
	    jt.getColumnModel().getColumn(1).setPreferredWidth(120);
	    jt.getColumnModel().getColumn(2).setPreferredWidth(60);
	    jt.getColumnModel().getColumn(3).setPreferredWidth(120);
	    jt.getColumnModel().getColumn(4).setPreferredWidth(20);
	    jt.getColumnModel().getColumn(5).setPreferredWidth(20);
	    jt.getColumnModel().getColumn(6).setPreferredWidth(20);
	    jt.getColumnModel().getColumn(7).setPreferredWidth(20);
	    jt.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	    sp = new JScrollPane(jt); 
	    sp.setBounds(10,250,1180,440);

	    ownerMark = new JLabel("Vũ Quý Đạt - MSSV: 20176082 - Lớp: Vuwit16b");
		ownerMark.setBounds(10,700,1180,30);
		


	    askRemove = new JLabel("Bạn có chắc chắn muốn xoá những sách đã chọn!", SwingConstants.CENTER);
	    askRemove.setBounds(100, 160, 1000, 30);
		
		cancelRemove =  new JButton("Huỷ");
		cancelRemove.setBounds(500, 190, 80, 30);
		cancelRemove.setForeground(Color.RED);
		

		confirmRemove =  new JButton("Xoá");
		confirmRemove.setBounds(580, 190, 80, 30);
		confirmRemove.setBackground(Color.BLUE);
		confirmRemove.setForeground(Color.BLUE);
		add(cancelRemove);
		add(confirmRemove);
		add(askRemove);

		askRemove.setVisible(false);
		cancelRemove.setVisible(false);
		confirmRemove.setVisible(false);
	    
		add(name);
		add(nameInput);
		add(type);
		add(typeInput);
		add(author);
		add(authorInput);
		add(nxb);
		add(nxbInput);
		add(publisedhDate);
		add(publisedhDateInput);
		add(dataType);
		add(dataTypeInput);
	
		add(addBookButton); // button
		add(removeBookButton);
		
		add(searchButton);
		add(comfirmBorrowButton);
		add(doneSearchButton);
		
		add(addBookIntoCartButton);
		add(removeBookFromCartButton);

		add(errorLabel);   
		add(titleTable);   
		add(reload);   
	    add(sp); // scroll pane
	    add(ownerMark);
	    
	    // set centre header for table
//	    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) jt.getTableHeader().getDefaultRenderer();
//	    renderer.setHorizontalAlignment(0);         
//	    setVisible(true);    
	}

}

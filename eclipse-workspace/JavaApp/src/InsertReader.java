import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumnModel;
//import javax.swing.table.TableModel;
//import javax.swing.table.DefaultTableCellRenderer;

import java.util.Arrays; 
//import java.util.ArrayList; 
//import java.util.List;

public class InsertReader extends JDialog {
	private static final TableModelListener InsertReader = null;
	int countRow = 0;

	Connection myConn = null;
	Statement myStmt = null;
	ResultSet myRs = null;
	DefaultTableModel model = new DefaultTableModel(new String[]{"Mã độc giả", "Họ và Tên", "Số điện thoại", "Mã thẻ mượn sách"}, 0);
	
	String listColumn[] = {"id", "name", "phonenumber", "idCard"};
	static <T> T[] append(T[] arr, T element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	void loadData() {
//		model = null;
//		model = new DefaultTableModel(new String[]{"Mã sách", "Tên sách", "Thể loại", "Tác giả", "Nhà xuất bản", "Ngày xuất bản", "Kiểu sách"}, 0);

		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementDB", "root" , "Iviundhacthi8987m");
			
			// 2. Create a statement
			myStmt = myConn.createStatement();
			
			// 3. Execute SQL query
			myRs = myStmt.executeQuery("select * from Reader");
			
			// 4. Process the result set
			while (myRs.next()) {
				System.out.println(myRs.getString("phonenumber") + ", " + myRs.getString("name"));

			    String a = myRs.getString("id");
			    String b = myRs.getString("name");
			    String c = myRs.getString("phonenumber");
			    String d = myRs.getString("idCard");
			    model.addRow(new Object[]{a, b, c, d});

			}
			countRow = model.getRowCount();

            myConn.close();
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public InsertReader() {
		loadData();
		
		pack();
		setModal(true);
		setResizable(true);
		getContentPane().setLayout(null);
		
		//
		JTextField nameInput = new JTextField("", 5);
		nameInput.setBounds(160, 0, 200, 50);
		add(nameInput);

		JLabel name = new JLabel("Họ và tên");
		name.setBounds(10, 0, 100, 50);
		add(name);

		//
		JTextField typeInput = new JTextField("", 5);
		typeInput.setBounds(160, 50, 200, 50);
		add(typeInput);

		JLabel type = new JLabel("Số điện thoại");
		type.setBounds(10, 50, 100, 50);
		add(type);

		//
		JTextField publisedhDateInput = new JTextField("", 5);
		publisedhDateInput.setBounds(510, 0, 200, 50);
		add(publisedhDateInput);

		JLabel publisedhDate = new JLabel("Mã thẻ");
		publisedhDate.setBounds(370, 0, 100, 50);
		add(publisedhDate);

		JButton addBookButton = new JButton("Đăng kí thông tin người đọc");
		addBookButton.setBounds(300, 150, 200, 50);
		addBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// 1. Get a connection to database
					myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementDB", "root" , "Iviundhacthi8987m");
					
					// 2. Create a statement
					myStmt = myConn.createStatement();
					
					  // the mysql insert statement
				      String query = " insert into Reader (id, name, phonenumber, idCard)"
				        + " values (?, ?, ?, ?)";

			      // create the mysql insert preparedstatement
			      PreparedStatement preparedStmt = myConn.prepareStatement(query);
			      preparedStmt.setInt    (1, ++countRow);
			      preparedStmt.setString (2, nameInput.getText());
			      preparedStmt.setString (3, typeInput.getText());
			      preparedStmt.setString (4, publisedhDateInput.getText());

			      // execute the preparedstatement
			      preparedStmt.execute();
			      
			      myConn.close();

//					loadData();
//					model.addRow(new Object[]{1, "Column 2", "Column 3", "Column 3", "Column 3"});
				    model.addRow(new Object[]{countRow, nameInput.getText(), typeInput.getText(), publisedhDateInput.getText()});
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
		add(addBookButton);
		

		JLabel titleTable = new JLabel("Bảng danh sách độc giả đã đăng kí");
		titleTable.setBounds(10,-20,780,500);
		add(titleTable);
	    JTable jt = new JTable(model);    
	    jt.setBounds(10,250,780,500);          
	    jt.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub

		        int row = e.getFirstRow();
		        int column = e.getColumn();
		        if (column == 0) {
		        	return;
		        }
		        Object data = model.getValueAt(row, column);
		        
		        String sqlupdate = "UPDATE Reader SET " + listColumn[column] + " = '" + data + "' WHERE id = " + ++row;
				if(column != 0) {
					try {
						// 1. Get a connection to database
						myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementDB", "root" , "Iviundhacthi8987m");
						
						// 2. Create a statement
						myStmt = myConn.createStatement();
						
			            PreparedStatement pst = myConn.prepareStatement(sqlupdate);
			            pst.executeUpdate();
			            myConn.close();
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}
        });
//	    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) jt.getTableHeader().getDefaultRenderer();
//	    renderer.setHorizontalAlignment(0);
	    JScrollPane sp = new JScrollPane(jt); 
	    sp.setBounds(10,250,780,440);      
	    add(sp);             
//	    setVisible(true);    
	}

}
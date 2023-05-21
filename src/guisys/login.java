package guisys;

import guisys.AdminLogin;
import guisys.MainSys;
import guisys.Database;
import guisys.StudentReg;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class login extends JFrame {
	private JLabel regclick;
	private JPanel contentPane;
	private final JPanel left = new JPanel();

	private String currentStudentId;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public login() throws SQLException {
		final Connection conn = Database.getConnection();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\CHRONOS\\Dropbox\\My PC (DESKTOP-MB5HNEI)\\Documents\\Course Evaluation System\\CourseEvalSystem\\faith_logo.jpg"));
		setTitle("FAITH Course Evaluation System [Student Login]");

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 627, 432);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(64, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		left.setBackground(new Color(152, 173, 207));
		left.setBounds(0, 0, 270, 399);
		contentPane.add(left);
		left.setLayout(null);

		JLabel intro = new JLabel("COURSE");
		intro.setBounds(74, 187, 122, 27);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
		left.add(intro);

		JLabel intro2 = new JLabel("EVALUATION");
		intro2.setBounds(38, 224, 194, 29);
		intro2.setHorizontalAlignment(SwingConstants.CENTER);
		intro2.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
		left.add(intro2);

		JLabel intro3 = new JLabel("SYSTEM");
		intro3.setBounds(74, 263, 123, 27);
		intro3.setHorizontalAlignment(SwingConstants.CENTER);
		intro3.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
		left.add(intro3);

		JLabel lblFaithC = new JLabel("FAITH COLLEGES S.Y. 2022 - 2023");
		lblFaithC.setForeground(new Color(128, 128, 128));
		lblFaithC.setBackground(new Color(192, 192, 192));
		lblFaithC.setHorizontalAlignment(SwingConstants.CENTER);
		lblFaithC.setFont(new Font("Roboto Medium", Font.PLAIN, 14));
		lblFaithC.setBounds(18, 351, 234, 38);
		left.add(lblFaithC);

		final JLabel ICON = new JLabel("");
		ICON.setFont(new Font("Tahoma", Font.PLAIN, 5));
		ICON.setHorizontalTextPosition(SwingConstants.CENTER);
		ICON.setHorizontalAlignment(SwingConstants.CENTER);
		ICON.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\CHRONOS\\eclipse-workspace\\CourseEvalSystem\\src\\guisys\\resize_faith.png").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		ICON.setBounds(34, 27, 201, 150);

		ICON.addMouseListener(new MouseAdapter() {
			private int clickCount = 0;
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        clickCount++;

		        if (clickCount == 5) {
		            AdminLogin frame = null;
					frame = new AdminLogin();
		            frame.setVisible(true);
		            
		            Window window = SwingUtilities.getWindowAncestor(ICON);
		            if (window != null) {
		                window.dispose();
		            }
		        }
		    }
		});

		left.add(ICON);

		final Panel reg_panel = new Panel();
		reg_panel.setBounds(276, 0, 337, 399);
		contentPane.add(reg_panel);
		reg_panel.setLayout(null);

		final Choice choice = new Choice();
		choice.setBounds(40, 247, 259, 30);
		reg_panel.add(choice);
		
		DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, "public", null, new String[] { "TABLE" });
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            if (!tableName.equalsIgnoreCase("students")) {
                choice.add(tableName);
            }
        }
        tables.close();

		final TextField input_name = new TextField();
		input_name.setBounds(40, 171, 259, 21);
		input_name.setForeground(new Color(192, 192, 192));
		input_name.setFont(new Font("MS Gothic", Font.PLAIN, 12));
		input_name.setText(" S1234567890");
		reg_panel.add(input_name);

		input_name.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (input_name.getText().equals(" S1234567890")) {
		            input_name.setText("");
		            input_name.setForeground(new Color(51,105,105));
		        }
		    }

		    @Override
		    public void focusLost(FocusEvent e) {
		        if (input_name.getText().isEmpty()) {
		            input_name.setText(" S1234567890");
		            input_name.setForeground(new Color(192, 192, 192));
		        }
		    }
		});

		JLabel welcome = new JLabel("WELCOME!");
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
		welcome.setBounds(80, 76, 176, 27);
		reg_panel.add(welcome);

		JLabel sn_input = new JLabel("Student Number:");
		sn_input.setHorizontalAlignment(SwingConstants.CENTER);
		sn_input.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
		sn_input.setBounds(40, 139, 121, 27);
		reg_panel.add(sn_input);

		JLabel course_input = new JLabel("Course:");
		course_input.setHorizontalAlignment(SwingConstants.CENTER);
		course_input.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
		course_input.setBounds(40, 215, 54, 27);
		reg_panel.add(course_input);

		JButton enter_button = new JButton("VIEW SUBJECTS");
		enter_button.setFont(new Font("Roboto Medium", Font.PLAIN, 10));
		enter_button.setForeground(new Color(0, 0, 0));
		enter_button.setBackground(new Color(152, 173, 207));
		enter_button.setBounds(108, 301, 121, 21);
		enter_button.setFocusPainted(false);
		reg_panel.add(enter_button);

		enter_button.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        final String studentNumber = input_name.getText();
		        String course = choice.getSelectedItem();
		        Student student = Database.getStudent(conn, studentNumber);
		        
		        // Set the current student ID
		        currentStudentId = student.getId();

		        try {
		            if (checkStudentNumber(conn, studentNumber, course)) {
		                MainSys systemFrame = new MainSys(conn, student);
		                systemFrame.setVisible(true);
		                dispose();
		            } else {
		                JOptionPane.showMessageDialog(null, "Invalid student number or course. Please try again.",
		                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (IOException | HeadlessException | SQLException ex) {
		            ex.printStackTrace();
		        }
		    }
		});


		regclick = new JLabel();
		regclick.setFont(new Font("Tahoma", Font.ITALIC, 10));
		regclick.setSize(65, 27);
		regclick.setLocation(251, 362);
		regclick.setForeground(new Color(0, 0, 0));
		regclick.setText("<html><u>Register here.</u></html>");
		regclick.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StudentReg openreg = null;
				try {
					openreg = new StudentReg();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				openreg.setVisible(true);
				dispose();
			}

			public void mouseEntered(MouseEvent e) {
				regclick.setForeground(new Color(152, 173, 207));
			}

			public void mouseExited(MouseEvent e) {
				regclick.setForeground(Color.BLACK);
			}
		});
        reg_panel.add(regclick);

        JLabel login_text = new JLabel();
        login_text.setText("No account yet?");
        login_text.setForeground(Color.BLACK);
        login_text.setFont(new Font("Tahoma", Font.ITALIC, 10));
        login_text.setBounds(174, 362, 77, 27);
        reg_panel.add(login_text);
    }
    
    private boolean checkStudentNumber(Connection connector, String studentNumber, String course_input) throws SQLException {
    	String course = Database.getCourse(connector, studentNumber);
    	
    	return course_input.equals(course);
    }

	public String getCurrentStudentId() {
		// TODO Auto-generated method stub
		return null;
	}
}

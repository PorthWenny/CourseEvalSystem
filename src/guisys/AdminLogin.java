package guisys;

import guisys.Database;
import guisys.login;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AdminLogin extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JComboBox<String> choice;
    private Connection conn;

    private int logoClickCount = 0;
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Create the frame.
     */
    public AdminLogin() {
    	
    	try {
            conn = Database.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
            e.printStackTrace();
        }
    	
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\CHRONOS\\Dropbox\\My PC (DESKTOP-MB5HNEI)\\Documents\\course.toUpperCase() Evaluation System\\CourseEvalSystem\\faith_logo.jpg"));
        setTitle("FAITH Course Evaluation System [Administrator Login]");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 627, 432);
        contentPane = new JPanel();
        contentPane.setForeground(new Color(64, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initializeGUI(); // Initialize the GUI components
    }
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminLogin frame = new AdminLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeGUI() {
    	JPanel panel_2 = new JPanel();
        panel_2.setBounds(10, 106, 593, 279);
        contentPane.add(panel_2);
        panel_2.setLayout(null);
        
        panel_2.setEnabled(false);
        panel_2.setVisible(false);
        
        choice = new JComboBox<>();
        choice.setBounds(71, 6, 91, 20);
        panel_2.add(choice);

        JLabel course_input = new JLabel("Course:");
        course_input.setBounds(10, 6, 60, 18);
        course_input.setHorizontalAlignment(SwingConstants.LEFT);
        course_input.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
        panel_2.add(course_input);

        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.setBounds(42, 199, 120, 30);
        panel_2.add(addCourseButton);

        addCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choice.getSelectedItem();
                if (selectedCourse != null) {
                    addCourseTable();
                }
            }
        });

        JButton addSubjectButton = new JButton("Add Subject");
        addSubjectButton.setBounds(42, 61, 120, 30);
        panel_2.add(addSubjectButton);

        addSubjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choice.getSelectedItem();
                if (selectedCourse != null) {
                    showAddSubjectDialog(selectedCourse.toUpperCase());
                }
            }
        });

        JButton editButton = new JButton("Edit");
        editButton.setBounds(42, 101, 120, 30);
        panel_2.add(editButton);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedCourse = (String) choice.getSelectedItem();
                    if (selectedCourse != null) {
                        String subject = table.getValueAt(selectedRow, 0).toString();
                        String prof = table.getValueAt(selectedRow, 1).toString();
                        showEditSubjectDialog(selectedCourse.toUpperCase(), selectedRow, subject, prof);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to edit.");
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(42, 140, 120, 30);
        panel_2.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedCourse = (String) choice.getSelectedItem();
                    if (selectedCourse != null) {
                        int confirmOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected row?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirmOption == JOptionPane.YES_OPTION) {
                            String subject = table.getValueAt(selectedRow, 0).toString();
                            String prof = table.getValueAt(selectedRow, 1).toString();
                            deleteSubjectAndProfessor(selectedCourse.toUpperCase(), subject, prof);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }
        });

        final JButton deleteCourseButton = new JButton("Delete Course");
        deleteCourseButton.setBounds(42, 239, 120, 30);
        panel_2.add(deleteCourseButton);

        deleteCourseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourse = (String) choice.getSelectedItem();
                if (selectedCourse != null) {
                    int confirmOption = JOptionPane.showConfirmDialog(null, "Deleting the course will remove all data associated with it. Are you sure you want to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirmOption == JOptionPane.YES_OPTION) {
                        deleteCourse(selectedCourse.toUpperCase());
                    }
                }
            }
        });

        table = new JTable();

        table.setBorder(new LineBorder(new Color(68, 68, 255), 2));
        table.setToolTipText("You can add/delete subjects.");
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Subject", "Professor" }));

        table.setDefaultEditor(Object.class, null);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setToolTipText("You can edit the table here.");
        scrollPane.setBounds(206, 6, 377, 263);
        panel_2.add(scrollPane);
        contentPane.add(panel_2);
        
            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBackground(new Color(68, 68, 255));
            panel.setBounds(0, 0, 613, 96);
            contentPane.add(panel);

            JPanel pic = new JPanel();
            pic.setBounds(520, 11, 83, 74);
            pic.setBackground(new Color(68, 68, 255));
            pic.setLayout(new BorderLayout());
            try {
                BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\CHRONOS\\eclipse-workspace\\CourseEvalSystem\\src\\guisys\\resize_faith.png"));
                int desiredWidth = 80;
                int desiredHeight = (int) ((double) desiredWidth / myPicture.getWidth() * myPicture.getHeight());
                Image scaledImage = myPicture.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
                JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
                pic.add(picLabel, BorderLayout.NORTH);
                
             // Add the MouseListener to the pic JPanel
                pic.addMouseListener(new MouseAdapter() {
                    private int clickCount = 0;
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        clickCount++;

                        if (clickCount == 5) {
                            login frame = null;
                            try {
                                frame = new login();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            frame.setVisible(true);

                            Window window = SwingUtilities.getWindowAncestor(pic);
                            if (window != null) {
                                window.dispose();
                            }
                        }
                    }
                });
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            panel.add(pic);

            JPanel panel_1 = new JPanel();
            panel_1.setBackground(new Color(68, 68, 255));
            panel_1.setBounds(10, 5, 340, 85);
            panel.add(panel_1);
            panel_1.setLayout(null);

            JLabel lblPassword = new JLabel("Password:");
            lblPassword.setForeground(new Color(255, 255, 255));
            lblPassword.setBounds(10, 47, 97, 27);
            panel_1.add(lblPassword);
            lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
            lblPassword.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            
            JLabel lblUsername = new JLabel("Username:");
            lblUsername.setForeground(new Color(255, 255, 255));
            lblUsername.setBounds(10, 10, 97, 27);
            panel_1.add(lblUsername);
            lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
            lblUsername.setFont(new Font("Roboto Medium", Font.PLAIN, 16));

            JButton loginButton = new JButton("Login");
            loginButton.setBounds(350, 34, 97, 27);
            panel.add(loginButton);
            loginButton.setFont(new Font("Roboto Medium", Font.PLAIN, 14));
            loginButton.setForeground(new Color(0, 0, 0));
            loginButton.setBackground(new Color(152, 173, 207));
            loginButton.setFocusPainted(false);

            usernameField = new JTextField();
            usernameField.setBounds(119, 10, 194, 27);
            panel_1.add(usernameField);
            usernameField.setColumns(10);
            usernameField.setVisible(true);

            passwordField = new JPasswordField();
            passwordField.setBounds(119, 47, 194, 27);
            panel_1.add(passwordField);
            passwordField.setVisible(true);

            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = new String(passwordField.getPassword());

                    if ("admin".equals(username) && "password".equals(password)) {
                        usernameField.setEnabled(false);
                        passwordField.setEnabled(false);
                        loginButton.setEnabled(false);
                        
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        
                        panel_2.setEnabled(true);
                        panel_2.setVisible(true);
                        loadCourses();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                    }
                }
            });
     }

    private void loadCourses() {
        choice.removeAllItems();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name <> 'students' COLLATE \"C\"");

            while (rs.next()) {
                String tableName = rs.getString("table_name");
                tableName = tableName.toUpperCase();
                choice.addItem(tableName);
            }
            rs.close();
            st.close();

            choice.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String selectedCourse = (String) choice.getSelectedItem();
                    if (selectedCourse != null) {
                        refreshTable(selectedCourse.toUpperCase());
                    }
                }
            });

            String selectedCourse = (String) choice.getSelectedItem();
            if (selectedCourse != null) {
                refreshTable(selectedCourse.toUpperCase());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch courses from the database.");
            e.printStackTrace();
        }
    }private void loadCourses() {
        choice.removeAllItems();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_name <> 'students' COLLATE \"C\"");

            while (rs.next()) {
                String tableName = rs.getString("table_name");
                tableName = tableName.toUpperCase();
                choice.addItem(tableName);
            }
            rs.close();
            st.close();

            choice.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String selectedCourse = (String) choice.getSelectedItem();
                    if (selectedCourse != null) {
                        refreshTable(selectedCourse.toUpperCase());
                    }
                }
            });

            String selectedCourse = (String) choice.getSelectedItem();
            if (selectedCourse != null) {
                refreshTable(selectedCourse.toUpperCase());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch courses from the database.");
            e.printStackTrace();
        }
    }


        private void refreshTable(String course) {
            if (course == null) {
            	DefaultTableModel model = (DefaultTableModel) table.getModel();
            	model.setRowCount(0);
            	} else {
            	DefaultTableModel model = (DefaultTableModel) table.getModel();
            	model.setRowCount(0);
            	try {
            	Statement st = conn.createStatement();
            	ResultSet rs = st.executeQuery("SELECT subject, prof FROM \"" + course.toUpperCase() + "\"");
                while (rs.next()) {
                    String subject = rs.getString("subject");
                    String professor = rs.getString("prof");
                    model.addRow(new Object[] { subject, professor });
                }
                rs.close();
                st.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to fetch data from the database.");
                e.printStackTrace();
            }
        }
    }

        private void addCourseTable() {
            String course = JOptionPane.showInputDialog(null, "Enter the course name:", "Add Course", JOptionPane.PLAIN_MESSAGE);

            if (course == null) {
                JOptionPane.showMessageDialog(null, "Course name cannot be empty.");
            } else if (!course.startsWith("B")) {
                JOptionPane.showMessageDialog(null, "Course name must start with 'B'.");
            } else {
                String tableName = course.toUpperCase();

                try {
                    Connection connection = Database.getConnection();
                    Statement statement = connection.createStatement();

                    // Check if the table already exists
                    String checkTableQuery = String.format(
                        "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = '%s')",
                        tableName
                    );
                    ResultSet resultSet = statement.executeQuery(checkTableQuery);
                    resultSet.next();
                    boolean tableExists = resultSet.getBoolean(1);
                    resultSet.close();

                    if (tableExists) {
                        JOptionPane.showMessageDialog(null, "Table already exists.");
                    } else {
                        // Create the table in the database
                        String createTableQuery = String.format(
                            "CREATE TABLE \"%s\" (" +
                            "id SERIAL PRIMARY KEY," +
                            "subject TEXT," +
                            "prof TEXT," +
                            "answer INT[] DEFAULT '{}'" +
                            ")",
                            tableName
                        );
                        statement.executeUpdate(createTableQuery);
                        JOptionPane.showMessageDialog(null, "Table added successfully.");
                        
                        loadCourses();
                    }

                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Failed to add table.");
                    e.printStackTrace();
                }
            }
        }



    private void showAddSubjectDialog(String course) {
        JTextField subjectField = new JTextField();
        JTextField professorField = new JTextField();

        Object[] message = { "Subject:", subjectField, "Professor:", professorField };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subject = subjectField.getText().trim();
            String professor = professorField.getText().trim();

            if (subject.isEmpty() || professor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Subject and professor cannot be empty.");
            } else {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[] { subject, professor });

                try {
                	String insertQuery = "INSERT INTO \"" + course.toUpperCase() + "\" (subject, prof) VALUES (?, ?)";
                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setString(1, subject);
                    ps.setString(2, professor);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Failed to add subject to the database.");
                    e.printStackTrace();
                }
            }
        }
    }

    private void showEditSubjectDialog(String course, int row, String subject, String professor) {
        JTextField subjectField = new JTextField(subject);
        JTextField professorField = new JTextField(professor);

        Object[] message = { "Subject:", subjectField, "Professor:", professorField };

        int option = JOptionPane.showConfirmDialog(null, message, "Edit Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String editedSubject = subjectField.getText().trim();
            String editedProfessor = professorField.getText().trim();

            if (editedSubject.isEmpty() || editedProfessor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Subject and professor cannot be empty.");
           } else {
           DefaultTableModel model = (DefaultTableModel) table.getModel();
           model.setValueAt(editedSubject, row, 0);
           model.setValueAt(editedProfessor, row, 1);
           try {
        	   String updateQuery = "UPDATE \"" + course.toUpperCase() + "\" SET subject = ?, prof = ? WHERE subject = ? AND professor = ?";
               PreparedStatement ps = conn.prepareStatement(updateQuery);
               ps.setString(1, editedSubject);
               ps.setString(2, editedProfessor);
               ps.setString(3, subject);
               ps.setString(4, professor);
               ps.executeUpdate();
               ps.close();
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Failed to update subject in the database.");
               e.printStackTrace();
           }
       }
   }
}

private void deleteSubjectAndProfessor(String course, String subject, String professor) {
   DefaultTableModel model = (DefaultTableModel) table.getModel();
   int row = table.getSelectedRow();
   model.removeRow(row);

   try {
	   String deleteQuery = "DELETE FROM \"" + course.toUpperCase() + "\" WHERE subject = ? AND prof = ?";
       PreparedStatement ps = conn.prepareStatement(deleteQuery);
       ps.setString(1, subject);
       ps.setString(2, professor);
       ps.executeUpdate();
       ps.close();
   } catch (SQLException e) {
       JOptionPane.showMessageDialog(null, "Failed to delete subject from the database.");
       e.printStackTrace();
   }
}

private void deleteCourse(String course) {
   try {
       Statement st = conn.createStatement();
       st.executeUpdate("DROP TABLE \"" + course.toUpperCase() + "\"");
       st.close();
       choice.removeItem(course);

       JOptionPane.showMessageDialog(null, "Course deleted successfully.");
   } catch (SQLException e) {
       JOptionPane.showMessageDialog(null, "Failed to delete course from the database.");
       e.printStackTrace();
   }
}
}

            
               




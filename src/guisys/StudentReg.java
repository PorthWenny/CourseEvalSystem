package guisys;

import guisys.AdminLogin;
import guisys.MainSys;
import guisys.Database;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class StudentReg extends JFrame {
    private JLabel registrationClick;
    private JPanel contentPane;
    private final JPanel leftPanel = new JPanel();
    private JFrame frame;
    private boolean isLoginClicked;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StudentReg frame = new StudentReg();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public StudentReg() throws SQLException {
        final Connection connection = Database.getConnection();
        frame = this;

        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\CHRONOS\\Dropbox\\My PC (DESKTOP-MB5HNEI)\\Documents\\Course Evaluation System\\CourseEvalSystem\\faith_logo.jpg"));
        setTitle("FAITH Course Evaluation System [Student Register]");

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 627, 432);
        contentPane = new JPanel();
        contentPane.setForeground(new Color(64, 128, 128));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        leftPanel.setBackground(new Color(152, 173, 207));
        leftPanel.setBounds(0, 0, 270, 399);
        contentPane.add(leftPanel);
        leftPanel.setLayout(null);

        JLabel courseLabel1 = new JLabel("COURSE");
        courseLabel1.setBounds(74, 187, 122, 27);
        courseLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        courseLabel1.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
        leftPanel.add(courseLabel1);

        JLabel courseLabel2 = new JLabel("EVALUATION");
        courseLabel2.setBounds(38, 224, 194, 29);
        courseLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        courseLabel2.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
        leftPanel.add(courseLabel2);

        JLabel courseLabel3 = new JLabel("SYSTEM");
        courseLabel3.setBounds(74, 263, 123, 27);
        courseLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        courseLabel3.setFont(new Font("Roboto Medium", Font.PLAIN, 32));
        leftPanel.add(courseLabel3);

        JLabel faithCollegesLabel = new JLabel("FAITH COLLEGES S.Y. 2022 - 2023");
        faithCollegesLabel.setForeground(new Color(128, 128, 128));
        faithCollegesLabel.setBackground(new Color(192, 192, 192));
        faithCollegesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        faithCollegesLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 14));
        faithCollegesLabel.setBounds(18, 351, 234, 38);
        leftPanel.add(faithCollegesLabel);

        final JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Tahoma", Font.PLAIN, 5));
        iconLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setIcon(new ImageIcon(new ImageIcon("C:\\Users\\CHRONOS\\eclipse-workspace\\CourseEvalSystem\\src\\guisys\\resize_faith.png").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        iconLabel.setBounds(34, 27, 201, 150);

        iconLabel.addMouseListener(new MouseAdapter() {
            private int clickCount = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                clickCount++;

                if (clickCount == 5) {
                    AdminLogin frame = null;
					frame = new AdminLogin();
                    frame.setVisible(true);

                    Window window = SwingUtilities.getWindowAncestor(iconLabel);
                    if (window != null) {
                        window.dispose();
                    }
                }
            }
        });

        leftPanel.add(iconLabel);

        final Panel registrationPanel = new Panel();
        registrationPanel.setBounds(276, 0, 337, 399);
        contentPane.add(registrationPanel);
        registrationPanel.setLayout(null);

        JLabel inputLabel2 = new JLabel("Please input the following to be fully registered:");
        inputLabel2.setHorizontalAlignment(SwingConstants.LEFT);
        inputLabel2.setFont(new Font("Roboto Medium", Font.ITALIC, 12));
        inputLabel2.setBounds(15, 72, 385, 55);
        registrationPanel.add(inputLabel2);

        final Choice courseChoice = new Choice();
        courseChoice.setBounds(15, 264, 149, 18);
        registrationPanel.add(courseChoice);
        
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, "public", null, new String[] { "TABLE" });
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            if (tableName.contains("B")) {
            	courseChoice.add(tableName);
            }
        }
        tables.close();

        final TextField studentNumberField = new TextField();
        studentNumberField.setForeground(new Color(192, 192, 192));
        studentNumberField.setFont(new Font("MS Gothic", Font.PLAIN, 12));
        studentNumberField.setText("S1234567890");
        studentNumberField.setBounds(15, 144, 298, 21);

        registrationPanel.add(studentNumberField);

        final TextField firstNameField = new TextField();
        firstNameField.setText(" John");
        firstNameField.setForeground(new Color(192, 192, 192));
        firstNameField.setFont(new Font("MS Gothic", Font.PLAIN, 12));
        firstNameField.setBounds(15, 202, 149, 21);
        registrationPanel.add(firstNameField);

        final TextField surnameField = new TextField();
        surnameField.setForeground(new Color(192, 192, 192));
        surnameField.setFont(new Font("MS Mincho", Font.PLAIN, 12));
        surnameField.setText(" Doe");
        surnameField.setBounds(170, 202, 143, 21);
        registrationPanel.add(surnameField);
        
        studentNumberField.addFocusListener(new FocusListener() {
            private boolean isFirstFocus = true;

            @Override
            public void focusGained(FocusEvent e) {
                if (isFirstFocus) {
                    studentNumberField.setText("");
                    studentNumberField.setForeground(new Color(51,105,105));
                    isFirstFocus = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!isLoginClicked) { 
                    if (studentNumberField.getText().isEmpty()) {
                        studentNumberField.setText(" S1234567890");
                        studentNumberField.setForeground(new Color(192, 192, 192));
                        isFirstFocus = true; 
                    } else {
                        String studentNumber = studentNumberField.getText();
                        if (!studentNumber.matches("S\\d{10}")) {
                            JOptionPane.showMessageDialog(StudentReg.this.frame, "Invalid student number. It must start with 'S' and be followed by exactly 10 digits.");
                            studentNumberField.requestFocus();
                        }
                    }
                }
            }
        });

        firstNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (firstNameField.getText().equals(" John")) {
                    firstNameField.setText("");
                    firstNameField.setForeground(new Color(51,105,105));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (firstNameField.getText().isEmpty()) {
                    firstNameField.setText(" John");
                    firstNameField.setForeground(new Color(192, 192, 192));
                }
            }
        });

        surnameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (surnameField.getText().equals(" Doe")) {
                    surnameField.setText("");
                    surnameField.setForeground(new Color(51,105,105));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (surnameField.getText().isEmpty()) {
                    surnameField.setText(" Doe");
                    surnameField.setForeground(new Color(192, 192, 192));
                }
            }
        });

            JLabel welcomeLabel = new JLabel("Ad Astra, New Student!");
            welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            welcomeLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 26));
            welcomeLabel.setBounds(5, 10, 327, 94);
            registrationPanel.add(welcomeLabel);

            JLabel studentNumberLabel = new JLabel("Student Number:");
            studentNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
            studentNumberLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            studentNumberLabel.setBounds(15, 114, 121, 27);
            registrationPanel.add(studentNumberLabel);

            JLabel courseLabel = new JLabel("Course:");
            courseLabel.setHorizontalAlignment(SwingConstants.LEFT);
            courseLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            courseLabel.setBounds(15, 238, 54, 27);
            registrationPanel.add(courseLabel);

            JButton registerButton = new JButton("REGISTER");
            registerButton.setFont(new Font("Roboto Medium", Font.PLAIN, 10));
            registerButton.setForeground(new Color(0, 0, 0));
            registerButton.setBackground(new Color(152, 173, 207));
            registerButton.setBounds(123, 316, 90, 21);
            registerButton.setFocusPainted(false);
            registrationPanel.add(registerButton);

            registrationClick = new JLabel();
            registrationClick.setFont(new Font("Tahoma", Font.ITALIC, 10));
            registrationClick.setSize(65, 27);
            registrationClick.setLocation(267, 362);
            registrationClick.setForeground(new Color(0, 0, 0));
            registrationClick.setText("<html><u>Login here.</u></html>");

            registrationClick.addMouseListener(new MouseAdapter() {
            	@Override
                public void mouseClicked(MouseEvent e) {
                    isLoginClicked = true;
                    login backToLogin = null;
                    try {
                        backToLogin = new login();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    backToLogin.setVisible(true);
                    dispose();
                }

                public void mouseEntered(MouseEvent e) {
                    registrationClick.setForeground(new Color(152, 173, 207));
                }

                public void mouseExited(MouseEvent e) {
                    registrationClick.setForeground(Color.BLACK);
                }
            });
            registrationPanel.add(registrationClick);

            JLabel alreadyAccountLabel = new JLabel("Already got an account?");
            alreadyAccountLabel.setText("Already got an account?");
            alreadyAccountLabel.setForeground(Color.BLACK);
            alreadyAccountLabel.setFont(new Font("Tahoma", Font.ITALIC, 10));
            alreadyAccountLabel.setBounds(153, 362, 124, 27);
            registrationPanel.add(alreadyAccountLabel);

            JLabel firstNameLabel = new JLabel("First Name:");
            firstNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            firstNameLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            firstNameLabel.setBounds(15, 171, 121, 27);
            registrationPanel.add(firstNameLabel);

            JLabel surnameLabel = new JLabel("Surname:");
            surnameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            surnameLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            surnameLabel.setBounds(170, 173, 121, 27);
            registrationPanel.add(surnameLabel);

            final Choice yearLevelChoice = new Choice();
            yearLevelChoice.setBounds(170, 264, 143, 18);
            registrationPanel.add(yearLevelChoice);

            yearLevelChoice.add("1A");
            yearLevelChoice.add("2A");
            yearLevelChoice.add("3A");
            yearLevelChoice.add("4A");

            JLabel yearLevelLabel = new JLabel("Year Level:");
            yearLevelLabel.setHorizontalAlignment(SwingConstants.LEFT);
            yearLevelLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
            yearLevelLabel.setBounds(170, 239, 100, 27);
            registrationPanel.add(yearLevelLabel);

            registerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String studentNumber = studentNumberField.getText().trim();
                    String firstName = firstNameField.getText().trim();
                    String surname = surnameField.getText().trim();
                    String course = courseChoice.getSelectedItem().trim();
                    String yearLevel = yearLevelChoice.getSelectedItem().trim();

                    if (studentNumber.isEmpty() || firstName.isEmpty() || surname.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String table = "students";

                    try {
                        Connection conn = Database.getConnection();

                        if (checkStudentNumber(conn, studentNumber, course)) {
                            JOptionPane.showMessageDialog(null, "Student number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                            conn.close();
                            return;
                        }

                        String query = "INSERT INTO " + table + " (id, name, surname, \"courseId\", level) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement statement = conn.prepareStatement(query);
                        statement.setString(1, studentNumber);
                        statement.setString(2, firstName);
                        statement.setString(3, surname);
                        statement.setInt(4, 1);
                        statement.setString(5, yearLevel);
                        int rowsInserted = statement.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(null, "Registration successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            login backToLogin = null;
                            try {
                                backToLogin = new login();
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            backToLogin.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to register.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        statement.close();
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        private boolean checkStudentNumber(Connection connection, String studentNumber, String course) throws SQLException {
            String studentCourse = Database.getCourse(connection, studentNumber);

            return course.equals(studentCourse);
        }
    }
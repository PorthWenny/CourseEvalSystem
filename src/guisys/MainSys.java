package guisys;

import guisys.Student;
import guisys.login;
import guisys.EvalForm;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MainSys extends JFrame {
    private JTable table;
    private JButton answerButton;
    private JButton resetButton;
    
    public MainSys(Connection conn, Student student) throws IOException, SQLException {
    	// this.conn = conn;
        getContentPane().setForeground(new Color(255, 255, 255));
        setResizable(false);
        setBackground(new Color(192, 192, 192));
        setAlwaysOnTop(true);
        setFont(new Font("Roboto Medium", Font.PLAIN, 14));
        setForeground(new Color(0, 0, 255));
        setIconImage(
                Toolkit.getDefaultToolkit().getImage("C:\\Users\\CHRONOS\\Dropbox\\My PC (DESKTOP-MB5HNEI)\\Documents\\Course Evaluation System\\CourseEvalSystem\\faith_logo.jpg"));
        setTitle("Course Evaluation System [Student]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 726, 450);
        getContentPane().setLayout(null);
        
        JPanel pic = new JPanel();
        pic.setBounds(619, 10, 83, 74);
        pic.setBackground(new Color(68, 68, 255));
        pic.setLayout(new BorderLayout());
        try {
            BufferedImage myPicture = ImageIO
                    .read(new File("C:\\Users\\CHRONOS\\eclipse-workspace\\CourseEvalSystem\\src\\guisys\\resize_faith.png"));
            int desiredWidth = 80; // Adjust the desired width
            int desiredHeight = (int) ((double) desiredWidth / myPicture.getWidth() * myPicture.getHeight());

            Image scaledImage = myPicture.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            pic.add(picLabel, BorderLayout.NORTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
						// TODO Auto-generated catch block
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
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 712, 96);
        panel.setBackground(new Color(68, 68, 255));
        getContentPane().add(panel);
        panel.setLayout(null);
        panel.add(pic);
        
        JLabel welcome = new JLabel("WELCOME, " + student.getName() + "!");
        welcome.setHorizontalAlignment(SwingConstants.LEFT);
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setFont(new Font("The Bold Font", Font.BOLD, 40));
        welcome.setBounds(20, 10, 559, 64);
        panel.add(welcome);
        
        JLabel course = new JLabel("Student's Course and Level: BSCS -" + student.getLevel());
        course.setForeground(new Color(255, 255, 255));
        course.setHorizontalAlignment(SwingConstants.LEFT);
        course.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
        course.setBounds(20, 59, 435, 27);
        panel.add(course);
        
        JPanel subj = new JPanel();
        subj.setBounds(10, 106, 692, 297);
        getContentPane().add(subj);
        subj.setLayout(new BorderLayout());
        subj.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {}, new Object[] { "Subject", "Professor", "Answered?" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        List<String[]> data = fetchDataFromSupabase(conn, student); 
        for (String[] row : data) {
            tableModel.addRow(row);
        }

        // Create the table using the table model
        table = new JTable(tableModel) {
            @Override
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    @Override
                    public boolean getReorderingAllowed() {
                        return false;
                    }
                };
            }
        };

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        
     // Custom cell renderer for the "Answered?" column
        TableCellRenderer answeredRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null && column == 2) {
                    String answered = value.toString();
                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    
                    // Set the appropriate icon based on the value
                    if (answered.equals("1")) {
                        label.setIcon(createCheckIcon()); // Display checkmark
                    } else if (answered.equals("0")) {
                        label.setIcon(createXIcon()); // Display X symbol
                    }
                    
                    // Adjust the size of the column to show the icons fully
                    table.getColumnModel().getColumn(column).setMinWidth(80);
                    table.getColumnModel().getColumn(column).setMaxWidth(80);
                    table.getColumnModel().getColumn(column).setWidth(80);

                    return label;
                }
                return cell;
            }
        };

        // Set the custom renderer for the "Answered?" column
        table.getColumnModel().getColumn(2).setCellRenderer(answeredRenderer);

        // Set column alignment to center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        subj.add(scrollPane, BorderLayout.CENTER);

        answerButton = new JButton("Answer");
        answerButton.setEnabled(false);
        answerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String subject = table.getValueAt(selectedRow, 0).toString();
                    String professor = table.getValueAt(selectedRow, 1).toString();
                    JOptionPane.showMessageDialog(MainSys.this,
                            "Answering the evaluation for:\n\nSubject: " + subject + "\nProfessor: " + professor);
                    
                    EvalForm answerFrame = null;
					try {
						answerFrame = new EvalForm(conn, student, subject);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						System.out.print("Connection closed.");
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	                answerFrame.setVisible(true);
	                dispose();
                }
            }
        });

        resetButton = new JButton("Reset Progress");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String subject = table.getValueAt(selectedRow, 0).toString();
                    String professor = table.getValueAt(selectedRow, 1).toString();
                    int option = JOptionPane.showConfirmDialog(MainSys.this,
                            "Are you sure you want to reset the progress for:\n\nSubject: " + subject + "\nProfessor: "
                                    + professor,
                            "Reset Progress", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Perform reset action here
                        JOptionPane.showMessageDialog(MainSys.this,
                                "Progress reset for:\n\nSubject: " + subject + "\nProfessor: " + professor);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(answerButton);
        buttonPanel.add(resetButton);

        subj.add(buttonPanel, BorderLayout.SOUTH);
        
        JButton viewButton = new JButton("View Answers");
        viewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        buttonPanel.add(viewButton);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (table.getSelectedRow() != -1) {
                    answerButton.setEnabled(true);
                    resetButton.setEnabled(true);
                } else {
                    answerButton.setEnabled(false);
                    resetButton.setEnabled(false);
                }
            }
        });
    }
    
 // Utility method to create a checkmark icon
    private Icon createCheckIcon() {
        // Replace with your own checkmark icon creation logic
        // For example, you can use an image file or create a custom icon programmatically
        // Here's an example using a built-in icon from the Java Swing library
        return UIManager.getIcon("OptionPane.informationIcon");
    }

    // Utility method to create an X symbol icon
    private Icon createXIcon() {
        // Replace with your own X symbol icon creation logic
        // For example, you can use an image file or create a custom icon programmatically
        // Here's an example using a built-in icon from the Java Swing library
        return UIManager.getIcon("OptionPane.errorIcon");
    }
    
    private List<String[]> fetchDataFromSupabase(Connection conn, Student student) throws SQLException {
        List<String[]> data = new ArrayList<>();

        // Perform the database query and retrieve the data
        try (Statement statement = conn.createStatement()) {

            String tableName = Database.getCourse(conn, student.getId());
            String userId = student.getId(); // Assuming the ID is stored in the Student object

            // Check if the user's ID column already exists in the table
            ResultSet columnsResult = conn.getMetaData().getColumns(null, null, tableName, userId);
            boolean idColumnExists = columnsResult.next();

            // If the ID column doesn't exist, add it to the table
            if (!idColumnExists) {
                statement.execute("ALTER TABLE \"" + tableName + "\" ADD COLUMN \"" + userId + "\" INTEGER DEFAULT 0");
            }
            
            ArrayList<Subject> subjects = (ArrayList<Subject>) Database.getSubjects(conn, student);
            for (Subject curr : subjects) {
            	String[] row = {curr.getName(), curr.getProf(), ""};
            	data.add(row);
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            throw e;
        }

        return data;
    }
}

package guisys;

import guisys.Student;
import guisys.Database;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class EvalForm extends JFrame {

	private JPanel contentPane;
	
	private String currentStudentId;
	
	private List<String> questionColumns; // List to store the question column names
	private int currentQuestionIndex; // Index to track the current question being displayed
	private JLabel questionLabel; // JLabel to display the current question
	private JRadioButton[] ratingButtons; // Array of JRadioButtons for rating questions
	private JTextField textField; // JTextField for text input questions

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                Connection conn = Database.getConnection();
	                String currentStudentId = "your_student_id"; 

	                EvalForm frame = new EvalForm(conn, currentStudentId);
	                frame.setVisible(true);

	                conn.close(); // Close the database connection when done
	            } catch (SQLException | IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public EvalForm(Connection conn, String currentStudentId) throws IOException, SQLException {
	    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 726, 450); setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
        
        JPanel header = new JPanel();
        header.setBounds(0, 0, 712, 96);
        header.setBackground(new Color(68, 68, 255));
        getContentPane().add(header);
        header.setLayout(null);
        header.add(pic);
        
        Student student = Database.getStudent(conn, currentStudentId);
        
        JLabel welcome = new JLabel("WELCOME, " + student.getName() + "!");
        welcome.setHorizontalAlignment(SwingConstants.LEFT);
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setFont(new Font("The Bold Font", Font.BOLD, 40));
        welcome.setBounds(20, 10, 559, 64);
        header.add(welcome);
        
        JLabel course = new JLabel("Student's Course and Level: " + student.getCourse() + "-" + student.getLevel());
        course.setForeground(new Color(255, 255, 255));
        course.setHorizontalAlignment(SwingConstants.LEFT);
        course.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
        course.setBounds(20, 59, 435, 27);
        header.add(course);
        
        JPanel QNA = new JPanel();
        QNA.setBounds(0, 106, 712, 297);
        contentPane.add(QNA);
        
     // Retrieve the question column names from the database
        questionColumns = Database.getQuestionColumns(conn);

        // Initialize the current question index
        currentQuestionIndex = 0;
        QNA.setLayout(null);

        // Initialize the JLabel for displaying the current question
        questionLabel = new JLabel();
        questionLabel.setBounds(105, 24, 502, 121);
        QNA.add(questionLabel);

        // Initialize the array of JRadioButtons for rating questions
        ratingButtons = new JRadioButton[5];
        for (int i = 0; i < ratingButtons.length; i++) {
            ratingButtons[i] = new JRadioButton(String.valueOf(i + 1));
            ratingButtons[i].setBounds(20 + (i * 50), 60, 50, 30);
            QNA.add(ratingButtons[i]);
        }

        // Initialize the JTextField for text input questions
        textField = new JTextField();
        textField.setBounds(278, 212, 156, 19);
        QNA.add(textField);
        
     // Initialize and add the back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(278, 241, 53, 21);
        QNA.add(backButton);

        // Initialize and add the next button
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(381, 241, 53, 21);
        QNA.add(nextButton);

        // Register event listeners for the buttons
        backButton.addActionListener(e -> backButtonClicked());
        nextButton.addActionListener(e -> nextButtonClicked());

        // Display the first question
        displayQuestion();
	}

	private void displayQuestion() throws SQLException {
	    if (currentQuestionIndex >= 0 && currentQuestionIndex < questionColumns.size()) {
	        String question = questionColumns.get(currentQuestionIndex);
	        questionLabel.setText(question);

	        boolean isRatingQuestion = Database.isRatingQuestion(question); // Method to determine if the question is a rating question

	        // Show/hide components based on the question type
	        if (isRatingQuestion) {
	            for (JRadioButton radioButton : ratingButtons) {
	                radioButton.setVisible(true);
	            }
	            textField.setVisible(false);
	        } else {
	            for (JRadioButton radioButton : ratingButtons) {
	                radioButton.setVisible(false);
	            }
	            textField.setVisible(true);
	        }
	    }
	}

	private void backButtonClicked() throws SQLException {
	    if (currentQuestionIndex > 0) {
	        currentQuestionIndex--;
	        displayQuestion();
	    }
	}

	private void nextButtonClicked() throws SQLException {
	    if (currentQuestionIndex < questionColumns.size() - 1) {
	        currentQuestionIndex++;
	        displayQuestion();
	    }
	}

}

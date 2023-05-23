package guisys;

import guisys.Student;
import guisys.Database;
import guisys.Subject;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class EvalForm extends JFrame {

	private JPanel contentPane;
	
	private List<Question> questionColumns; // List to store the question column names
	private int currentQuestionIndex; // Index to track the current question being displayed
	private JLabel questionLabel; // JLabel to display the current question
	private JRadioButton[] ratingButtons; // Array of JRadioButtons for rating questions
	private JTextField textField; // JTextField for text input questions

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                Connection conn = Database.getConnection();
	                String currentStudentId = student.getId();
	                
	                EvalForm frame = new EvalForm(conn, currentStudentId);
	                frame.setVisible(true);

	                conn.close(); // Close the database connection when done
	            } catch (SQLException | IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}*/

	/**
	 * Create the frame.
	 */
	
	public EvalForm(Connection conn, Student student, String subject) throws IOException, SQLException {
		
		ArrayList<Object> answers = new ArrayList<>();
 	    
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
        
        JLabel welcome = new JLabel("WELCOME, " + student.getName() + "!");
        welcome.setHorizontalAlignment(SwingConstants.LEFT);
        welcome.setForeground(new Color(255, 255, 255));
        welcome.setFont(new Font("The Bold Font", Font.BOLD, 40));
        welcome.setBounds(20, 10, 559, 64);
        header.add(welcome);
        
        JLabel course = new JLabel("Currently Evaluating: " + subject);
        course.setForeground(new Color(255, 255, 255));
        course.setHorizontalAlignment(SwingConstants.LEFT);
        course.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
        course.setBounds(20, 59, 435, 27);
        header.add(course);
        
        JPanel QNA = new JPanel();
        QNA.setBounds(0, 106, 712, 297);
        contentPane.add(QNA);
        
     // Retrieve the question column names from the database
        questionColumns = Database.getQuestion(conn);

        // Initialize the current question index
        currentQuestionIndex = 0;
        QNA.setLayout(null);

        // Initialize the JLabel for displaying the current question
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBounds(6, 24, 700, 121);
        QNA.add(questionLabel);

        // Initialize the array of JRadioButtons for rating questions
        ratingButtons = new JRadioButton[5];
        ButtonGroup buttonGroup = new ButtonGroup();
        int x = 102; // Initial x-coordinate for the radio buttons
        int y = 152; // y-coordinate for the radio buttons and text field
        int buttonWidth = 100; // Width of each radio button
        int spacing = 20; // Spacing between radio buttons

        for (int i = 0; i < ratingButtons.length; i++) {
            ratingButtons[i] = new JRadioButton(String.valueOf(i + 1));
            ratingButtons[i].setActionCommand(String.valueOf(i + 1));
            ratingButtons[i].setBounds(x, y, buttonWidth, 30);
            QNA.add(ratingButtons[i]);
            buttonGroup.add(ratingButtons[i]);
            x += buttonWidth + spacing;
        }

        // Initialize the JTextField for text input questions
        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setBounds(102, 152, 508, 76);
        QNA.add(textField);
        
     // Initialize and add the back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 266, 78, 21);
        QNA.add(backButton);

        // Initialize and add the next button
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(105, 266, 78, 21);
        QNA.add(nextButton);
        
        JButton submit = new JButton("Submit Answer");
        submit.setBounds(576, 266, 126, 21);
        
        submit.addActionListener(e -> {
			try {
				if (questionColumns.get(currentQuestionIndex).getAnswer().equals("")) {
		    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
		    			for (JRadioButton radioButton : ratingButtons) {
		    				if (radioButton.isSelected()) {
		    					questionColumns.get(currentQuestionIndex).setAnswer(radioButton.getActionCommand());
		    					System.out.print(radioButton.getActionCommand());
		    				}
		                
		    				radioButton.setSelected(false);
		    			}
		    		}
			    	else {
			    		questionColumns.get(currentQuestionIndex).setAnswer(textField.getText());
			    		System.out.print(questionColumns.get(currentQuestionIndex).getAnswer());
			    		
			    		textField.setText("");
			    	}
		    	}
		    	else {
		    		String answer = questionColumns.get(currentQuestionIndex).getAnswer();
		    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
		    			ratingButtons[Integer.parseInt(answer) - 1].setSelected(true);
		    		}
		    		else {
		    			textField.setText(answer);
		    		}
		    	}	
				
				JOptionPane.showMessageDialog(this, "Answers submitted successfully!");
				MainSys systemFrame = new MainSys(conn, student);
                systemFrame.setVisible(true);
				Database.submitAnswers(questionColumns, student, subject);
				dispose();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        QNA.add(submit);

        // Register event listeners for the buttons
        backButton.addActionListener(e -> {
			try {
				backButtonClicked();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        nextButton.addActionListener(e -> {
			try {
				nextButtonClicked();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        // Display the first question
        displayQuestion(conn);
	}
	
	
	private void displayQuestion(Connection conn) throws SQLException {
	    if (currentQuestionIndex >= 0 && currentQuestionIndex < questionColumns.size()) {
	        Question question = questionColumns.get(currentQuestionIndex);
	        questionLabel.setText(question.getQuestion());

	        // Show/hide components based on the question type
	        if (question.getType().equals("int8")) {
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
	    	
	    	if (questionColumns.get(currentQuestionIndex).getAnswer().equals("")) {
	    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
	    			for (JRadioButton radioButton : ratingButtons) {
	    				if (radioButton.isSelected()) {
	    					questionColumns.get(currentQuestionIndex).setAnswer(radioButton.getActionCommand());
	    					System.out.print(radioButton.getActionCommand());
	    				}
	                
	    				radioButton.setSelected(false);
	    			}
	    		}
		    	else {
		    		questionColumns.get(currentQuestionIndex).setAnswer(textField.getText());
		    		System.out.print(questionColumns.get(currentQuestionIndex).getAnswer());
		    		
		    		textField.setText("");
		    	}
	    	}
	    	else {
	    		String answer = questionColumns.get(currentQuestionIndex).getAnswer();
	    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
	    			ratingButtons[Integer.parseInt(answer) - 1].setSelected(true);
	    		}
	    		else {
	    			textField.setText(answer);
	    		}
	    	}
	    	
	        currentQuestionIndex--;
	        displayQuestion(null);
	    }
	}

	private void nextButtonClicked() throws SQLException {
	    if (currentQuestionIndex < questionColumns.size() - 1) {
	    	
	    	if (questionColumns.get(currentQuestionIndex).getAnswer().equals("")) {
	    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
	    			for (JRadioButton radioButton : ratingButtons) {
	    				if (radioButton.isSelected()) {
	    					questionColumns.get(currentQuestionIndex).setAnswer(radioButton.getActionCommand());
	    					System.out.print(radioButton.getActionCommand());
	    				}
	                
	    				radioButton.setSelected(false);
	    			}
	    		}
		    	else {
		    		questionColumns.get(currentQuestionIndex).setAnswer(textField.getText());
		    		System.out.print(questionColumns.get(currentQuestionIndex).getAnswer());
		    		
		    		textField.setText("");
		    	}
	    	}
	    	else {
	    		String answer = questionColumns.get(currentQuestionIndex).getAnswer();
	    		if (questionColumns.get(currentQuestionIndex).getType().equals("int8")) {
	    			ratingButtons[Integer.parseInt(answer) - 1].setSelected(true);
	    		}
	    		else {
	    			textField.setText(answer);
	    		}
	    	}
	    	

	    	
	        currentQuestionIndex++;
	        displayQuestion(null);
	    }
	}
}

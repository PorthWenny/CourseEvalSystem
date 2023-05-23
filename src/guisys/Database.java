package guisys;

import java.util.*;
import java.sql.*;

public class Database {
	
	private static Connection conn;
	
	public static Connection getConnection() throws SQLException {

        String url = "jdbc:postgresql://db.tyrfvtvzbzushigzjzbp.supabase.co:5432/postgres";
        String username = "postgres";
        String password = "wow321wow123wow";

        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Connection successful! ✔\n");
        return conn;
    }
	
	public static void main (String[] args) throws SQLException {
		conn = getConnection();
	}
	
	public static String getCourse(Connection conn, String id) {
	    String course = "";
	    try {
	        Statement st = conn.createStatement();
	        ResultSet rs = st.executeQuery(String.format("SELECT coursename FROM students as s JOIN course as c ON c.\"id\" = s.\"courseId\" WHERE s.id = '%s'", id.toUpperCase()));
	        
	        if (rs.next()) {
	            course = rs.getString("coursename");
	        }
	        rs.close();
	        st.close();

	        return course;
	    } catch (SQLException e) {
	        System.out.println("getCourse() - Error message: " + e.getMessage());
	    }
	    return course;
	}
	
	public static Student getStudent (Connection conn, String id) {
		Student student = new Student("", "", "", 0, "");
		try {
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery(String.format("SELECT * FROM students WHERE id = '%s'", id));
		    while (rs.next()) {
		        student = new Student(rs.getString("id"),rs.getString("name"),rs.getString("surname"),rs.getInt("courseId"), rs.getString("level"));
		    }
		    rs.close();
		    st.close();

		    return student;
		} catch (SQLException e) {
		    System.out.println("getData() - Error message: " + e.getMessage());
		}
		return student;
	}
	
	public static boolean isRatingQuestion(String columnName) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT type FROM questions";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String dataType = rs.getString("type");
                // Assuming "int8" represents a rating question
                return dataType.equals("int8");
            }
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
        return false;
    }

    public static List<Question> getQuestion(Connection conn) throws SQLException {
        List<Question> question = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM questions";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String currQuestion = rs.getString("question");
                String currDataType = rs.getString("type");
                int currId = rs.getInt("id");
                
                question.add(new Question(currId, currQuestion, currDataType));
            }
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }

        return question;
    }

    private static void closeResultSet(ResultSet rs) {
    	if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}

	private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	static void submitAnswers(List<Question> questions, Student student, String subject) throws SQLException {
		int subjectId = 0;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = String.format("SELECT id FROM subjects WHERE \"subject\" = '%s'", subject);
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                subjectId = rs.getInt("id");
            }
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }
		
			PreparedStatement ps = null;
		    try {
		        for (Question curr : questions) {
		        	String insertQuery = "INSERT INTO evaluation (\"studentId\", \"subjectId\", \"answer\", \"questionId\")"  + String.format(" VALUES ('%s', %d, '%s', %d)", student.getId(), subjectId, curr.getAnswer(), curr.getId());
		        	ps = conn.prepareStatement(insertQuery);
		        	ps.executeUpdate();
		        	closeStatement(ps);
		        }
		    } finally {
		    }
	}
	
	 public static List<Subject> getSubjects(Connection conn, Student student) throws SQLException {
	        List<Subject> subject = new ArrayList<>();

	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        try {
	            String query = String.format("SELECT * FROM subjects as s JOIN relationship as r ON r.\"subjectId\" = s.id JOIN course as c ON r.\"courseId\" = c.id JOIN students st ON r.\"courseId\" = st.\"courseId\" WHERE st.id = '%s'", student.getId());
	            stmt = conn.prepareStatement(query);
	            rs = stmt.executeQuery();
	            while (rs.next()) {
	                String currName = rs.getString("subject");
	                String currProf = rs.getString("prof");
	                int currId = rs.getInt("id");
	                
	                subject.add(new Subject(currId, currName, currProf));
	            }
	        } finally {
	            closeResultSet(rs);
	            closeStatement(stmt);
	        }

	        return subject;
	    }
	 
    private static void closeConnection() {
        if (conn != null) {
            try {
            	System.out.print("Connection closed.");
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
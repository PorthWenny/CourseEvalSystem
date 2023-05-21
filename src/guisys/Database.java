package guisys;

import java.util.*;
import java.sql.*;

public class Database {
	public static Connection getConnection() throws SQLException {

        String url = "jdbc:postgresql://db.tyrfvtvzbzushigzjzbp.supabase.co:5432/postgres";
        String username = "postgres";
        String password = "wow321wow123wow";

        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println("Connection successful! ✔\n");
        return conn;
    }
	
	public static void main (String[] args) throws SQLException {
		Connection connector = getConnection();
	}
	
	public static String getCourse (Connection conn, String id) {
		String course = "";
		try {
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery(String.format("SELECT course FROM students WHERE id = '%s'", id));
		    while (rs.next()) {
		        course = rs.getString("course");
		    }
		    rs.close();
		    st.close();
		    
		    return course;
		} catch (SQLException e) {
		    System.out.println("getData() - Error message: " + e.getMessage());
		}
		return course;
	}
	
	public static Student getStudent (Connection conn, String id) {
		Student student = new Student("", "", "", "", "");
		try {
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery(String.format("SELECT * FROM students WHERE id = '%s'", id));
		    while (rs.next()) {
		        student = new Student(rs.getString("id"),rs.getString("name"),rs.getString("surname"),rs.getString("course"), rs.getString("level"));
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
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            String query = "SELECT data_type FROM information_schema.columns WHERE table_name = 'evaluation' AND column_name = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, columnName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String dataType = rs.getString("data_type");
                // Assuming "int2" represents a rating question
                return dataType.equals("int2");
            }
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
        return false;
    }

    public static List<String> getQuestionColumns(Connection conn) throws SQLException {
        List<String> questionColumns = new ArrayList<>();

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT column_name FROM information_schema.columns WHERE table_name = 'evaluation' AND column_name NOT IN ('key', 'id', 'subject')";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                questionColumns.add(columnName);
            }
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
        }

        return questionColumns;
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

    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
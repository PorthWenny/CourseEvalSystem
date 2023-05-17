package guisys;

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
}
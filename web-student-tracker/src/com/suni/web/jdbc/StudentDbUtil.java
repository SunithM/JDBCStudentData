package com.suni.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;

	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {

			// 1-get the connection
			myConn = dataSource.getConnection();

			// 2-Create SQL statement.
			String sql = "select * from student order by last_name";
			myStmt = myConn.createStatement();

			// 3-Executing Query.
			myRs = myStmt.executeQuery(sql);

			// 4-Process result.

			while (myRs.next()) {
				// 1- Retrieve data from result set row.
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				// 2-Create new Student object.
				Student tempStudent = new Student(id, firstName, lastName, email);

				// 3-add it to the list of students.
				students.add(tempStudent);

			}

			// 5-Close JDBC.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			close(myConn, myStmt, myRs);
		}
		return students;
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myConn != null)
				myConn.close();
			if (myStmt != null)
				myStmt.close();
			if (myRs != null)
				myRs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addStudent(Student theStudent) {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {

			// db get connection
			myConn = dataSource.getConnection();
			// create sql for insert
			String sql = "insert into student " + "(first_name,last_name,email)" + "values(?,?,?)";
			myStmt = myConn.prepareStatement(sql);

			// set the param value for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			// execute sql insert
			myStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// clean up JDBC object
			close(myConn, myStmt, null);
		}

	}

	public Student getStudent(String theStudentId) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			// convert student id to int
			studentId = Integer.parseInt(theStudentId);
			// get connect to database
			myConn = dataSource.getConnection();

			// create sql to get selected student
			String sql = "select * from student where id=?";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set the param
			myStmt.setInt(1, studentId);

			// execute the statement
			myRs = myStmt.executeQuery();

			// retrive the data from result set
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				theStudent = new Student(studentId, firstName, lastName, email);

			} else {
				throw new Exception("Could not find student id:" + studentId);
			}
			return theStudent;
		} finally {
			// clean up jdbc
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql update statement
			String sql = "update student " + "set first_name=?,last_name=?,email=? " + "where id=?";

			// preapare statement
			myStmt = myConn.prepareStatement(sql);

			// set param
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			// execute SQL statement
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}

	}

	public void deleteStudent(int id) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql update statement
			String sql = "delete from student "+ "where id=?";

			// preapare statement
			myStmt = myConn.prepareStatement(sql);

			// set param
			myStmt.setInt(1, id);

			// execute SQL statement
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
		
	}

}

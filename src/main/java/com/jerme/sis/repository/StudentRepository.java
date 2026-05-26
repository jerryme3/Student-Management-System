package com.jerme.sis.repository;

import com.jerme.sis.entities.Student;
import com.jerme.sis.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository extends Repository<Student> {

    @Override
    public void createTable() {
        String createTable = """
                CREATE TABLE IF NOT EXISTS students (
                id SERIAL PRIMARY KEY,
                school_id INT,
                first_name VARCHAR(30) NOT NULL,
                middle_name VARCHAR(30) NOT NULL,
                last_name VARCHAR(25) NOT NULL,
                email VARCHAR(50) NOT NULL,
                hashed_password VARCHAR(100) NOT NULL,
                program VARCHAR(10) NOT NULL,
                gwa NUMERIC(3, 2) CHECK (gwa >= 0.0 AND gwa <= 5.0)
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            if (stmt.execute(createTable))
                System.out.println("Table has been created.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Student> insert(Student student) {
        String insert = "INSERT INTO students (school_id, first_name, middle_name, last_name, email, hashed_password, program, gwa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(insert)) {

            p.setInt(1, student.getId());
            p.setString(2, student.getFirstName());
            p.setString(3, student.getMiddleName());
            p.setString(4, student.getLastName());
            p.setString(5, student.getEmail());
            p.setString(6, student.getPassword());
            p.setString(7, student.getProgram());
            p.setDouble(8, student.getGwa());

            return p.executeUpdate() > 0 ? Optional.of(student) : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(int schoolId) {
        String delete = "DELETE FROM students WHERE school_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(delete)) {

            p.setInt(1, schoolId);

            if (p.executeUpdate() > 0) return true;

        } catch (SQLException e) {
          e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Student student) {
        String update = """
                UPDATE students
                SET first_name = ?,
                middle_name = ?,
                last_name = ?,
                email = ?,
                hashed_password = ?,
                program = ?,
                gwa = ?
                WHERE school_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setInt(8, student.getId());
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getMiddleName());
            ps.setString(3, student.getLastName());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPassword());
            ps.setString(6, student.getProgram());
            ps.setDouble(7, student.getGwa());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Student> read(String query) {
        List<Student> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

        while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean existsById(int id) { //quick look up in db
        String retrieve = "SELECT 1 FROM students WHERE school_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(retrieve)) {

            p.setInt(1, id);

            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        String retrieve = "SELECT 1 FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(retrieve)) {

            p.setString(1, email);

            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Optional<Student> findById(int id) { //para sa searching
        String retrieve = "SELECT * FROM students WHERE school_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(retrieve)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        String retrieve = "SELECT * FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(retrieve)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    protected Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("school_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("hashed_password"),
                rs.getString("program"),
                rs.getDouble("gwa")
        );
    }
}
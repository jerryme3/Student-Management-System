package com.jerme.sis.repository;

import com.jerme.sis.db.DatabaseConnection;
import com.jerme.sis.entities.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherRepository extends Repository<Teacher> {

    @Override
    public void createTable() {

        String createTable = """
                CREATE TABLE IF NOT EXISTS teachers (
                id SERIAL PRIMARY KEY,
                teacher_id INT,
                first_name VARCHAR(30) NOT NULL,
                middle_name VARCHAR(30) NOT NULL,
                last_name VARCHAR(25) NOT NULL,
                email VARCHAR(50) NOT NULL,
                hashed_password VARCHAR(75) NOT NULL,
                department VARCHAR(50) NOT NULL,
                teacher_level INT
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
    public Optional<Teacher> insert(Teacher teacher) {
        String insert = "INSERT INTO teachers (teacher_id, first_name, middle_name, last_name, email, hashed_password, department, teacher_level) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(insert)) {

            p.setInt(1, teacher.getId());
            p.setString(2, teacher.getFirstName());
            p.setString(3, teacher.getMiddleName());
            p.setString(4, teacher.getLastName());
            p.setString(5, teacher.getEmail());
            p.setString(6, teacher.getPassword());
            p.setString(7, teacher.getDepartment());
            p.setInt(8, teacher.getTeacherLevel());

            return p.executeUpdate() > 0 ? Optional.of(teacher) : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(int teacherId) {
        String delete = "DELETE FROM teachers WHERE teacher_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement p = conn.prepareStatement(delete)) {

            p.setInt(1, teacherId);

            if (p.executeUpdate() > 0) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Teacher teacher) {
        String update = """
                UPDATE teachers
                SET first_name = ?,
                middle_name = ?,
                last_name = ?,
                email = ?,
                hashed_password = ?,
                department = ?,
                teacher_level = ?
                WHERE teacher_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(update)) {

            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getMiddleName());
            ps.setString(3, teacher.getLastName());
            ps.setString(4, teacher.getEmail());
            ps.setString(5, teacher.getPassword());
            ps.setString(6, teacher.getDepartment());
            ps.setInt(7, teacher.getTeacherLevel());
            ps.setInt(8, teacher.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Teacher> read(String query) {
        List<Teacher> list = new ArrayList<>();

        String readAll = """
                SELECT * FROM teachers
                ORDER BY last_name
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(readAll);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean existsById(int id) { //quick look up in db
        String retrieve = "SELECT 1 FROM teachers WHERE teacher_id = ?";

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
        String retrieve = "SELECT 1 FROM teachers WHERE email = ?";

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
    public Optional<Teacher> findById(int id) {
        String retrieve = "SELECT * FROM teachers WHERE teacher_id = ?";

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
    public Optional<Teacher> findByEmail(String email) {
        String retrieve = "SELECT * FROM teachers WHERE email = ?";

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
    protected Teacher mapRow(ResultSet rs) throws SQLException {
        return new Teacher(
                rs.getInt("teacher_id"),
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("hashed_password"),
                rs.getString("department"),
                rs.getInt("teacher_level")
        );
    }
}
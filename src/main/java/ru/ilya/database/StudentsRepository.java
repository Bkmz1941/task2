package ru.ilya.database;

import ru.ilya.models.StudentModel;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentsRepository {
    private Database database;

    public StudentsRepository() {
        this.database = Database.get();
        this.database.connect();
    }

    public List<StudentModel> getStudents() {
        String statement = "SELECT * FROM students";
        List<StudentModel> result = new ArrayList<>();
        try (
                Statement stmt = this.database.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(statement);
        ) {
            while (rs.next()) {
                StudentModel student = new StudentModel(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("third_name"),
                        rs.getString("birthday_at"),
                        rs.getString("group_name")
                );
                result.add(student);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    public Optional<StudentModel> getStudent(int modelId) {
        Optional<StudentModel> optionalStudentModel = Optional.empty();

        String statement = "SELECT * FROM students WHERE id = ?";
        try (
                PreparedStatement ps = this.database.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setInt(1, modelId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                optionalStudentModel = Optional.of(new StudentModel(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("second_name"),
                        rs.getString("third_name"),
                        rs.getString("birthday_at"),
                        rs.getString("group_name")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return optionalStudentModel;
    }

    public void addStudent(StudentModel studentModel) {
        String statement = "INSERT INTO students (first_name, second_name, third_name, birthday_at, group_name) VALUES (?,?,?,?,?)";
        try (
                PreparedStatement ps = this.database.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        ) {
            Date date = null;
            if (studentModel.getBirthdayAt() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate localDate = LocalDate.parse(studentModel.getBirthdayAt(), formatter);
                date = Date.valueOf(localDate);
            }

            ps.setString(1, studentModel.getFirstName());
            ps.setString(2, studentModel.getSecondName());
            ps.setString(3, studentModel.getThirdName());
            ps.setDate(4, date != null ? new java.sql.Date(date.getTime()) : null);
            ps.setString(5, studentModel.getGroupName());

            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                studentModel.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteStudent(int modelId) {
        String statement = "DELETE FROM students WHERE id = ?";
        try (
                PreparedStatement ps = this.database.getConnection().prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setInt(1, modelId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

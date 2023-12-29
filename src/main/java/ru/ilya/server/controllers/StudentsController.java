package ru.ilya.server.controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.ilya.database.StudentsRepository;
import ru.ilya.exceptions.ModelNotFoundException;
import ru.ilya.models.ResponseModel;
import ru.ilya.models.StudentModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsController extends Controller {
    private final StudentsRepository studentsRepository;

    public StudentsController() {
        super();
        this.studentsRepository = new StudentsRepository();
    }

    public void index(HttpExchange exchange) {
        try {
            List<StudentModel> students = this.studentsRepository.getStudents();
            ResponseModel<List<StudentModel>> response = new ResponseModel<>(students, null);
            this.response(exchange, response);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }

    public void get(HttpExchange exchange, int modelId) {
        try {
            Optional<StudentModel> optionalStudentModel = this.studentsRepository.getStudent(modelId);
            if (optionalStudentModel.isEmpty()) {
                throw new ModelNotFoundException("Model not found");
            }

            ResponseModel<StudentModel> response = new ResponseModel<>(optionalStudentModel.get(), null);
            this.response(exchange, response);
        } catch (ModelNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }

    public void create(HttpExchange exchange) {
        try {
            InputStream inputStream = exchange.getRequestBody();
            Reader reader = new InputStreamReader(inputStream);
            StudentModel student = this.gson.fromJson(reader, StudentModel.class);
            this.studentsRepository.addStudent(student);
            ResponseModel<StudentModel> response = new ResponseModel<>(student, null);
            this.response(exchange, response);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }

    public void delete(HttpExchange exchange, int modelId) {
        try {
            Optional<StudentModel> optionalStudentModel = this.studentsRepository.getStudent(modelId);
            if (optionalStudentModel.isEmpty()) {
                throw new ModelNotFoundException("Model not found");
            }
            this.studentsRepository.deleteStudent(modelId);
            ResponseModel<Boolean> response = new ResponseModel<>(null, "Success");
            this.response(exchange, response);
        } catch (ModelNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong");
        }
    }
}

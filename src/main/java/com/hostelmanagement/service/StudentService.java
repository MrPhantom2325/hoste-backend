package com.hostelmanagement.service;

import com.hostelmanagement.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    private static final String SHEET_NAME = "Students";

    public List<Student> getAllStudents() throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, Student.getHeaderRow());
        
        List<List<Object>> values = googleSheetsService.readSheet(SHEET_NAME + "!A2:H");
        List<Student> students = new ArrayList<>();
        
        for (List<Object> row : values) {
            if (!row.isEmpty() && !row.get(0).toString().trim().isEmpty()) {
                students.add(Student.fromSheetRow(row));
            }
        }
        
        return students;
    }

    public Student getStudentById(String id) throws IOException {
        List<Student> students = getAllStudents();
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Student saveStudent(Student student) throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, Student.getHeaderRow());
        
        if (student.getId() == null || student.getId().isEmpty()) {
            // Create new student
            student.setId(UUID.randomUUID().toString());
            List<List<Object>> newRow = List.of(student.toSheetRow());
            googleSheetsService.appendToSheet(SHEET_NAME + "!A:H", newRow);
        } else {
            // Update existing student
            int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, student.getId());
            if (rowIndex != -1) {
                googleSheetsService.updateRow(SHEET_NAME, rowIndex, student.toSheetRow());
            } else {
                // Student not found, create new one
                List<List<Object>> newRow = List.of(student.toSheetRow());
                googleSheetsService.appendToSheet(SHEET_NAME + "!A:H", newRow);
            }
        }
        
        return student;
    }

    public void deleteStudent(String id) throws IOException {
        int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, id);
        if (rowIndex != -1) {
            googleSheetsService.deleteRow(SHEET_NAME, rowIndex);
        }
    }

    public List<Student> getStudentsByRoom(String room) throws IOException {
        List<Student> allStudents = getAllStudents();
        return allStudents.stream()
                .filter(student -> student.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByBlock(String block) throws IOException {
        List<Student> allStudents = getAllStudents();
        return allStudents.stream()
                .filter(student -> student.getRoom().startsWith(block + "-"))
                .collect(Collectors.toList());
    }
} 
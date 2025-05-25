package com.hostelmanagement.service;

import com.hostelmanagement.model.AttendanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    private static final String SHEET_NAME = "Attendance";

    public List<AttendanceRecord> getAllAttendance() throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, AttendanceRecord.getHeaderRow());
        
        List<List<Object>> values = googleSheetsService.readSheet(SHEET_NAME + "!A2:H");
        List<AttendanceRecord> records = new ArrayList<>();
        
        for (List<Object> row : values) {
            if (!row.isEmpty() && !row.get(0).toString().trim().isEmpty()) {
                records.add(AttendanceRecord.fromSheetRow(row));
            }
        }
        
        return records;
    }

    public AttendanceRecord getAttendanceById(String id) throws IOException {
        List<AttendanceRecord> records = getAllAttendance();
        return records.stream()
                .filter(record -> record.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<AttendanceRecord> getAttendanceByDate(String date) throws IOException {
        List<AttendanceRecord> allRecords = getAllAttendance();
        return allRecords.stream()
                .filter(record -> record.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public AttendanceRecord saveAttendanceRecord(AttendanceRecord record) throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, AttendanceRecord.getHeaderRow());
        
        if (record.getId() == null || record.getId().isEmpty()) {
            // Create new record
            record.setId(UUID.randomUUID().toString());
            List<List<Object>> newRow = List.of(record.toSheetRow());
            googleSheetsService.appendToSheet(SHEET_NAME + "!A:H", newRow);
        } else {
            // Update existing record
            int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, record.getId());
            if (rowIndex != -1) {
                googleSheetsService.updateRow(SHEET_NAME, rowIndex, record.toSheetRow());
            } else {
                // Record not found, create new one
                List<List<Object>> newRow = List.of(record.toSheetRow());
                googleSheetsService.appendToSheet(SHEET_NAME + "!A:H", newRow);
            }
        }
        
        return record;
    }

    public void saveAttendanceForDate(String date, List<AttendanceRecord> records) throws IOException {
        // Delete existing records for this date
        List<AttendanceRecord> existingRecords = getAttendanceByDate(date);
        for (AttendanceRecord existing : existingRecords) {
            deleteAttendanceRecord(existing.getId());
        }
        
        // Save new records
        for (AttendanceRecord record : records) {
            record.setDate(date);
            record.setId(UUID.randomUUID().toString());
            saveAttendanceRecord(record);
        }
    }

    public void deleteAttendanceRecord(String id) throws IOException {
        int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, id);
        if (rowIndex != -1) {
            googleSheetsService.deleteRow(SHEET_NAME, rowIndex);
        }
    }

    public List<AttendanceRecord> getAttendanceByStudentId(String studentId) throws IOException {
        List<AttendanceRecord> allRecords = getAllAttendance();
        return allRecords.stream()
                .filter(record -> record.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getAttendanceByDateRange(String startDate, String endDate) throws IOException {
        List<AttendanceRecord> allRecords = getAllAttendance();
        return allRecords.stream()
                .filter(record -> record.getDate().compareTo(startDate) >= 0 && 
                                record.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
    }
} 
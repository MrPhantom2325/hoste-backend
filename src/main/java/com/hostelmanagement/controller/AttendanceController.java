package com.hostelmanagement.controller;

import com.hostelmanagement.model.AttendanceRecord;
import com.hostelmanagement.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> getAllAttendance() {
        try {
            List<AttendanceRecord> records = attendanceService.getAllAttendance();
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceRecord> getAttendanceById(@PathVariable String id) {
        try {
            AttendanceRecord record = attendanceService.getAttendanceById(id);
            if (record != null) {
                return new ResponseEntity<>(record, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceByDate(@RequestParam String date) {
        try {
            List<AttendanceRecord> records = attendanceService.getAttendanceByDate(date);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> saveAttendance(@RequestBody Map<String, Object> requestBody) {
        try {
            String date = (String) requestBody.get("date");
            Map<String, Object> attendanceData = (Map<String, Object>) requestBody.get("attendance");
            
            // Convert attendance data to AttendanceRecord objects
            List<AttendanceRecord> records = new java.util.ArrayList<>();
            
            for (Map.Entry<String, Object> entry : attendanceData.entrySet()) {
                String studentId = entry.getKey();
                Boolean isPresent = (Boolean) entry.getValue();
                
                // You would typically fetch student details here
                // For now, creating a basic record
                AttendanceRecord record = new AttendanceRecord();
                record.setDate(date);
                record.setStudentId(studentId);
                record.setStatus(isPresent ? "present" : "absent");
                record.setCheckInTime(isPresent ? "08:00 AM" : "-");
                record.setCheckOutTime(isPresent ? "10:00 PM" : "-");
                
                records.add(record);
            }
            
            attendanceService.saveAttendanceForDate(date, records);
            return new ResponseEntity<>("Attendance saved successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to save attendance", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceByStudentId(@PathVariable String studentId) {
        try {
            List<AttendanceRecord> records = attendanceService.getAttendanceByStudentId(studentId);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/range")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        try {
            List<AttendanceRecord> records = attendanceService.getAttendanceByDateRange(startDate, endDate);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceRecord(@PathVariable String id) {
        try {
            AttendanceRecord existingRecord = attendanceService.getAttendanceById(id);
            if (existingRecord != null) {
                attendanceService.deleteAttendanceRecord(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 
package com.hostelmanagement.model;

import java.util.Arrays;
import java.util.List;

public class AttendanceRecord {
    private String id;
    private String date;
    private String studentId;
    private String name;
    private String room;
    private String checkInTime;
    private String checkOutTime;
    private String status; // "present", "absent"

    // Default constructor
    public AttendanceRecord() {}

    // Constructor with all fields
    public AttendanceRecord(String id, String date, String studentId, String name, String room, 
                           String checkInTime, String checkOutTime, String status) {
        this.id = id;
        this.date = date;
        this.studentId = studentId;
        this.name = name;
        this.room = room;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }

    // Convert to Google Sheets row
    public List<Object> toSheetRow() {
        return Arrays.asList(id, date, studentId, name, room, checkInTime, checkOutTime, status);
    }

    // Create from Google Sheets row
    public static AttendanceRecord fromSheetRow(List<Object> row) {
        if (row.size() < 8) {
            while (row.size() < 8) {
                row.add("");
            }
        }
        
        return new AttendanceRecord(
            row.get(0).toString(),
            row.get(1).toString(),
            row.get(2).toString(),
            row.get(3).toString(),
            row.get(4).toString(),
            row.get(5).toString(),
            row.get(6).toString(),
            row.get(7).toString()
        );
    }

    // Static method to get header row
    public static List<Object> getHeaderRow() {
        return Arrays.asList("ID", "Date", "Student ID", "Name", "Room", "Check In Time", "Check Out Time", "Status");
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 
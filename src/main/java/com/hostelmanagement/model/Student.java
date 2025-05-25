package com.hostelmanagement.model;

import java.util.Arrays;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String emergencyContact;
    private String room;
    private String joiningDate;

    // Default constructor
    public Student() {}

    // Constructor with all fields
    public Student(String id, String name, String email, String phone, String address, 
                  String emergencyContact, String room, String joiningDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.room = room;
        this.joiningDate = joiningDate;
    }

    // Convert to Google Sheets row (List of Objects)
    public List<Object> toSheetRow() {
        return Arrays.asList(id, name, email, phone, address, emergencyContact, room, joiningDate);
    }

    // Create from Google Sheets row
    public static Student fromSheetRow(List<Object> row) {
        if (row.size() < 8) {
            // Pad with empty strings if row is incomplete
            while (row.size() < 8) {
                row.add("");
            }
        }
        
        return new Student(
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
        return Arrays.asList("ID", "Name", "Email", "Phone", "Address", "Emergency Contact", "Room", "Joining Date");
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }
} 
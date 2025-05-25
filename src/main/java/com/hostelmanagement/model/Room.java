package com.hostelmanagement.model;

import java.util.Arrays;
import java.util.List;

public class Room {
    private String id;
    private String roomNumber;
    private String block;
    private String floor;
    private int capacity;
    private int occupants;
    private String status; // "occupied", "available", "maintenance"

    // Default constructor
    public Room() {}

    // Constructor with all fields
    public Room(String id, String roomNumber, String block, String floor, 
               int capacity, int occupants, String status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.block = block;
        this.floor = floor;
        this.capacity = capacity;
        this.occupants = occupants;
        this.status = status;
    }

    // Convert to Google Sheets row
    public List<Object> toSheetRow() {
        return Arrays.asList(id, roomNumber, block, floor, capacity, occupants, status);
    }

    // Create from Google Sheets row
    public static Room fromSheetRow(List<Object> row) {
        if (row.size() < 7) {
            while (row.size() < 7) {
                row.add("");
            }
        }
        
        return new Room(
            row.get(0).toString(),
            row.get(1).toString(),
            row.get(2).toString(),
            row.get(3).toString(),
            Integer.parseInt(row.get(4).toString().isEmpty() ? "0" : row.get(4).toString()),
            Integer.parseInt(row.get(5).toString().isEmpty() ? "0" : row.get(5).toString()),
            row.get(6).toString()
        );
    }

    // Static method to get header row
    public static List<Object> getHeaderRow() {
        return Arrays.asList("ID", "Room Number", "Block", "Floor", "Capacity", "Occupants", "Status");
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupants() {
        return occupants;
    }

    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 
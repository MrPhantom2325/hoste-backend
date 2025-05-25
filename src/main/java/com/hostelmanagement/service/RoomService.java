package com.hostelmanagement.service;

import com.hostelmanagement.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    private static final String SHEET_NAME = "Rooms";

    public List<Room> getAllRooms() throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, Room.getHeaderRow());
        
        List<List<Object>> values = googleSheetsService.readSheet(SHEET_NAME + "!A2:G");
        List<Room> rooms = new ArrayList<>();
        
        for (List<Object> row : values) {
            if (!row.isEmpty() && !row.get(0).toString().trim().isEmpty()) {
                rooms.add(Room.fromSheetRow(row));
            }
        }
        
        return rooms;
    }

    public Room getRoomById(String id) throws IOException {
        List<Room> rooms = getAllRooms();
        return rooms.stream()
                .filter(room -> room.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Room saveRoom(Room room) throws IOException {
        // Initialize sheet with headers if empty
        googleSheetsService.initializeSheetIfEmpty(SHEET_NAME, Room.getHeaderRow());
        
        if (room.getId() == null || room.getId().isEmpty()) {
            // Create new room
            room.setId(UUID.randomUUID().toString());
            List<List<Object>> newRow = List.of(room.toSheetRow());
            googleSheetsService.appendToSheet(SHEET_NAME + "!A:G", newRow);
        } else {
            // Update existing room
            int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, room.getId());
            if (rowIndex != -1) {
                googleSheetsService.updateRow(SHEET_NAME, rowIndex, room.toSheetRow());
            } else {
                // Room not found, create new one
                List<List<Object>> newRow = List.of(room.toSheetRow());
                googleSheetsService.appendToSheet(SHEET_NAME + "!A:G", newRow);
            }
        }
        
        return room;
    }

    public void deleteRoom(String id) throws IOException {
        int rowIndex = googleSheetsService.findRowIndexById(SHEET_NAME, id);
        if (rowIndex != -1) {
            googleSheetsService.deleteRow(SHEET_NAME, rowIndex);
        }
    }

    public List<Room> getRoomsByBlock(String block) throws IOException {
        List<Room> allRooms = getAllRooms();
        return allRooms.stream()
                .filter(room -> room.getBlock().equals(block))
                .collect(Collectors.toList());
    }

    public List<Room> getRoomsByStatus(String status) throws IOException {
        List<Room> allRooms = getAllRooms();
        return allRooms.stream()
                .filter(room -> room.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Room> getAvailableRooms() throws IOException {
        return getRoomsByStatus("available");
    }
} 
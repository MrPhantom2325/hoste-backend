package com.hostelmanagement.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleSheetsService {

    @Autowired
    private Sheets sheetsService;

    @Value("${google.sheets.spreadsheet.id}")
    private String spreadsheetId;

    // Read data from a specific range
    public List<List<Object>> readSheet(String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        return values != null ? values : new ArrayList<>();
    }

    // Write data to a specific range
    public void writeSheet(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);
        
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    // Append data to a sheet
    public void appendToSheet(String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange().setValues(values);
        
        sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    // Clear a range
    public void clearSheet(String range) throws IOException {
        ClearValuesRequest clearRequest = new ClearValuesRequest();
        sheetsService.spreadsheets().values()
                .clear(spreadsheetId, range, clearRequest)
                .execute();
    }

    // Update a specific row
    public void updateRow(String sheetName, int rowIndex, List<Object> values) throws IOException {
        String range = sheetName + "!" + rowIndex + ":" + rowIndex;
        List<List<Object>> updateValues = Arrays.asList(values);
        writeSheet(range, updateValues);
    }

    // Delete a row by shifting cells up
    public void deleteRow(String sheetName, int rowIndex) throws IOException {
        // Get sheet ID first
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        Sheet sheet = null;
        for (Sheet s : spreadsheet.getSheets()) {
            if (s.getProperties().getTitle().equals(sheetName)) {
                sheet = s;
                break;
            }
        }
        
        if (sheet != null) {
            DeleteDimensionRequest deleteRequest = new DeleteDimensionRequest()
                    .setRange(new DimensionRange()
                            .setSheetId(sheet.getProperties().getSheetId())
                            .setDimension("ROWS")
                            .setStartIndex(rowIndex - 1) // 0-based index
                            .setEndIndex(rowIndex));

            BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(Arrays.asList(new Request().setDeleteDimension(deleteRequest)));

            sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
        }
    }

    // Initialize sheet with headers if empty
    public void initializeSheetIfEmpty(String sheetName, List<Object> headers) throws IOException {
        List<List<Object>> values = readSheet(sheetName + "!A1:Z1");
        
        if (values.isEmpty()) {
            List<List<Object>> headerRow = Arrays.asList(headers);
            writeSheet(sheetName + "!A1:Z1", headerRow);
        }
    }

    // Get the next available row number
    public int getNextRowNumber(String sheetName) throws IOException {
        List<List<Object>> values = readSheet(sheetName + "!A:A");
        return values.size() + 1;
    }

    // Find row index by ID (assuming ID is in column A)
    public int findRowIndexById(String sheetName, String id) throws IOException {
        List<List<Object>> values = readSheet(sheetName + "!A:A");
        
        for (int i = 0; i < values.size(); i++) {
            if (!values.get(i).isEmpty() && values.get(i).get(0).toString().equals(id)) {
                return i + 1; // 1-based index
            }
        }
        
        return -1; // Not found
    }
} 
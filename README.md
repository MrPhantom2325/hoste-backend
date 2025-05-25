# Hostel Management System Backend

This is a Spring Boot backend application that uses Google Sheets as the data storage for a hostel management system.

## Features

- Student management (add, update, delete, view)
- Room management (add, update, delete, view)
- Attendance tracking
- Google Sheets integration for easy data management

## Prerequisites

- Java 17 or higher
- Maven
- Google Cloud Platform account
- Google Sheets API enabled

## Setup Instructions

### 1. Google Sheets Setup

1. **Create a Google Sheet**:
   - Go to [Google Sheets](https://sheets.google.com)
   - Create a new spreadsheet
   - Name it "Hostel Management System"
   - Create three sheets: "Students", "Rooms", "Attendance"

2. **Get the Spreadsheet ID**:
   - From the URL: `https://docs.google.com/spreadsheets/d/SPREADSHEET_ID/edit`
   - Copy the `SPREADSHEET_ID` part

3. **Set up Google Cloud Project**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing one
   - Enable the Google Sheets API
   - Create a service account
   - Download the JSON credentials file
   - Rename it to `google-sheets-credentials.json`

4. **Share the Sheet**:
   - Open your Google Sheet
   - Click "Share" button
   - Add the service account email (from the JSON file) with "Editor" permissions

### 2. Backend Setup

1. **Update Configuration**:
   - Place `google-sheets-credentials.json` in `src/main/resources/`
   - Update `application.properties` with your spreadsheet ID:
     ```properties
     google.sheets.spreadsheet.id=YOUR_SPREADSHEET_ID_HERE
     ```

2. **Build and Run**:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Students
- GET `/api/students` - Get all students
- GET `/api/students/{id}` - Get student by ID
- POST `/api/students` - Create new student
- PUT `/api/students/{id}` - Update student
- DELETE `/api/students/{id}` - Delete student
- GET `/api/students/room/{room}` - Get students by room
- GET `/api/students/block/{block}` - Get students by block

### Rooms
- GET `/api/rooms` - Get all rooms
- GET `/api/rooms/{id}` - Get room by ID
- POST `/api/rooms` - Create new room
- PUT `/api/rooms/{id}` - Update room
- DELETE `/api/rooms/{id}` - Delete room
- GET `/api/rooms/block/{block}` - Get rooms by block
- GET `/api/rooms/status/{status}` - Get rooms by status
- GET `/api/rooms/available` - Get available rooms

### Attendance
- GET `/api/attendance` - Get all attendance records
- GET `/api/attendance/{id}` - Get attendance record by ID
- GET `/api/attendance/date?date=YYYY-MM-DD` - Get attendance by date
- POST `/api/attendance` - Save attendance records
- GET `/api/attendance/student/{studentId}` - Get attendance by student
- GET `/api/attendance/range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` - Get attendance by date range
- DELETE `/api/attendance/{id}` - Delete attendance record

## Google Sheets Structure

The application will automatically create headers in your sheets:

### Students Sheet
| ID | Name | Email | Phone | Address | Emergency Contact | Room | Joining Date |

### Rooms Sheet
| ID | Room Number | Block | Floor | Capacity | Occupants | Status |

### Attendance Sheet
| ID | Date | Student ID | Name | Room | Check In Time | Check Out Time | Status |

## Benefits of Using Google Sheets

1. **Easy Data Management**: Non-technical users can view and edit data directly in Google Sheets
2. **Real-time Collaboration**: Multiple users can work on the data simultaneously
3. **Built-in Backup**: Google automatically backs up your data
4. **Cost-effective**: No database hosting costs
5. **Familiar Interface**: Most users are familiar with spreadsheet interfaces
6. **Data Export**: Easy to export data to various formats (CSV, Excel, PDF)
7. **Sharing and Permissions**: Easy to control who can view/edit the data 
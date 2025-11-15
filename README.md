# Booking Service API Documentation

This Spring Boot application provides a complete booking service API that matches the Genda Tickets API specification.

## Base URL
```
http://localhost:8076
```

## API Endpoints

### 1. Create Booking
**POST** `/bookings/book`

Creates a new booking for a user.

**Request Body:**
```json
{
    "userId": "string",
    "passengerName": "string",
    "passengerEmail": "string",
    "passengerPhone": "string",
    "departureProvince": "string",
    "departureDistrict": "string",
    "destinationProvince": "string",
    "destinationDistrict": "string",
    "travelDate": "yyyy-MM-ddTHH:mm:ss",
    "price": 15.00,
    "numberOfPassengers": 1,
    "seatNumbers": "string (optional)",
    "vehicleInfo": "string (optional)"
}
```

**Response:** `201 Created`
```json
{
    "id": 1,
    "bookingId": "BKG-12345678",
    "userId": "user123",
    "passengerName": "John Doe",
    "passengerEmail": "john@example.com",
    "passengerPhone": "+1234567890",
    "departureProvince": "Kigali",
    "departureDistrict": "Gasabo",
    "destinationProvince": "Eastern",
    "destinationDistrict": "Rwamagana",
    "travelDate": "2024-11-15T10:00:00",
    "bookingDate": "2024-11-14T14:30:00",
    "price": 15.00,
    "numberOfPassengers": 1,
    "status": "CONFIRMED",
    "seatNumbers": "A1",
    "vehicleInfo": "Bus 001"
}
```

### 2. List User's Bookings
**GET** `/bookings/user/bookings?userId={userId}`

Retrieves all bookings for a specific user.

**Response:** `200 OK`
```json
[
    {
        "id": 1,
        "bookingId": "BKG-12345678",
        "userId": "user123",
        "passengerName": "John Doe",
        "status": "CONFIRMED",
        ...
    }
]
```

### 3. Get Booking Details
**GET** `/bookings/user/bookings/{bookingId}?userId={userId}`

Retrieves detailed information about a specific booking.

**Response:** `200 OK` (same format as create booking response)

### 4. Get Booking History
**GET** `/bookings/user/history?userId={userId}`

Retrieves the complete booking history for a user (same as list bookings).

**Response:** `200 OK` (array of bookings)

### 5. Cancel Booking
**PUT** `/bookings/{bookingId}/cancel?userId={userId}`

Cancels a specific booking.

**Response:** `200 OK`
```json
{
    "id": 1,
    "bookingId": "BKG-12345678",
    "status": "CANCELLED",
    ...
}
```

### 6. Get User Booking Statistics
**GET** `/bookings/user/stats?userId={userId}`

Retrieves statistics about a user's booking activity.

**Response:** `200 OK`
```json
{
    "totalBookings": 10,
    "confirmedBookings": 7,
    "cancelledBookings": 1,
    "completedBookings": 2,
    "totalSpent": 150.00,
    "mostVisitedDestination": "Rwamagana"
}
```

### 7. Download Ticket PDF
**POST** `/bookings/{bookingId}/download-pdf?userId={userId}`

Generates and downloads a PDF ticket for the booking.

**Response:** `200 OK` (PDF file download)

### 8. Test Endpoint
**GET** `/bookings/test`

Simple test endpoint to verify the API is running.

**Response:** `200 OK`
```
"Booking API working smoothly"
```

## Database Schema

### Bookings Table
- `id` (Primary Key)
- `booking_id` (Unique identifier)
- `user_id`
- `passenger_name`
- `passenger_email`
- `passenger_phone`
- `departure_province`
- `departure_district`
- `destination_province`
- `destination_district`
- `travel_date`
- `booking_date`
- `price`
- `number_of_passengers`
- `status` (CONFIRMED, CANCELLED, COMPLETED)
- `seat_numbers`
- `vehicle_info`

### Routes Table (existing)
- `id` (Primary Key)
- `departure_province`
- `departure_district`
- `destination_province`
- `destination_district`
- `price`

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK` - Success
- `201 Created` - Resource created successfully
- `400 Bad Request` - Validation errors or business logic errors
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

## Features

1. **Complete Booking Management**: Create, view, cancel bookings
2. **User Statistics**: Track user booking patterns and spending
3. **PDF Generation**: Generate printable tickets using iText7
4. **Data Validation**: Input validation for all booking requests
5. **Status Management**: Track booking status throughout lifecycle
6. **History Tracking**: Complete booking history for users

## Running the Application

1. Ensure PostgreSQL database is running and configured in `application.properties`
2. Run `mvn clean install` to build the project
3. Run `mvn spring-boot:run` to start the application
4. Access the API at `http://localhost:8076`

## Dependencies

- Spring Boot 3.3.1
- Spring Data JPA
- PostgreSQL Driver
- iText7 (PDF generation)
- Lombok
- Jackson (JSON processing)
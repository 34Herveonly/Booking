package com.example.Booking.Controller;

import com.example.Booking.Service.BookingService;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.dto.UserBookingStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * POST /bookings/book - Create booking
     */
    @PostMapping("/book")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating booking: " + e.getMessage());
        }
    }

    /**
     * GET /bookings/user/bookings - List user's bookings
     */
    @GetMapping("/user/bookings")
    public ResponseEntity<?> getUserBookings(@RequestParam String userId) {
        try {
            List<BookingResponse> bookings = bookingService.getUserBookings(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving bookings: " + e.getMessage());
        }
    }

    /**
     * GET /bookings/user/bookings/{bookingId} - Get booking details
     */
    @GetMapping("/user/bookings/{bookingId}")
    public ResponseEntity<?> getBookingDetails(
            @PathVariable String bookingId,
            @RequestParam String userId) {
        try {
            Optional<BookingResponse> booking = bookingService.getBookingDetails(bookingId, userId);
            if (booking.isPresent()) {
                return ResponseEntity.ok(booking.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving booking details: " + e.getMessage());
        }
    }

    /**
     * GET /bookings/user/history - Booking history
     */
    @GetMapping("/user/history")
    public ResponseEntity<?> getUserBookingHistory(@RequestParam String userId) {
        try {
            List<BookingResponse> history = bookingService.getUserBookingHistory(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving booking history: " + e.getMessage());
        }
    }

    /**
     * PUT /bookings/{bookingId}/cancel - Cancel booking
     */
    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<?> cancelBooking(
            @PathVariable String bookingId,
            @RequestParam String userId) {
        try {
            BookingResponse cancelledBooking = bookingService.cancelBooking(bookingId, userId);
            return ResponseEntity.ok(cancelledBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error cancelling booking: " + e.getMessage());
        }
    }

    /**
     * GET /bookings/user/stats - User booking stats
     */
    @GetMapping("/user/stats")
    public ResponseEntity<?> getUserBookingStats(@RequestParam String userId) {
        try {
            UserBookingStats stats = bookingService.getUserBookingStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving user stats: " + e.getMessage());
        }
    }

    /**
     * POST /bookings/{bookingId}/download-pdf - Download ticket PDF
     */
    @PostMapping("/{bookingId}/download-pdf")
    public ResponseEntity<?> downloadTicketPdf(
            @PathVariable String bookingId,
            @RequestParam String userId) {
        try {
            byte[] pdfBytes = bookingService.generateTicketPdf(bookingId, userId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket-" + bookingId + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF: " + e.getMessage());
        }
    }

    /**
     * Test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("Booking API working smoothly");
    }
}
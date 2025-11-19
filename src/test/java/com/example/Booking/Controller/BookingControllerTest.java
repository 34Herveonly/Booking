//package com.example.Booking.Controller;
//
//import com.example.Booking.Model.Booking;
//import com.example.Booking.Service.BookingService;
//import com.example.Booking.dto.BookingRequest;
//import com.example.Booking.dto.BookingResponse;
//import com.example.Booking.dto.UserBookingStats;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BookingController.class)
//public class BookingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookingService bookingService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private BookingRequest sampleRequest;
//    private BookingResponse sampleResponse;
//
//    @BeforeEach
//    void setUp() {
//        // Setup sample booking request
//        sampleRequest = new BookingRequest();
//        sampleRequest.setUserId("user123");
//        sampleRequest.setPassengerName("John Doe");
//        sampleRequest.setPassengerEmail("john.doe@example.com");
//        sampleRequest.setPassengerPhone("+250781234567");
//        sampleRequest.setDepartureProvince("Kigali City");
//        sampleRequest.setDepartureDistrict("Gasabo");
//        sampleRequest.setDestinationProvince("Eastern Province");
//        sampleRequest.setDestinationDistrict("Rwamagana");
//        sampleRequest.setTravelDate(LocalDateTime.now().plusDays(1));
//        sampleRequest.setPrice(new BigDecimal("1500.00"));
//        sampleRequest.setNumberOfPassengers(2);
//        sampleRequest.setSeatNumbers("A1, A2");
//        sampleRequest.setVehicleInfo("Bus - Kigali Express");
//
//        // Setup sample booking response
//        sampleResponse = new BookingResponse();
//        sampleResponse.setId(1L);
//        sampleResponse.setBookingId("BKG-12345678");
//        sampleResponse.setUserId("user123");
//        sampleResponse.setPassengerName("John Doe");
//        sampleResponse.setPassengerEmail("john.doe@example.com");
//        sampleResponse.setPassengerPhone("+250781234567");
//        sampleResponse.setDepartureProvince("Kigali City");
//        sampleResponse.setDepartureDistrict("Gasabo");
//        sampleResponse.setDestinationProvince("Eastern Province");
//        sampleResponse.setDestinationDistrict("Rwamagana");
//        sampleResponse.setTravelDate(LocalDateTime.now().plusDays(1));
//        sampleResponse.setBookingDate(LocalDateTime.now());
//        sampleResponse.setPrice(new BigDecimal("1500.00"));
//        sampleResponse.setNumberOfPassengers(2);
//        sampleResponse.setStatus(Booking.BookingStatus.CONFIRMED);
//        sampleResponse.setSeatNumbers("A1, A2");
//        sampleResponse.setVehicleInfo("Bus - Kigali Express");
//    }
//
//    @Test
//    public void testCreateBooking_Success() throws Exception {
//        // Given
//        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(sampleResponse);
//
//        // When & Then
//        mockMvc.perform(post("/bookings/book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.bookingId").value("BKG-12345678"))
//                .andExpect(jsonPath("$.userId").value("user123"))
//                .andExpect(jsonPath("$.passengerName").value("John Doe"))
//                .andExpect(jsonPath("$.passengerEmail").value("john.doe@example.com"))
//                .andExpect(jsonPath("$.price").value(1500.00))
//                .andExpect(jsonPath("$.numberOfPassengers").value(2))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"));
//    }
//
//    @Test
//    public void testCreateBooking_ValidationError() throws Exception {
//        // Given - invalid request with missing userId
//        BookingRequest invalidRequest = new BookingRequest();
//        invalidRequest.setPassengerName("John Doe");
//
//        when(bookingService.createBooking(any(BookingRequest.class)))
//                .thenThrow(new IllegalArgumentException("User ID is required"));
//
//        // When & Then
//        mockMvc.perform(post("/bookings/book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("User ID is required")));
//    }
//
//    @Test
//    public void testGetUserBookings_Success() throws Exception {
//        // Given
//        List<BookingResponse> bookings = Arrays.asList(sampleResponse);
//        when(bookingService.getUserBookings("user123")).thenReturn(bookings);
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/bookings")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].bookingId").value("BKG-12345678"))
//                .andExpect(jsonPath("$[0].userId").value("user123"));
//    }
//
//    @Test
//    public void testGetUserBookings_EmptyList() throws Exception {
//        // Given
//        when(bookingService.getUserBookings("user123")).thenReturn(Arrays.asList());
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/bookings")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(0));
//    }
//
//    @Test
//    public void testGetBookingDetails_Success() throws Exception {
//        // Given
//        when(bookingService.getBookingDetails("BKG-12345678", "user123"))
//                .thenReturn(Optional.of(sampleResponse));
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/bookings/BKG-12345678")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookingId").value("BKG-12345678"))
//                .andExpect(jsonPath("$.userId").value("user123"));
//    }
//
//    @Test
//    public void testGetBookingDetails_NotFound() throws Exception {
//        // Given
//        when(bookingService.getBookingDetails("BKG-INVALID", "user123"))
//                .thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/bookings/BKG-INVALID")
//                        .param("userId", "user123"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void testGetUserBookingHistory_Success() throws Exception {
//        // Given
//        List<BookingResponse> history = Arrays.asList(sampleResponse);
//        when(bookingService.getUserBookingHistory("user123")).thenReturn(history);
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/history")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].bookingId").value("BKG-12345678"));
//    }
//
//    @Test
//    public void testCancelBooking_Success() throws Exception {
//        // Given
//        BookingResponse cancelledResponse = new BookingResponse();
//        cancelledResponse.setId(sampleResponse.getId());
//        cancelledResponse.setBookingId(sampleResponse.getBookingId());
//        cancelledResponse.setUserId(sampleResponse.getUserId());
//        cancelledResponse.setStatus(Booking.BookingStatus.CANCELLED);
//
//        when(bookingService.cancelBooking("BKG-12345678", "user123"))
//                .thenReturn(cancelledResponse);
//
//        // When & Then
//        mockMvc.perform(put("/bookings/BKG-12345678/cancel")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookingId").value("BKG-12345678"))
//                .andExpect(jsonPath("$.status").value("CANCELLED"));
//    }
//
//    @Test
//    public void testCancelBooking_AlreadyCancelled() throws Exception {
//        // Given
//        when(bookingService.cancelBooking("BKG-12345678", "user123"))
//                .thenThrow(new RuntimeException("Booking is already cancelled"));
//
//        // When & Then
//        mockMvc.perform(put("/bookings/BKG-12345678/cancel")
//                        .param("userId", "user123"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Booking is already cancelled"));
//    }
//
//    @Test
//    public void testGetUserBookingStats_Success() throws Exception {
//        // Given
//        UserBookingStats stats = new UserBookingStats(
//                10L, 7L, 1L, 2L,
//                new BigDecimal("15000.00"),
//                "Rwamagana"
//        );
//        when(bookingService.getUserBookingStats("user123")).thenReturn(stats);
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/stats")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalBookings").value(10))
//                .andExpect(jsonPath("$.confirmedBookings").value(7))
//                .andExpect(jsonPath("$.cancelledBookings").value(1))
//                .andExpect(jsonPath("$.completedBookings").value(2))
//                .andExpect(jsonPath("$.totalSpent").value(15000.00))
//                .andExpect(jsonPath("$.mostVisitedDestination").value("Rwamagana"));
//    }
//
//    @Test
//    public void testDownloadTicketPdf_Success() throws Exception {
//        // Given
//        byte[] pdfContent = "Mock PDF Content".getBytes();
//        when(bookingService.generateTicketPdf("BKG-12345678", "user123"))
//                .thenReturn(pdfContent);
//
//        // When & Then
//        mockMvc.perform(post("/bookings/BKG-12345678/download-pdf")
//                        .param("userId", "user123"))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", "application/pdf"))
//                .andExpect(header().string("Content-Disposition",
//                        "form-data; name=\"attachment\"; filename=\"ticket-BKG-12345678.pdf\""))
//                .andExpect(content().bytes(pdfContent));
//    }
//
//    @Test
//    public void testDownloadTicketPdf_BookingNotFound() throws Exception {
//        // Given
//        when(bookingService.generateTicketPdf("BKG-INVALID", "user123"))
//                .thenThrow(new RuntimeException("Booking not found or does not belong to user"));
//
//        // When & Then
//        mockMvc.perform(post("/bookings/BKG-INVALID/download-pdf")
//                        .param("userId", "user123"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Booking not found or does not belong to user"));
//    }
//
//    @Test
//    public void testApiTest() throws Exception {
//        mockMvc.perform(get("/bookings/test"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Booking API working smoothly"));
//    }
//
//    @Test
//    public void testCreateBooking_ServerError() throws Exception {
//        // Given
//        when(bookingService.createBooking(any(BookingRequest.class)))
//                .thenThrow(new RuntimeException("Database connection failed"));
//
//        // When & Then
//        mockMvc.perform(post("/bookings/book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleRequest)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error creating booking")));
//    }
//
//    @Test
//    public void testGetUserBookings_ServerError() throws Exception {
//        // Given
//        when(bookingService.getUserBookings("user123"))
//                .thenThrow(new RuntimeException("Database error"));
//
//        // When & Then
//        mockMvc.perform(get("/bookings/user/bookings")
//                        .param("userId", "user123"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error retrieving bookings")));
//    }
//}
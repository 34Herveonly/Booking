//package com.example.Booking;
//
//import com.example.Booking.dto.BookingRequest;
//import com.example.Booking.dto.BookingResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebMvc
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.datasource.driver-class-name=org.h2.Driver"
//})
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional
//public class BookingIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private static String createdBookingId = null;
//
//    @Test
//    @Order(1)
//    public void testCompleteBookingFlow() throws Exception {
//        String userId = "integration-user-001";
//
//        // 1. Test API is working
//        mockMvc.perform(get("/bookings/test"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Booking API working smoothly"));
//
//        // 2. Create a booking
//        BookingRequest request = createSampleBookingRequest(userId);
//
//        String response = mockMvc.perform(post("/bookings/book")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.userId").value(userId))
//                .andExpect(jsonPath("$.passengerName").value("Integration Test User"))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"))
//                .andExpect(jsonPath("$.bookingId").exists())
//                .andReturn().getResponse().getContentAsString();
//
//        BookingResponse bookingResponse = objectMapper.readValue(response, BookingResponse.class);
//        createdBookingId = bookingResponse.getBookingId();
//
//        // 3. Get user bookings
//        mockMvc.perform(get("/bookings/user/bookings")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].bookingId").value(createdBookingId))
//                .andExpect(jsonPath("$[0].userId").value(userId));
//
//        // 4. Get specific booking details
//        mockMvc.perform(get("/bookings/user/bookings/" + createdBookingId)
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookingId").value(createdBookingId))
//                .andExpect(jsonPath("$.userId").value(userId))
//                .andExpect(jsonPath("$.status").value("CONFIRMED"));
//
//        // 5. Get booking history
//        mockMvc.perform(get("/bookings/user/history")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].bookingId").value(createdBookingId));
//
//        // 6. Get user stats
//        mockMvc.perform(get("/bookings/user/stats")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalBookings").value(1))
//                .andExpect(jsonPath("$.confirmedBookings").value(1))
//                .andExpect(jsonPath("$.cancelledBookings").value(0))
//                .andExpect(jsonPath("$.totalSpent").value(2500.00));
//
//        // 7. Cancel the booking
//        mockMvc.perform(put("/bookings/" + createdBookingId + "/cancel")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookingId").value(createdBookingId))
//                .andExpect(jsonPath("$.status").value("CANCELLED"));
//
//        // 8. Verify stats after cancellation
//        mockMvc.perform(get("/bookings/user/stats")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalBookings").value(1))
//                .andExpect(jsonPath("$.confirmedBookings").value(0))
//                .andExpect(jsonPath("$.cancelledBookings").value(1));
//    }
//
//    @Test
//    @Order(2)
//    public void testValidationErrors() throws Exception {
//        // Test with invalid booking request (missing required fields)
//        BookingRequest invalidRequest = new BookingRequest();
//        invalidRequest.setPassengerName("Test User");
//
//        mockMvc.perform(post("/bookings/book")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("User ID is required")));
//    }
//
//    @Test
//    @Order(3)
//    public void testBookingNotFound() throws Exception {
//        String userId = "test-user";
//        String invalidBookingId = "BKG-INVALID123";
//
//        // Test getting non-existent booking
//        mockMvc.perform(get("/bookings/user/bookings/" + invalidBookingId)
//                .param("userId", userId))
//                .andExpect(status().isNotFound());
//
//        // Test canceling non-existent booking
//        mockMvc.perform(put("/bookings/" + invalidBookingId + "/cancel")
//                .param("userId", userId))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Booking not found or does not belong to user"));
//    }
//
//    @Test
//    @Order(4)
//    public void testMultipleBookingsForUser() throws Exception {
//        String userId = "multi-booking-user";
//
//        // Create multiple bookings
//        for (int i = 1; i <= 3; i++) {
//            BookingRequest request = createSampleBookingRequest(userId);
//            request.setPassengerName("Test User " + i);
//            request.setPrice(new BigDecimal(1000 + (i * 500))); // Different prices
//
//            mockMvc.perform(post("/bookings/book")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(request)))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.userId").value(userId));
//        }
//
//        // Verify user has 3 bookings
//        mockMvc.perform(get("/bookings/user/bookings")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$.length()").value(3));
//
//        // Check user stats
//        mockMvc.perform(get("/bookings/user/stats")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.totalBookings").value(3))
//                .andExpect(jsonPath("$.confirmedBookings").value(3))
//                .andExpect(jsonPath("$.totalSpent").value(6000.00)); // 1500 + 2000 + 2500
//    }
//
//    @Test
//    @Order(5)
//    public void testPdfGeneration() throws Exception {
//        String userId = "pdf-test-user";
//
//        // Create a booking first
//        BookingRequest request = createSampleBookingRequest(userId);
//
//        String response = mockMvc.perform(post("/bookings/book")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//
//        BookingResponse bookingResponse = objectMapper.readValue(response, BookingResponse.class);
//        String bookingId = bookingResponse.getBookingId();
//
//        // Test PDF generation
//        mockMvc.perform(post("/bookings/" + bookingId + "/download-pdf")
//                .param("userId", userId))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Type", "application/pdf"))
//                .andExpect(header().exists("Content-Disposition"));
//    }
//
//    @Test
//    @Order(6)
//    public void testCancelAlreadyCancelledBooking() throws Exception {
//        String userId = "cancel-test-user";
//
//        // Create and cancel a booking
//        BookingRequest request = createSampleBookingRequest(userId);
//
//        String response = mockMvc.perform(post("/bookings/book")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andReturn().getResponse().getContentAsString();
//
//        BookingResponse bookingResponse = objectMapper.readValue(response, BookingResponse.class);
//        String bookingId = bookingResponse.getBookingId();
//
//        // Cancel the booking
//        mockMvc.perform(put("/bookings/" + bookingId + "/cancel")
//                .param("userId", userId))
//                .andExpect(status().isOk());
//
//        // Try to cancel again
//        mockMvc.perform(put("/bookings/" + bookingId + "/cancel")
//                .param("userId", userId))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Booking is already cancelled"));
//    }
//
//    private BookingRequest createSampleBookingRequest(String userId) {
//        BookingRequest request = new BookingRequest();
//        request.setUserId(userId);
//        request.setPassengerName("Integration Test User");
//        request.setPassengerEmail("integration@test.com");
//        request.setPassengerPhone("+250781234567");
//        request.setDepartureProvince("Kigali City");
//        request.setDepartureDistrict("Gasabo");
//        request.setDestinationProvince("Eastern Province");
//        request.setDestinationDistrict("Rwamagana");
//        request.setTravelDate(LocalDateTime.now().plusDays(1));
//        request.setPrice(new BigDecimal("2500.00"));
//        request.setNumberOfPassengers(2);
//        request.setSeatNumbers("A1, A2");
//        request.setVehicleInfo("Bus - Test Express");
//        return request;
//    }
//}
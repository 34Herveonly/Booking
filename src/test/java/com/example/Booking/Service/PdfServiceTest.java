package com.example.Booking.Service;

import com.example.Booking.Model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PdfServiceTest {

    @InjectMocks
    private PdfService pdfService;

    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        sampleBooking = new Booking();
        sampleBooking.setId(1L);
        sampleBooking.setBookingId("BKG-TEST123");
        sampleBooking.setUserId("testuser");
        sampleBooking.setPassengerName("John Doe");
        sampleBooking.setPassengerEmail("john.doe@example.com");
        sampleBooking.setPassengerPhone("+250781234567");
        sampleBooking.setDepartureProvince("Kigali City");
        sampleBooking.setDepartureDistrict("Gasabo");
        sampleBooking.setDestinationProvince("Eastern Province");
        sampleBooking.setDestinationDistrict("Rwamagana");
        sampleBooking.setTravelDate(LocalDateTime.of(2024, 11, 20, 10, 0));
        sampleBooking.setBookingDate(LocalDateTime.of(2024, 11, 14, 15, 30));
        sampleBooking.setPrice(new BigDecimal("2500.00"));
        sampleBooking.setNumberOfPassengers(2);
        sampleBooking.setStatus(Booking.BookingStatus.CONFIRMED);
        sampleBooking.setSeatNumbers("A1, A2");
        sampleBooking.setVehicleInfo("Bus - Kigali Express");
    }

    @Test
    void testGenerateTicketPdf_Success() {
        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify PDF header (PDF files start with "%PDF")
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_WithMinimalBookingData() {
        // Given - booking with minimal required data
        Booking minimalBooking = new Booking();
        minimalBooking.setBookingId("BKG-MINIMAL");
        minimalBooking.setPassengerName("Jane Smith");
        minimalBooking.setPassengerEmail("jane@example.com");
        minimalBooking.setPassengerPhone("+250781111111");
        minimalBooking.setDepartureProvince("Kigali");
        minimalBooking.setDepartureDistrict("Nyarugenge");
        minimalBooking.setDestinationProvince("Southern");
        minimalBooking.setDestinationDistrict("Huye");
        minimalBooking.setTravelDate(LocalDateTime.now().plusDays(1));
        minimalBooking.setBookingDate(LocalDateTime.now());
        minimalBooking.setPrice(new BigDecimal("1000.00"));
        minimalBooking.setNumberOfPassengers(1);
        minimalBooking.setStatus(Booking.BookingStatus.CONFIRMED);
        // No seat numbers or vehicle info

        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(minimalBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_WithNullOptionalFields() {
        // Given - booking with null optional fields
        sampleBooking.setSeatNumbers(null);
        sampleBooking.setVehicleInfo(null);

        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_WithEmptyOptionalFields() {
        // Given - booking with empty optional fields
        sampleBooking.setSeatNumbers("");
        sampleBooking.setVehicleInfo("   "); // whitespace only

        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_ExceptionHandling() {
        // Given - booking with null required field that might cause issues
        Booking invalidBooking = new Booking();
        // Leave most fields null to potentially trigger exceptions
        invalidBooking.setBookingId("BKG-INVALID");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> pdfService.generateTicketPdf(invalidBooking));
        
        assertTrue(exception.getMessage().contains("Error generating PDF"));
    }

    @Test
    void testGenerateTicketPdf_LongText() {
        // Given - booking with long text values
        sampleBooking.setPassengerName("John Doe with a very very very long name that might cause formatting issues");
        sampleBooking.setVehicleInfo("Bus - Kigali Express Premium Service with luxury seats and air conditioning system - Route 001");

        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_SpecialCharacters() {
        // Given - booking with special characters
        sampleBooking.setPassengerName("Jean-Claude Müller");
        sampleBooking.setDepartureProvince("Kigali Çity");
        sampleBooking.setDestinationDistrict("Rwamagañá");

        // When
        byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

        // Then
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader);
    }

    @Test
    void testGenerateTicketPdf_DifferentStatuses() {
        // Test with different booking statuses
        Booking.BookingStatus[] statuses = {
            Booking.BookingStatus.CONFIRMED,
            Booking.BookingStatus.COMPLETED
        };

        for (Booking.BookingStatus status : statuses) {
            // Given
            sampleBooking.setStatus(status);

            // When
            byte[] pdfBytes = pdfService.generateTicketPdf(sampleBooking);

            // Then
            assertNotNull(pdfBytes, "PDF generation failed for status: " + status);
            assertTrue(pdfBytes.length > 0, "Empty PDF for status: " + status);
            
            // Verify it's a valid PDF
            String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
            assertEquals("%PDF", pdfHeader, "Invalid PDF header for status: " + status);
        }
    }
}
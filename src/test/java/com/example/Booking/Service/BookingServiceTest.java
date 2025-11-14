package com.example.Booking.Service;

import com.example.Booking.Model.Booking;
import com.example.Booking.Repository.BookingRepository;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.dto.UserBookingStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private BookingService bookingService;

    private BookingRequest validRequest;
    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        validRequest = new BookingRequest();
        validRequest.setUserId("user123");
        validRequest.setPassengerName("John Doe");
        validRequest.setPassengerEmail("john@example.com");
        validRequest.setPassengerPhone("+250781234567");
        validRequest.setDepartureProvince("Kigali");
        validRequest.setDepartureDistrict("Gasabo");
        validRequest.setDestinationProvince("Eastern");
        validRequest.setDestinationDistrict("Rwamagana");
        validRequest.setTravelDate(LocalDateTime.now().plusDays(1));
        validRequest.setPrice(new BigDecimal("1500.00"));
        validRequest.setNumberOfPassengers(2);

        sampleBooking = new Booking();
        sampleBooking.setId(1L);
        sampleBooking.setBookingId("BKG-12345678");
        sampleBooking.setUserId("user123");
        sampleBooking.setPassengerName("John Doe");
        sampleBooking.setPassengerEmail("john@example.com");
        sampleBooking.setPassengerPhone("+250781234567");
        sampleBooking.setDepartureProvince("Kigali");
        sampleBooking.setDepartureDistrict("Gasabo");
        sampleBooking.setDestinationProvince("Eastern");
        sampleBooking.setDestinationDistrict("Rwamagana");
        sampleBooking.setTravelDate(LocalDateTime.now().plusDays(1));
        sampleBooking.setPrice(new BigDecimal("1500.00"));
        sampleBooking.setNumberOfPassengers(2);
        sampleBooking.setStatus(Booking.BookingStatus.CONFIRMED);
        sampleBooking.setBookingDate(LocalDateTime.now());
    }

    @Test
    void testCreateBooking_Success() {
        // Given
        when(bookingRepository.save(any(Booking.class))).thenReturn(sampleBooking);

        // When
        BookingResponse result = bookingService.createBooking(validRequest);

        // Then
        assertNotNull(result);
        assertEquals("BKG-12345678", result.getBookingId());
        assertEquals("user123", result.getUserId());
        assertEquals("John Doe", result.getPassengerName());
        assertEquals(Booking.BookingStatus.CONFIRMED, result.getStatus());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_MissingUserId() {
        // Given
        validRequest.setUserId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(validRequest));

        assertEquals("User ID is required", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCreateBooking_MissingPassengerName() {
        // Given
        validRequest.setPassengerName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(validRequest));

        assertEquals("Passenger name is required", exception.getMessage());
    }

    @Test
    void testCreateBooking_InvalidTravelDate() {
        // Given
        validRequest.setTravelDate(LocalDateTime.now().minusDays(1)); // Past date

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(validRequest));

        assertEquals("Travel date cannot be in the past", exception.getMessage());
    }

    @Test
    void testCreateBooking_InvalidPrice() {
        // Given
        validRequest.setPrice(BigDecimal.ZERO);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(validRequest));

        assertEquals("Price must be greater than 0", exception.getMessage());
    }

    @Test
    void testCreateBooking_InvalidNumberOfPassengers() {
        // Given
        validRequest.setNumberOfPassengers(0);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(validRequest));

        assertEquals("Number of passengers must be at least 1", exception.getMessage());
    }

    @Test
    void testGetUserBookings_Success() {
        // Given
        List<Booking> bookings = Arrays.asList(sampleBooking);
        when(bookingRepository.findByUserIdOrderByBookingDateDesc("user123")).thenReturn(bookings);

        // When
        List<BookingResponse> result = bookingService.getUserBookings("user123");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BKG-12345678", result.get(0).getBookingId());

        verify(bookingRepository).findByUserIdOrderByBookingDateDesc("user123");
    }

    @Test
    void testGetBookingDetails_Success() {
        // Given
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));

        // When
        Optional<BookingResponse> result = bookingService.getBookingDetails("BKG-12345678", "user123");

        // Then
        assertTrue(result.isPresent());
        assertEquals("BKG-12345678", result.get().getBookingId());
        assertEquals("user123", result.get().getUserId());
    }

    @Test
    void testGetBookingDetails_NotFound() {
        // Given
        when(bookingRepository.findByBookingIdAndUserId("BKG-INVALID", "user123"))
                .thenReturn(Optional.empty());

        // When
        Optional<BookingResponse> result = bookingService.getBookingDetails("BKG-INVALID", "user123");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testCancelBooking_Success() {
        // Given
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(sampleBooking);

        // When
        BookingResponse result = bookingService.cancelBooking("BKG-12345678", "user123");

        // Then
        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        // Given
        when(bookingRepository.findByBookingIdAndUserId("BKG-INVALID", "user123"))
                .thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking("BKG-INVALID", "user123"));

        assertEquals("Booking not found or does not belong to user", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBooking_AlreadyCancelled() {
        // Given
        sampleBooking.setStatus(Booking.BookingStatus.CANCELLED);
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking("BKG-12345678", "user123"));

        assertEquals("Booking is already cancelled", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBooking_AlreadyCompleted() {
        // Given
        sampleBooking.setStatus(Booking.BookingStatus.COMPLETED);
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.cancelBooking("BKG-12345678", "user123"));

        assertEquals("Cannot cancel completed booking", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testGetUserBookingStats_Success() {
        // Given
        when(bookingRepository.countByUserId("user123")).thenReturn(10L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CONFIRMED)).thenReturn(7L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CANCELLED)).thenReturn(1L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.COMPLETED)).thenReturn(2L);
        when(bookingRepository.sumPriceByUserIdExcludingCancelled("user123")).thenReturn(new BigDecimal("15000.00"));
        
        List<Object[]> destinationData = new ArrayList<>();
        destinationData.add(new Object[] { "Rwamagana", 5L });
        when(bookingRepository.findMostVisitedDestinationByUserId("user123"))
                .thenReturn(destinationData);

        // When
        UserBookingStats result = bookingService.getUserBookingStats("user123");

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getTotalBookings());
        assertEquals(7L, result.getConfirmedBookings());
        assertEquals(1L, result.getCancelledBookings());
        assertEquals(2L, result.getCompletedBookings());
        assertEquals(new BigDecimal("15000.00"), result.getTotalSpent());
        assertEquals("Rwamagana", result.getMostVisitedDestination());
    }

    @Test
    void testGetUserBookingStats_NoSpending() {
        // Given
        when(bookingRepository.countByUserId("user123")).thenReturn(0L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CONFIRMED)).thenReturn(0L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CANCELLED)).thenReturn(0L);
        when(bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.COMPLETED)).thenReturn(0L);
        when(bookingRepository.sumPriceByUserIdExcludingCancelled("user123")).thenReturn(null);
        when(bookingRepository.findMostVisitedDestinationByUserId("user123")).thenReturn(Arrays.asList());

        // When
        UserBookingStats result = bookingService.getUserBookingStats("user123");

        // Then
        assertNotNull(result);
        assertEquals(0L, result.getTotalBookings());
        assertEquals(BigDecimal.ZERO, result.getTotalSpent());
        assertEquals("N/A", result.getMostVisitedDestination());
    }

    @Test
    void testGenerateTicketPdf_Success() {
        // Given
        byte[] pdfContent = "Mock PDF Content".getBytes();
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));
        when(pdfService.generateTicketPdf(sampleBooking)).thenReturn(pdfContent);

        // When
        byte[] result = bookingService.generateTicketPdf("BKG-12345678", "user123");

        // Then
        assertNotNull(result);
        assertArrayEquals(pdfContent, result);
        verify(pdfService).generateTicketPdf(sampleBooking);
    }

    @Test
    void testGenerateTicketPdf_BookingNotFound() {
        // Given
        when(bookingRepository.findByBookingIdAndUserId("BKG-INVALID", "user123"))
                .thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.generateTicketPdf("BKG-INVALID", "user123"));

        assertEquals("Booking not found or does not belong to user", exception.getMessage());
        verify(pdfService, never()).generateTicketPdf(any());
    }

    @Test
    void testGenerateTicketPdf_CancelledBooking() {
        // Given
        sampleBooking.setStatus(Booking.BookingStatus.CANCELLED);
        when(bookingRepository.findByBookingIdAndUserId("BKG-12345678", "user123"))
                .thenReturn(Optional.of(sampleBooking));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.generateTicketPdf("BKG-12345678", "user123"));

        assertEquals("Cannot generate PDF for cancelled booking", exception.getMessage());
        verify(pdfService, never()).generateTicketPdf(any());
    }
}
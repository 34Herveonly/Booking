//package com.example.Booking.Repository;
//
//import com.example.Booking.Model.Booking;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.TestPropertySource;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@TestPropertySource(properties = {
//    "spring.datasource.url=jdbc:h2:mem:testdb",
//    "spring.jpa.hibernate.ddl-auto=create-drop"
//})
//public class BookingRepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    private Booking sampleBooking1;
//    private Booking sampleBooking2;
//    private Booking sampleBooking3;
//
//    @BeforeEach
//    void setUp() {
//        // Create sample bookings
//        sampleBooking1 = createBooking("user123", "BKG-001", "Kigali", "Gasabo",
//                "Eastern", "Rwamagana", new BigDecimal("1500.00"), Booking.BookingStatus.CONFIRMED);
//
//        sampleBooking2 = createBooking("user123", "BKG-002", "Kigali", "Nyarugenge",
//                "Southern", "Huye", new BigDecimal("2000.00"), Booking.BookingStatus.CANCELLED);
//
//        sampleBooking3 = createBooking("user456", "BKG-003", "Northern", "Musanze",
//                "Eastern", "Rwamagana", new BigDecimal("1800.00"), Booking.BookingStatus.COMPLETED);
//
//        // Persist the bookings
//        entityManager.persist(sampleBooking1);
//        entityManager.persist(sampleBooking2);
//        entityManager.persist(sampleBooking3);
//        entityManager.flush();
//    }
//
//    @Test
//    void testFindByUserIdOrderByBookingDateDesc() {
//        // When
//        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc("user123");
//
//        // Then
//        assertEquals(2, bookings.size());
//        assertTrue(bookings.stream().allMatch(b -> "user123".equals(b.getUserId())));
//
//        // Verify order (most recent first)
//        assertTrue(bookings.get(0).getBookingDate().isAfter(bookings.get(1).getBookingDate()) ||
//                  bookings.get(0).getBookingDate().isEqual(bookings.get(1).getBookingDate()));
//    }
//
//    @Test
//    void testFindByBookingId() {
//        // When
//        Optional<Booking> result = bookingRepository.findByBookingId("BKG-001");
//
//        // Then
//        assertTrue(result.isPresent());
//        assertEquals("BKG-001", result.get().getBookingId());
//        assertEquals("user123", result.get().getUserId());
//    }
//
//    @Test
//    void testFindByBookingId_NotFound() {
//        // When
//        Optional<Booking> result = bookingRepository.findByBookingId("BKG-INVALID");
//
//        // Then
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void testFindByBookingIdAndUserId() {
//        // When
//        Optional<Booking> result = bookingRepository.findByBookingIdAndUserId("BKG-001", "user123");
//
//        // Then
//        assertTrue(result.isPresent());
//        assertEquals("BKG-001", result.get().getBookingId());
//        assertEquals("user123", result.get().getUserId());
//    }
//
//    @Test
//    void testFindByBookingIdAndUserId_WrongUser() {
//        // When
//        Optional<Booking> result = bookingRepository.findByBookingIdAndUserId("BKG-001", "user456");
//
//        // Then
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void testFindByUserIdAndStatusOrderByBookingDateDesc() {
//        // When
//        List<Booking> confirmedBookings = bookingRepository.findByUserIdAndStatusOrderByBookingDateDesc(
//                "user123", Booking.BookingStatus.CONFIRMED);
//
//        // Then
//        assertEquals(1, confirmedBookings.size());
//        assertEquals("BKG-001", confirmedBookings.get(0).getBookingId());
//        assertEquals(Booking.BookingStatus.CONFIRMED, confirmedBookings.get(0).getStatus());
//    }
//
//    @Test
//    void testCountByUserId() {
//        // When
//        Long count = bookingRepository.countByUserId("user123");
//
//        // Then
//        assertEquals(2L, count);
//    }
//
//    @Test
//    void testCountByUserIdAndStatus() {
//        // When
//        Long confirmedCount = bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CONFIRMED);
//        Long cancelledCount = bookingRepository.countByUserIdAndStatus("user123", Booking.BookingStatus.CANCELLED);
//
//        // Then
//        assertEquals(1L, confirmedCount);
//        assertEquals(1L, cancelledCount);
//    }
//
//    @Test
//    void testSumPriceByUserIdExcludingCancelled() {
//        // When
//        BigDecimal totalSpent = bookingRepository.sumPriceByUserIdExcludingCancelled("user123");
//
//        // Then
//        // Should only include confirmed bookings (1500.00), excluding cancelled ones
//        assertEquals(new BigDecimal("1500.00"), totalSpent);
//    }
//
//    @Test
//    void testSumPriceByUserIdExcludingCancelled_NoBookings() {
//        // When
//        BigDecimal totalSpent = bookingRepository.sumPriceByUserIdExcludingCancelled("nonexistent");
//
//        // Then
//        assertNull(totalSpent);
//    }
//
//    @Test
//    void testFindMostVisitedDestinationByUserId() {
//        // Create additional booking to Rwamagana for user123 to make it the most visited
//        Booking additionalBooking = createBooking("user123", "BKG-004", "Kigali", "Kicukiro",
//                "Eastern", "Rwamagana", new BigDecimal("1200.00"), Booking.BookingStatus.CONFIRMED);
//        entityManager.persist(additionalBooking);
//        entityManager.flush();
//
//        // When
//        List<Object[]> results = bookingRepository.findMostVisitedDestinationByUserId("user123");
//
//        // Then
//        assertFalse(results.isEmpty());
//        Object[] mostVisited = results.get(0);
//        assertEquals("Rwamagana", mostVisited[0]); // destination
//        assertEquals(2L, mostVisited[1]); // count
//    }
//
//    @Test
//    void testFindMostVisitedDestinationByUserId_NoBookings() {
//        // When
//        List<Object[]> results = bookingRepository.findMostVisitedDestinationByUserId("nonexistent");
//
//        // Then
//        assertTrue(results.isEmpty());
//    }
//
//    @Test
//    void testSaveAndRetrieve() {
//        // Given
//        Booking newBooking = createBooking("newuser", "BKG-NEW", "Western", "Karongi",
//                "Northern", "Musanze", new BigDecimal("3000.00"), Booking.BookingStatus.CONFIRMED);
//
//        // When
//        Booking saved = bookingRepository.save(newBooking);
//        Optional<Booking> retrieved = bookingRepository.findByBookingId("BKG-NEW");
//
//        // Then
//        assertNotNull(saved.getId());
//        assertTrue(retrieved.isPresent());
//        assertEquals("BKG-NEW", retrieved.get().getBookingId());
//        assertEquals("newuser", retrieved.get().getUserId());
//        assertEquals(new BigDecimal("3000.00"), retrieved.get().getPrice());
//    }
//
//    @Test
//    void testUpdateBookingStatus() {
//        // Given
//        Optional<Booking> booking = bookingRepository.findByBookingId("BKG-001");
//        assertTrue(booking.isPresent());
//
//        // When
//        booking.get().setStatus(Booking.BookingStatus.COMPLETED);
//        Booking updated = bookingRepository.save(booking.get());
//
//        // Then
//        assertEquals(Booking.BookingStatus.COMPLETED, updated.getStatus());
//
//        // Verify persistence
//        Optional<Booking> retrieved = bookingRepository.findByBookingId("BKG-001");
//        assertTrue(retrieved.isPresent());
//        assertEquals(Booking.BookingStatus.COMPLETED, retrieved.get().getStatus());
//    }
//
//    private Booking createBooking(String userId, String bookingId, String depProvince, String depDistrict,
//                                String destProvince, String destDistrict, BigDecimal price,
//                                Booking.BookingStatus status) {
//        Booking booking = new Booking();
//        booking.setUserId(userId);
//        booking.setBookingId(bookingId);
//        booking.setPassengerName("Test Passenger");
//        booking.setPassengerEmail("test@example.com");
//        booking.setPassengerPhone("+250781234567");
//        booking.setDepartureProvince(depProvince);
//        booking.setDepartureDistrict(depDistrict);
//        booking.setDestinationProvince(destProvince);
//        booking.setDestinationDistrict(destDistrict);
//        booking.setTravelDate(LocalDateTime.now().plusDays(1));
//        booking.setBookingDate(LocalDateTime.now());
//        booking.setPrice(price);
//        booking.setNumberOfPassengers(2);
//        booking.setStatus(status);
//        return booking;
//    }
//}
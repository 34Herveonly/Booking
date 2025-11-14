package com.example.Booking.Service;

import com.example.Booking.Model.Booking;
import com.example.Booking.Repository.BookingRepository;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.dto.UserBookingStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PdfService pdfService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PdfService pdfService) {
        this.bookingRepository = bookingRepository;
        this.pdfService = pdfService;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Validate request
        validateBookingRequest(request);
        
        // Create booking entity
        Booking booking = new Booking();
        booking.setBookingId(generateBookingId());
        booking.setUserId(request.getUserId());
        booking.setPassengerName(request.getPassengerName());
        booking.setPassengerEmail(request.getPassengerEmail());
        booking.setPassengerPhone(request.getPassengerPhone());
        booking.setDepartureProvince(request.getDepartureProvince());
        booking.setDepartureDistrict(request.getDepartureDistrict());
        booking.setDestinationProvince(request.getDestinationProvince());
        booking.setDestinationDistrict(request.getDestinationDistrict());
        booking.setTravelDate(request.getTravelDate());
        booking.setPrice(request.getPrice());
        booking.setNumberOfPassengers(request.getNumberOfPassengers());
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setVehicleInfo(request.getVehicleInfo());
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(savedBooking);
    }

    public List<BookingResponse> getUserBookings(String userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
        return bookings.stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<BookingResponse> getBookingDetails(String bookingId, String userId) {
        Optional<Booking> booking = bookingRepository.findByBookingIdAndUserId(bookingId, userId);
        return booking.map(BookingResponse::fromEntity);
    }

    public List<BookingResponse> getUserBookingHistory(String userId) {
        List<Booking> bookings = bookingRepository.findByUserIdOrderByBookingDateDesc(userId);
        return bookings.stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse cancelBooking(String bookingId, String userId) {
        Optional<Booking> optionalBooking = bookingRepository.findByBookingIdAndUserId(bookingId, userId);
        
        if (optionalBooking.isEmpty()) {
            throw new RuntimeException("Booking not found or does not belong to user");
        }
        
        Booking booking = optionalBooking.get();
        
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed booking");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        
        return BookingResponse.fromEntity(updatedBooking);
    }

    public UserBookingStats getUserBookingStats(String userId) {
        Long totalBookings = bookingRepository.countByUserId(userId);
        Long confirmedBookings = bookingRepository.countByUserIdAndStatus(userId, Booking.BookingStatus.CONFIRMED);
        Long cancelledBookings = bookingRepository.countByUserIdAndStatus(userId, Booking.BookingStatus.CANCELLED);
        Long completedBookings = bookingRepository.countByUserIdAndStatus(userId, Booking.BookingStatus.COMPLETED);
        
        BigDecimal totalSpent = bookingRepository.sumPriceByUserIdExcludingCancelled(userId);
        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }
        
        String mostVisitedDestination = getMostVisitedDestination(userId);
        
        return new UserBookingStats(
            totalBookings,
            confirmedBookings,
            cancelledBookings,
            completedBookings,
            totalSpent,
            mostVisitedDestination
        );
    }

    public byte[] generateTicketPdf(String bookingId, String userId) {
        Optional<Booking> optionalBooking = bookingRepository.findByBookingIdAndUserId(bookingId, userId);
        
        if (optionalBooking.isEmpty()) {
            throw new RuntimeException("Booking not found or does not belong to user");
        }
        
        Booking booking = optionalBooking.get();
        
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Cannot generate PDF for cancelled booking");
        }
        
        return pdfService.generateTicketPdf(booking);
    }

    private void validateBookingRequest(BookingRequest request) {
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (request.getPassengerName() == null || request.getPassengerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
        if (request.getPassengerEmail() == null || request.getPassengerEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getTravelDate() == null) {
            throw new IllegalArgumentException("Travel date is required");
        }
        if (request.getTravelDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Travel date cannot be in the past");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (request.getNumberOfPassengers() == null || request.getNumberOfPassengers() < 1) {
            throw new IllegalArgumentException("Number of passengers must be at least 1");
        }
    }

    private String generateBookingId() {
        return "BKG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String getMostVisitedDestination(String userId) {
        List<Object[]> results = bookingRepository.findMostVisitedDestinationByUserId(userId);
        if (results.isEmpty()) {
            return "N/A";
        }
        return (String) results.get(0)[0];
    }
}
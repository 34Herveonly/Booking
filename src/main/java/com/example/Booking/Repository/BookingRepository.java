package com.example.Booking.Repository;

import com.example.Booking.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByUserIdOrderByBookingDateDesc(String userId);
    
    Optional<Booking> findByBookingId(String bookingId);
    
    Optional<Booking> findByBookingIdAndUserId(String bookingId, String userId);
    
    List<Booking> findByUserIdAndStatusOrderByBookingDateDesc(String userId, Booking.BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.userId = :userId")
    Long countByUserId(@Param("userId") String userId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.userId = :userId AND b.status = :status")
    Long countByUserIdAndStatus(@Param("userId") String userId, @Param("status") Booking.BookingStatus status);
    
    @Query("SELECT SUM(b.price) FROM Booking b WHERE b.userId = :userId AND b.status != 'CANCELLED'")
    java.math.BigDecimal sumPriceByUserIdExcludingCancelled(@Param("userId") String userId);
    
    @Query("SELECT b.destinationDistrict, COUNT(b) as count FROM Booking b WHERE b.userId = :userId AND b.status != 'CANCELLED' GROUP BY b.destinationDistrict ORDER BY count DESC")
    List<Object[]> findMostVisitedDestinationByUserId(@Param("userId") String userId);
}
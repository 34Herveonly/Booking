package com.example.Booking.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getPassengerPhone() {
        return passengerPhone;
    }

    public void setPassengerPhone(String passengerPhone) {
        this.passengerPhone = passengerPhone;
    }

    public String getDepartureProvince() {
        return departureProvince;
    }

    public void setDepartureProvince(String departureProvince) {
        this.departureProvince = departureProvince;
    }

    public String getDepartureDistrict() {
        return departureDistrict;
    }

    public void setDepartureDistrict(String departureDistrict) {
        this.departureDistrict = departureDistrict;
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public void setDestinationProvince(String destinationProvince) {
        this.destinationProvince = destinationProvince;
    }

    public String getDestinationDistrict() {
        return destinationDistrict;
    }

    public void setDestinationDistrict(String destinationDistrict) {
        this.destinationDistrict = destinationDistrict;
    }

    public LocalDateTime getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDateTime travelDate) {
        this.travelDate = travelDate;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(Integer numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(String seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }
    
    // Default constructor
    public Booking() {}
    
    // All args constructor
    public Booking(Long id, String bookingId, String userId, String passengerName, String passengerEmail, String passengerPhone,
                  String departureProvince, String departureDistrict, String destinationProvince, String destinationDistrict,
                  LocalDateTime travelDate, LocalDateTime bookingDate, BigDecimal price, Integer numberOfPassengers,
                  BookingStatus status, String seatNumbers, String vehicleInfo) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.departureProvince = departureProvince;
        this.departureDistrict = departureDistrict;
        this.destinationProvince = destinationProvince;
        this.destinationDistrict = destinationDistrict;
        this.travelDate = travelDate;
        this.bookingDate = bookingDate;
        this.price = price;
        this.numberOfPassengers = numberOfPassengers;
        this.status = status;
        this.seatNumbers = seatNumbers;
        this.vehicleInfo = vehicleInfo;
    }

    @Column(name = "booking_id", unique = true, nullable = false)
    private String bookingId;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "passenger_name", nullable = false)
    private String passengerName;
    
    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;
    
    @Column(name = "passenger_phone", nullable = false)
    private String passengerPhone;
    
    @Column(name = "departure_province", nullable = false)
    private String departureProvince;
    
    @Column(name = "departure_district", nullable = false)
    private String departureDistrict;
    
    @Column(name = "destination_province", nullable = false)
    private String destinationProvince;
    
    @Column(name = "destination_district", nullable = false)
    private String destinationDistrict;
    
    @Column(name = "travel_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime travelDate;
    
    @Column(name = "booking_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingDate;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(name = "number_of_passengers", nullable = false)
    private Integer numberOfPassengers;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    
    @Column(name = "seat_numbers")
    private String seatNumbers;
    
    @Column(name = "vehicle_info")
    private String vehicleInfo;
    
    @PrePersist
    protected void onCreate() {
        if (bookingDate == null) {
            bookingDate = LocalDateTime.now();
        }
        if (status == null) {
            status = BookingStatus.CONFIRMED;
        }
    }
    
    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }
}
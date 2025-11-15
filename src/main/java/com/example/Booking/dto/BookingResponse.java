package com.example.Booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.Booking.Model.Booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingResponse {
    
    private Long id;
    private String bookingId;
    private String userId;
    private String passengerName;
    private String passengerEmail;
    private String passengerPhone;
    private String departureProvince;
    private String departureDistrict;
    private String destinationProvince;
    private String destinationDistrict;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime travelDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime bookingDate;
    
    private BigDecimal price;
    private Integer numberOfPassengers;
    private Booking.BookingStatus status;
    private String seatNumbers;
    private String vehicleInfo;
    
    // Default constructor
    public BookingResponse() {}
    
    // All args constructor
    public BookingResponse(Long id, String bookingId, String userId, String passengerName, String passengerEmail, String passengerPhone,
                          String departureProvince, String departureDistrict, String destinationProvince, String destinationDistrict,
                          LocalDateTime travelDate, LocalDateTime bookingDate, BigDecimal price, Integer numberOfPassengers,
                          Booking.BookingStatus status, String seatNumbers, String vehicleInfo) {
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
    
    // Getters and Setters
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
    
    public Booking.BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(Booking.BookingStatus status) {
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
    
    public static BookingResponse fromEntity(Booking booking) {
        return new BookingResponse(
            booking.getId(),
            booking.getBookingId(),
            booking.getUserId(),
            booking.getPassengerName(),
            booking.getPassengerEmail(),
            booking.getPassengerPhone(),
            booking.getDepartureProvince(),
            booking.getDepartureDistrict(),
            booking.getDestinationProvince(),
            booking.getDestinationDistrict(),
            booking.getTravelDate(),
            booking.getBookingDate(),
            booking.getPrice(),
            booking.getNumberOfPassengers(),
            booking.getStatus(),
            booking.getSeatNumbers(),
            booking.getVehicleInfo()
        );
    }
}
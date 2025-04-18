package com.example.Booking.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "Ticket_information")
public class Routes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    private PaymentStatus paymentStatus;





    public enum PaymentStatus {
        NOT_PAID,
        PAID
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String departure_province;
    private String departure_district;
    private String destination_province;


    public String getDeparture_province() {
        return departure_province;
    }

    public void setDeparture_province(String departure_province) {
        this.departure_province = departure_province;
    }

    public String getDeparture_district() {
        return departure_district;
    }

    public void setDeparture_district(String departure_district) {
        this.departure_district = departure_district;
    }

    public String getDestination_province() {
        return destination_province;
    }

    public void setDestination_province(String destination_province) {
        this.destination_province = destination_province;
    }

    public String getDestination_district() {
        return destination_district;
    }

    public void setDestination_district(String destination_district) {
        this.destination_district = destination_district;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    private String destination_district;
    private BigDecimal price;
}

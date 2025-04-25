package com.example.Booking.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "user_selected_routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public String getDepartureProvince() {
        return departureProvince;
    }

    public String getDepartureDistrict() {
        return departureDistrict;
    }

    public String getDestinationProvince() {
        return destinationProvince;
    }

    public String getDestinationDistrict() {
        return destinationDistrict;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Column(name = "departure_province", nullable = false)
    private String departureProvince;  // Changed to camelCase

    @Column(name = "departure_district", nullable = false)
    private String departureDistrict;  // Changed to camelCase

    @Column(name = "destination_province", nullable = false)
    private String destinationProvince;  // Changed to camelCase

    @Column(name = "destination_district", nullable = false)
    private String destinationDistrict;  // Changed to camelCase

    @Column(nullable = false)
    private BigDecimal price;
}
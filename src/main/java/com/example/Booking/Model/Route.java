package com.example.Booking.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Table(name = "routes")
@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureProvince;
    private String departureDistrict;
    private String destinationProvince;
    private String destinationDistrict;
    private BigDecimal price;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}
package com.example.Booking.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "user_selected_routes")
public class Routes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureProvince;
    private String departureDistrict;
    private String destinationProvince;
    private String destinationDistrict;
    private BigDecimal price;

    // OneToMany relationship with Ticket (optional)
    @OneToMany(mappedBy = "route")
    private List<Ticket> tickets;
}
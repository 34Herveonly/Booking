package com.example.Booking.Model;

import com.example.Booking.Repository.RoutesRepository;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String clientName;
    private LocalDateTime ticketDate;
    private BigDecimal paidAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Column(name = "route_id")
    private Long routeId;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentInformation paymentInformation;

    public enum PaymentStatus {
        NOT_PAID, PAID
    }
}
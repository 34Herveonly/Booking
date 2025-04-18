package com.example.Booking.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payment_information")
public class PaymentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payeeName;
    private LocalDateTime paymentDate;
    private boolean status;
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
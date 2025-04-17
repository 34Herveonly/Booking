package com.example.Booking.Model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_Information")
public class paymentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @Column(name = "payee_name")
    private String payee_name;
    @Column(name = "payment_date")
    private LocalDateTime payment_date;
    @Column(name = "payment_status")
    private boolean Status;
    @Column(name = "amount")
    private BigDecimal amount;


}

package com.example.Booking.Model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Ticket_information")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID ticket_id;

    public UUID getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(UUID ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getTicket_date() {
        return ticket_date;
    }

    public void setTicket_date(String ticket_date) {
        this.ticket_date = ticket_date;
    }

    public BigDecimal getPaid_Amt() {
        return paid_Amt;
    }

    public void setPaid_Amt(BigDecimal paid_Amt) {
        this.paid_Amt = paid_Amt;
    }

    public PaymentStatus getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(PaymentStatus payment_status) {
        this.payment_status = payment_status;
    }

    private String client_name;
    private String ticket_date;
    private BigDecimal paid_Amt;
    private PaymentStatus payment_status;

    public enum PaymentStatus {
        NOT_PAID,
        PAID
    }


}

package com.example.Booking.Service;

import com.example.Booking.Model.Ticket;
import com.example.Booking.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public Ticket getTicketById(UUID ticket_id){
        return ticketRepository.findById(ticket_id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Ticket MarkAsPaid(UUID ticket_id){
        Ticket ticket = getTicketById(ticket_id);
        ticket.setPaymentStatus(Ticket.PaymentStatus.PAID);
        return ticketRepository.save(ticket);
    }

}

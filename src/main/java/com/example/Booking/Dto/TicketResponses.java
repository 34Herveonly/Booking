package com.example.Booking.Dto;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Ticket_Responses")
public class TicketResponses {
    private String message;
    private String status;
    private String sessionId;
    private String sessionUrl;
}


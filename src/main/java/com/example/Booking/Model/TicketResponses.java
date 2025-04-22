package com.example.Booking.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponses {
    private String message;
    private String status;
    private String sessionId;
    private String sessionUrl;
}

package com.example.Booking.Service;

import com.example.Booking.Dto.PaymentRequest;
import com.example.Booking.Dto.PaymentResponses;
import com.example.Booking.Repository.paymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private paymentInfoRepository repository;

    public PaymentResponses ticketPayment(PaymentRequest paymentRequest) {

    }

}

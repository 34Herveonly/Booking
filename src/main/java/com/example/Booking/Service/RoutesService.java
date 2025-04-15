package com.example.Booking.Service;

import com.example.Booking.Model.Routes;
import com.example.Booking.Repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class RoutesService {

    @Autowired
    private RoutesRepository RoutesRepository;


    public Routes saveUserSelectedRoutes(Routes routes) {
        return RoutesRepository.save(routes);
    }
}

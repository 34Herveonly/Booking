package com.example.Booking.Service;

import com.example.Booking.Model.Route;
import com.example.Booking.Repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoutesService {

    private final RoutesRepository routesRepository;

    @Autowired
    public RoutesService(RoutesRepository routesRepository) {
        this.routesRepository = routesRepository;
    }

    @Transactional
    public Route saveUserSelectedRoutes(Route route) {
        if (route.getDepartureProvince() == null || route.getDestinationProvince() == null) {
            throw new IllegalArgumentException("Departure/destination province cannot be null");
        }
        else if (route.getDepartureDistrict()==null || route.getDestinationDistrict() == null) {
            throw new IllegalArgumentException("Departure/destination district cannot be null");
        }
        return routesRepository.save(route);
    }
}
package com.example.Booking.Repository;

import com.example.Booking.Model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutesRepository extends JpaRepository<Route, Long> {
    // Add custom query methods if needed
}
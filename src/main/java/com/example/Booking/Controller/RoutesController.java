package com.example.Booking.Controller;

import com.example.Booking.Model.Route;
import com.example.Booking.Service.RoutesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Routes")
public class RoutesController {

    private final RoutesService routesService;

    public RoutesController(RoutesService routesService) {
        this.routesService = routesService;
    }

    @PostMapping("/Save")
    public ResponseEntity<?> saveSelectedRoutes(@RequestBody Route route) {
        try {
            Route savedRoute = routesService.saveUserSelectedRoutes(route);
            return ResponseEntity.ok(savedRoute);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving route");
        }
    }
}
package com.example.Booking.Controller;

import com.example.Booking.Model.Routes;
import com.example.Booking.Service.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/Routes")
@RestController
public class RoutesController {

    @Autowired
    private RoutesService routesService;

    @PostMapping("/Save")
    public String saveSelectedRoutes(@RequestBody Routes routes){
        routesService.saveUserSelectedRoutes(routes);
        return "Saved to the database";
    }

}

package com.example.Booking;

import com.example.Booking.Model.Route;
import com.example.Booking.Model.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class EntityMappingTest {
    @Autowired
    private TestEntityManager em;

    @Test
    void testTicketRouteRelationship() {
        Route route = new Route();
        route.setDepartureProvince("Test Province");
        em.persist(route);

        Ticket ticket = new Ticket();
        ticket.setRoute(route);
        em.persist(ticket);

        em.flush();
        em.clear();

        Ticket found = em.find(Ticket.class, ticket.getId());
        assertNotNull(found.getRoute());
        assertEquals(route.getId(), found.getRoute().getId());
    }
}

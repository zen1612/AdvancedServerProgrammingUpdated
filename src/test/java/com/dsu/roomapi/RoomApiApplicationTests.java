package com.dsu.roomapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:roomtest;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class RoomApiApplicationTests {

    @org.springframework.beans.factory.annotation.Autowired
    RoomService rooms;
    @org.springframework.beans.factory.annotation.Autowired
    ReservationService reservations;

    @Test
    void capacityMustBeBetweenOneAndTwenty() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> rooms.create("Invalid", "A", 0));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> rooms.create("Invalid", "A", 21));
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    void reservationsPreserveDatesAndRejectOverlapButAllowAdjacentBookings() {
        Room room = rooms.create("Test room", "A", 8);
        var start = java.time.LocalDateTime.of(2026, 11, 20, 10, 0);
        var end = start.plusHours(1);
        var created = reservations.create(new ReservationCreateRequest(room.getId(), "Zen", start, end));
        org.junit.jupiter.api.Assertions.assertEquals(start, created.startTime());
        org.junit.jupiter.api.Assertions.assertEquals(end, created.endTime());
        var conflict = org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> reservations.create(new ReservationCreateRequest(room.getId(), "Other",
                        start.plusMinutes(30), end.plusMinutes(30))));
        org.junit.jupiter.api.Assertions.assertEquals(409, conflict.getStatusCode().value());
        reservations.create(new ReservationCreateRequest(room.getId(), "Next", end, end.plusHours(1)));
        org.junit.jupiter.api.Assertions.assertEquals(2, reservations.findByRoom(room.getId()).size());
        reservations.delete(created.id());
        org.junit.jupiter.api.Assertions.assertEquals(1, reservations.findByRoom(room.getId()).size());
    }

}

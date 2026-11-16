package com.dsu.roomapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    private String reservedBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    protected Reservation() { }

    public Reservation(Long id, Room room, String reservedBy,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.room = room;
        this.reservedBy = reservedBy;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
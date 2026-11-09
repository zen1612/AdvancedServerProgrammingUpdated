package com.dsu.roomapi;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "room_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reservation_room"))
    private Room room;

    @Column(name = "reserved_by", nullable = false, length = 50)
    private String reservedBy;
    @Column(name = "start_time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    protected Reservation(){ }
    public Reservation(Long id, Room room, String reservedBy, LocalDateTime startTime, LocalDateTime endTime){
        this.id = id;
        this.room = room;
        this.reservedBy = reservedBy;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public Long getId() { return id; }
    public Room getRoom() { return room; }
    public String getReservedBy() { return reservedBy; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime () { return endTime; }

}

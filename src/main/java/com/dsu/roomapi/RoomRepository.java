package com.dsu.roomapi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository
        extends JpaRepository<Room, Long>{
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select r from Room r where r.id = :id")
    java.util.Optional<Room> findForReservation(@org.springframework.data.repository.query.Param("id") Long id);

    List<Room> findByNameContainingIgnoreCase(String keyword);

    List<Room> findByCapacityGreaterThanEqualAndNameContainingIgnoreCase(
            int capacity, String keyword);
}

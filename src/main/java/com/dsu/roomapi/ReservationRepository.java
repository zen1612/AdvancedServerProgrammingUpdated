package com.dsu.roomapi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {

    List<Reservation> findByRoomIdOrderByStartTime(Long roomId);

    boolean existsByRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, java.time.LocalDateTime endTime, java.time.LocalDateTime startTime);

    boolean existsByRoomId(Long roomId);
}


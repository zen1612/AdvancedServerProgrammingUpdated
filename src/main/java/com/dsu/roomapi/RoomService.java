package com.dsu.roomapi;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Room> search(Integer capacity, String keyword) {
        if (capacity == null){
            return roomRepository.findByNameContainingIgnoreCase(keyword);
        }
        return roomRepository.findByCapacityGreaterThanEqualAndNameContainingIgnoreCase(capacity, keyword);
    }

    public Optional<Room> findById(Long id) {
        return roomRepository.findById(id);
    }

    public Room create(String name, int capacity) {
        checkCapacity(capacity);
        return roomRepository.save(new Room(null, name, capacity));
    }

    @Transactional
    public Room update(Long id, String name, int capacity) {
        checkCapacity(capacity);
        Room room = get(id);
        room.update(name, capacity);
        return room;
    }

    @Transactional
    public void delete(Long id) {
        Room room = get(id);
        if (reservationRepository.existsByRoomId(id)) {
            throw new ConflictException("room " + id + " has reservations");
        }
        roomRepository.delete(room);
    }

    private void checkCapacity(int capacity) {
        if (capacity < 1 || capacity > 20) throw new IllegalArgumentException("Capacity must be 1 and 20");
    }

    public int count() {
        return roomRepository.findAll().size();
    }

    public Room get(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("room " + id + " not found"));
    }

}

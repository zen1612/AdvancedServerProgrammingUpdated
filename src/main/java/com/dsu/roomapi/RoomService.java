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

    public RoomService(RoomRepository roomRepository) {this.roomRepository = roomRepository; }

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

    public boolean delete(Long id){
        if (!roomRepository.existsById(id)) {
            return false;
        }
        roomRepository.deleteById(id);
        return true;
    }

    private void checkCapacity(int capacity) {
        if (capacity < 1 || capacity > 20) throw new IllegalArgumentException("Capacity must be 1 and 20");
    }

    public int count() {
        return roomRepository.findAll().size();
    }

    public Room get(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "room " + id + " not found"));
    }

}

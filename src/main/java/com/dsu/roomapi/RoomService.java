package com.dsu.roomapi;

import java.util.List;
import java.util.Optional;

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
        validate(name, capacity);
        return roomRepository.save(new Room(null, name, capacity));
    }

    public Optional<Room> replace(Long id, String name, int capacity) {
        validate(name, capacity);
        return roomRepository.findById(id)
                .map(existing -> roomRepository.save(new Room(id, name, capacity)));
    }

    public boolean delete(Long id){
        if (!roomRepository.existsById(id)) {
            return false;
        }
        roomRepository.deleteById(id);
        return true;
    }

    private void validate(String name, int capacity) {
        if (name == null || name.isBlank() || name.length() > 100) throw new IllegalArgumentException("Room name must contain 1–100 characters");
        if (capacity < 1 || capacity > 20) throw new IllegalArgumentException("Capacity must be 1–20");
    }

    public int count() {
        return roomRepository.findAll().size();
    }

}

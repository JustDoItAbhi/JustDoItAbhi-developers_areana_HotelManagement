package com.booking_service.repo;

import com.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByRoomId(UUID roomId);
    Optional<Booking>findById(UUID boookingId);
}

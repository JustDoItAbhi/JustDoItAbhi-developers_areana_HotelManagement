package com.inventory_service.repository;

import com.inventory_service.model.UserInventry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface InventoryRepository extends JpaRepository<UserInventry, UUID> {
    Optional<UserInventry>findByUserEmail(String email);
    UserInventry findByRoomId(UUID roomId);
}

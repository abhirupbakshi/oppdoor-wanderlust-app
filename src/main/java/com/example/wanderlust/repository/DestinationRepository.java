package com.example.wanderlust.repository;

import com.example.wanderlust.model.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DestinationRepository extends JpaRepository<Destination, UUID> {

    @Query("""
            SELECT d
            FROM Destination d
            WHERE d.user.username = :username
            """)
    Page<Destination> findDestinationsByUsername(@Param("username") String username, Pageable pageable);
}

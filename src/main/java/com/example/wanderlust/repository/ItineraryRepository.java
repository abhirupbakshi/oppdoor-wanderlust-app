package com.example.wanderlust.repository;

import com.example.wanderlust.model.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ItineraryRepository extends JpaRepository<Itinerary, UUID> {

    @Query("""
            SELECT i
            FROM Itinerary i
            WHERE i.destination.user.username = :username
            """)
    Page<Itinerary> findItinerariesByUsername(@Param("username") String username, Pageable pageable);
}

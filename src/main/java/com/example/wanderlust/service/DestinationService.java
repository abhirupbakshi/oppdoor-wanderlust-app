package com.example.wanderlust.service;

import com.example.wanderlust.model.Destination;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface DestinationService {

    Optional<Destination> getDestinationById(UUID uuid, Optional<String> matchUsername);

    Page<Destination> getDestinationsByUsername(String username, int page, int limit);

    Destination createDestination(String username, Destination destination);

    Destination updateDestination(UUID uuid, Destination destination, Optional<String> matchUsername);

    Destination deleteDestination(UUID uuid, Optional<String> matchUsername);
}

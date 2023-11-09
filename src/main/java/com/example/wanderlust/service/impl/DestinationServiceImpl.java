package com.example.wanderlust.service.impl;

import com.example.wanderlust.exception.DestinationAbsentException;
import com.example.wanderlust.exception.UserAbsentException;
import com.example.wanderlust.model.Destination;
import com.example.wanderlust.model.User;
import com.example.wanderlust.repository.DestinationRepository;
import com.example.wanderlust.repository.UserRepository;
import com.example.wanderlust.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Service
public class DestinationServiceImpl extends AbstractUserDependentEntityServiceImpl<Destination, UUID> implements DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationServiceImpl(DestinationRepository destinationRepository, UserRepository userRepository) {

        super(userRepository);
        this.destinationRepository = destinationRepository;
    }

    @Override
    protected String extractUsernameFromEntity(Destination destination) {

        return destination.getUser().getUsername();
    }

    @Override
    public Optional<Destination> getDestinationById(UUID uuid, Optional<String> matchUsername) {

        Assert.notNull(uuid, "UUID cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");

        return super.getEntityById(uuid, matchUsername, destinationRepository);
    }

    @Override
    public Page<Destination> getDestinationsByUsername(String username, int page, int limit) {

        Assert.notNull(username, "Username cannot be null");
        Assert.isTrue(page >= 1, "Page number must be greater than or equal to 1");
        Assert.isTrue(limit >= 1, "Page content limit must be greater than or equal to 1");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Destination> destinations = destinationRepository.findDestinationsByUsername(username, pageable);

        return destinations;
    }

    @Override
    public Destination createDestination(String username, Destination destination) {

        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(destination, "Destination cannot be null");

        User fetchedUser = userRepository.findById(username)
                .orElseThrow(() -> new UserAbsentException("User with username: " + username + " does not exist"));

        destination
                .setUuid(null)
                .setUser(fetchedUser);

        return destinationRepository.save(destination);
    }

    @Override
    public Destination updateDestination(UUID uuid, Destination destination, Optional<String> matchUsername) {

        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Destination deleteDestination(UUID uuid, Optional<String> matchUsername) {

        Assert.notNull(uuid, "UUID cannot be null");
        Assert.notNull(matchUsername, "Optional username cannot be null");

        String destinationAbsentErrMsg = "Destination with UUID: " + uuid + " does not exist";

        return super.deleteEntity(uuid, matchUsername, destinationRepository, () -> new DestinationAbsentException(destinationAbsentErrMsg));
    }
}

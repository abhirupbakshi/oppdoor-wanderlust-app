package com.example.wanderlust.web.controller;

import com.example.wanderlust.exception.DestinationAbsentException;
import com.example.wanderlust.exception.HttpMethodNotImplementedException;
import com.example.wanderlust.exception.IllegalRequestException;
import com.example.wanderlust.model.Destination;
import com.example.wanderlust.model.validation.group.Create;
import com.example.wanderlust.service.DestinationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @Autowired
    public DestinationController(DestinationService destinationService) {

        this.destinationService = destinationService;
    }

    @GetMapping("{uuid}")
    ResponseEntity<Destination> getDestinationById(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Optional<Destination> optionalDestination = destinationService.getDestinationById(uuid, Optional.of(username));

        if (optionalDestination.isEmpty()) {
            throw new DestinationAbsentException("Destination with uuid: " + uuid + " does not exist");
        }

        return ResponseEntity.ok(optionalDestination.get());
    }

    @GetMapping
    ResponseEntity<List<Destination>> getDestinationsByUsername(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "limit", defaultValue = "10", required = false) int limit,
            Authentication authentication) {

        if (page < 1) {
            throw new IllegalRequestException("Page number must be greater than or equal to 1");
        }
        if (limit < 1) {
            throw new IllegalRequestException("Page limit must be greater than or equal to 1");
        }

        String username = authentication.getName();
        Page<Destination> destinationPage = destinationService.getDestinationsByUsername(username, page, limit);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(destinationPage.getTotalElements()))
                .body(destinationPage.getContent());
    }

    @PostMapping
    ResponseEntity<Destination> createDestination(
            @Validated(Create.class) @RequestBody Destination destination,
            Authentication authentication, HttpServletRequest request) {

        String username = authentication.getName();
        destination = destinationService.createDestination(username, destination);

        return ResponseEntity.created(URI.create(request.getRequestURI())).body(destination);
    }

    @PutMapping
    ResponseEntity<Destination> updateDestination() {

        throw new HttpMethodNotImplementedException("Update destination method has net been implemented yet");
    }

    @DeleteMapping("{uuid}")
    ResponseEntity<Destination> deleteDestination(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Destination destination = destinationService.deleteDestination(uuid, Optional.of(username));

        return ResponseEntity.ok(destination);
    }
}

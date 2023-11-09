package com.example.wanderlust.web.controller;

import com.example.wanderlust.exception.DestinationAbsentException;
import com.example.wanderlust.exception.HttpMethodNotImplementedException;
import com.example.wanderlust.exception.IllegalRequestException;
import com.example.wanderlust.exception.ItineraryAbsentException;
import com.example.wanderlust.model.Itinerary;
import com.example.wanderlust.model.validation.group.Create;
import com.example.wanderlust.service.ItineraryService;
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
@RequestMapping("itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping("{uuid}")
    ResponseEntity<Itinerary> getItineraryById(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Optional<Itinerary> optionalItinerary = itineraryService.getItineraryById(uuid, Optional.of(username));

        if (optionalItinerary.isEmpty()) {
            throw new ItineraryAbsentException("Itinerary with uuid: " + uuid + " does not exist");
        }

        return ResponseEntity.ok(optionalItinerary.get());
    }

    @GetMapping
    ResponseEntity<List<Itinerary>> getItinerariesByUsername(
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
        Page<Itinerary> itineraryPage = itineraryService.getItinerariesByUsername(username, page, limit);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(itineraryPage.getTotalElements()))
                .body(itineraryPage.getContent());
    }

    @PostMapping("{destinationId}")
    ResponseEntity<Itinerary> createItinerary(
            @PathVariable("destinationId") UUID destinationId,
            @Validated(Create.class) @RequestBody Itinerary itinerary,
            Authentication authentication,
            HttpServletRequest request) {

        String username = authentication.getName();
        itinerary = itineraryService.createItinerary(destinationId, itinerary, Optional.of(username));

        return ResponseEntity.created(URI.create(request.getRequestURI())).body(itinerary);
    }

    @PutMapping
    ResponseEntity<Itinerary> editItinerary() {

        throw new HttpMethodNotImplementedException("Update itinerary method has net been implemented yet");
    }

    @DeleteMapping("{uuid}")
    ResponseEntity<Itinerary> deleteItinerary(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        String username = authentication.getName();
        Itinerary itinerary = itineraryService.deleteItinerary(uuid, Optional.of(username));

        return ResponseEntity.ok(itinerary);
    }
}

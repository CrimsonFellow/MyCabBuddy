package com.travelcompany.bookingmanagement.controller;

import com.travelcompany.bookingmanagement.model.Booking;
import com.travelcompany.bookingmanagement.service.BookingService;
import com.travelcompany.bookingmanagement.service.FareService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FareService fareService; // Inject FareService

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class); // Define Logger

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String username = extractUsernameFromAuthHeader(authHeader); // Implement this method to decode username
        Booking createdBooking = bookingService.saveBooking(booking, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    // Helper method to extract username from Basic Auth header
    private String extractUsernameFromAuthHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            return credentials.split(":", 2)[0];
        }
        throw new IllegalArgumentException("Authorization header is invalid");
    }


    @GetMapping("/calculate-fare")
    public ResponseEntity<Double> calculateFare(
            @RequestParam String source,
            @RequestParam String destination) {
        try {
            // Log raw input
            logger.info("Received source: " + source + ", destination: " + destination);
            
            // Decode the source and destination
            source = URLDecoder.decode(source, StandardCharsets.UTF_8.toString());
            destination = URLDecoder.decode(destination, StandardCharsets.UTF_8.toString());
            
            // Log decoded input
            logger.info("Decoded source: " + source + ", destination: " + destination);
            
            // Calculate fare
            double fare = fareService.calculateFare(source, destination);
            return ResponseEntity.ok(fare);
        } catch (Exception e) {
            // Log the exception for debugging
            logger.error("Error calculating fare: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        Optional<Booking> existingBooking = bookingService.getBookingById(id);
        if (existingBooking.isPresent()) {
            Booking booking = existingBooking.get();
            booking.setSource(bookingDetails.getSource());
            booking.setDestination(bookingDetails.getDestination());
            booking.setBookingTime(bookingDetails.getBookingTime());
            booking.setFare(bookingDetails.getFare());
            booking.setPassenger(bookingDetails.getPassenger());
            Booking updatedBooking = bookingService.saveBooking(booking);
            return ResponseEntity.ok(updatedBooking);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> existingBooking = bookingService.getBookingById(id);
        if (existingBooking.isPresent()) {
            bookingService.deleteBooking(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


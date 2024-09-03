package com.travelcompany.bookingmanagement.controller;

import com.travelcompany.bookingmanagement.dto.PassengerRegistrationDto;
import com.travelcompany.bookingmanagement.model.PassengerProfile;
import com.travelcompany.bookingmanagement.service.PassengerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/passenger-profiles")
public class PassengerProfileController {

    private static final Logger logger = LoggerFactory.getLogger(PassengerProfileController.class);

    @Autowired
    private PassengerProfileService passengerProfileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<PassengerProfile> getAllPassengerProfiles() {
        logger.info("Fetching all passenger profiles");
        return passengerProfileService.getAllPassengerProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerProfile> getPassengerProfileById(@PathVariable Long id) {
        Optional<PassengerProfile> passengerProfile = passengerProfileService.getPassengerProfileById(id);
        return passengerProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPassenger(@RequestBody PassengerRegistrationDto registrationDto) {
        Map<String, Object> response = new HashMap<>();
        
        if (registrationDto.getPassword() == null || registrationDto.getPassword().isEmpty()) {
            logger.error("Password is missing or empty");
            response.put("error", "Password is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (passengerProfileService.existsByUsername(registrationDto.getUsername())) {
            logger.error("Username is already taken: " + registrationDto.getUsername());
            response.put("error", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        }

        if (passengerProfileService.existsByEmail(registrationDto.getEmail())) {
            logger.error("Email is already registered: " + registrationDto.getEmail());
            response.put("error", "Email is already registered");
            return ResponseEntity.badRequest().body(response);
        }

        // Encode the password using Argon2
        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());

        // Create a new PassengerProfile entity
        PassengerProfile passengerProfile = new PassengerProfile();
        passengerProfile.setName(registrationDto.getName());
        passengerProfile.setEmail(registrationDto.getEmail());
        passengerProfile.setUsername(registrationDto.getUsername());
        passengerProfile.setPassword(encodedPassword);  // Save the encoded password
        passengerProfile.setPhone(registrationDto.getPhone());  // Set the phone number

        // Save the passenger profile with the encoded password
        PassengerProfile savedPassenger = passengerProfileService.savePassengerProfile(passengerProfile);

        response.put("success", true);
        response.put("message", "User registered successfully with ID: " + savedPassenger.getId());
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(HttpServletRequest request) {
        // Extract the Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Decode the Base64 encoded username:password
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            // Log the received username and password
            logger.info("Decoded Username: " + username);
            logger.info("Decoded Password: " + password);

            // Authenticate the user
            Optional<PassengerProfile> passengerProfile = passengerProfileService.findByUsername(username);
            if (passengerProfile.isPresent()) {
                String storedPassword = passengerProfile.get().getPassword();
                logger.info("Stored encoded password: " + storedPassword);

                // Verify the password using the PasswordEncoder (Argon2 in your case)
                boolean isPasswordMatch = passwordEncoder.matches(password, storedPassword);
                logger.info("Password match result: " + isPasswordMatch);

                if (isPasswordMatch) {
                    logger.info("Login successful");

                    // Create a success response as a JSON object
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Login successful");
                    return ResponseEntity.ok()
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .body(response);
                }
            }

            // If authentication fails
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            return ResponseEntity.status(401)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .body(errorResponse);
        }

        // If Authorization header is missing or invalid
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Authorization header is missing or invalid");
        return ResponseEntity.status(401)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerProfile> updatePassengerProfile(@PathVariable Long id, @RequestBody PassengerProfile passengerDetails) {
        Optional<PassengerProfile> passengerProfile = passengerProfileService.getPassengerProfileById(id);
        if (passengerProfile.isPresent()) {
            PassengerProfile existingPassengerProfile = passengerProfile.get();
            existingPassengerProfile.setName(passengerDetails.getName());
            existingPassengerProfile.setEmail(passengerDetails.getEmail());
            existingPassengerProfile.setUsername(passengerDetails.getUsername());
            existingPassengerProfile.setPhone(passengerDetails.getPhone());

            // Only encode the password if it's being updated
            if (passengerDetails.getPassword() != null && !passengerDetails.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(passengerDetails.getPassword());
                existingPassengerProfile.setPassword(encodedPassword);
            }

            PassengerProfile updatedPassengerProfile = passengerProfileService.savePassengerProfile(existingPassengerProfile);
            return ResponseEntity.ok(updatedPassengerProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassengerProfile(@PathVariable Long id) {
        if (passengerProfileService.getPassengerProfileById(id).isPresent()) {
            passengerProfileService.deletePassengerProfile(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/current")
    public ResponseEntity<PassengerProfile> getCurrentPassengerProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);

            final String[] values = credentials.split(":", 2);
            String username = values[0];

            logger.info("Decoded Username for current profile: " + username);

            Optional<PassengerProfile> passengerProfile = passengerProfileService.findByUsername(username);
            if (passengerProfile.isPresent()) {
                PassengerProfile profile = passengerProfile.get();

                // Ensure the bookings are loaded, in case of lazy loading (depending on your JPA setup)
                if (profile.getBookings() != null) {
                    profile.getBookings().size(); // Trigger loading of bookings
                }

                // This will return the passenger profile with its associated bookings
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.status(401).body(null);
    }
}



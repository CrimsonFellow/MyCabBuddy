package com.travelcompany.bookingmanagement.repository;

import com.travelcompany.bookingmanagement.model.PassengerProfile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerProfileRepository extends JpaRepository<PassengerProfile, Long> {
	Optional<PassengerProfile> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}


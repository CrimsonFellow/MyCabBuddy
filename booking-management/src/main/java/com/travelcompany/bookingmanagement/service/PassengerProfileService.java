package com.travelcompany.bookingmanagement.service;

import com.travelcompany.bookingmanagement.model.PassengerProfile;
import com.travelcompany.bookingmanagement.repository.PassengerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerProfileService {

    @Autowired
    private PassengerProfileRepository passengerProfileRepository;


    public List<PassengerProfile> getAllPassengerProfiles() {
        return passengerProfileRepository.findAll();
    }

    public Optional<PassengerProfile> getPassengerProfileById(Long id) {
        return passengerProfileRepository.findById(id);
    }

    public PassengerProfile savePassengerProfile(PassengerProfile passengerProfile) {
        return passengerProfileRepository.save(passengerProfile);
    }

    public void deletePassengerProfile(Long id) {
        passengerProfileRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return passengerProfileRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return passengerProfileRepository.existsByEmail(email);
    }

    // New method to find a PassengerProfile by username
    public Optional<PassengerProfile> findByUsername(String username) {
        return passengerProfileRepository.findByUsername(username);
    }
}





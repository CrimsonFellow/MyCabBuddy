package com.travelcompany.bookingmanagement.security;

import com.travelcompany.bookingmanagement.model.PassengerProfile;
import com.travelcompany.bookingmanagement.service.PassengerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PassengerProfileService passengerProfileService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PassengerProfile> passengerProfile = passengerProfileService.findByUsername(username);
        if (passengerProfile.isPresent()) {
            return new CustomUserDetails(passengerProfile.get());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

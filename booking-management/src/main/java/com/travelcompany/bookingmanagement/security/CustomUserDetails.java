package com.travelcompany.bookingmanagement.security;

import com.travelcompany.bookingmanagement.model.PassengerProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final PassengerProfile passengerProfile;

    public CustomUserDetails(PassengerProfile passengerProfile) {
        this.passengerProfile = passengerProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For simplicity, we're assigning a default role
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return passengerProfile.getPassword();
    }

    @Override
    public String getUsername() {
        return passengerProfile.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

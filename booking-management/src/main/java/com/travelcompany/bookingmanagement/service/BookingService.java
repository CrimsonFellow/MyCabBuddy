package com.travelcompany.bookingmanagement.service;

import com.travelcompany.bookingmanagement.model.Booking;
import com.travelcompany.bookingmanagement.model.PassengerProfile;
import com.travelcompany.bookingmanagement.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FareService fareService;

    @Autowired
    private PassengerProfileService passengerProfileService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking saveBooking(Booking booking, String username) {
        // Find the passenger by username
        PassengerProfile passenger = passengerProfileService.findByUsername(username)
                                        .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        // Set the passenger and calculate the fare
        booking.setPassenger(passenger);
        double fare = fareService.calculateFare(booking.getSource(), booking.getDestination());
        booking.setFare(fare);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

}

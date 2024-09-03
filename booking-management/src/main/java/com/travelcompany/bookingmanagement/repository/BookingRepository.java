package com.travelcompany.bookingmanagement.repository;

import com.travelcompany.bookingmanagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

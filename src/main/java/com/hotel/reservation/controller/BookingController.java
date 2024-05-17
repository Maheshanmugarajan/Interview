package com.hotel.reservation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.RoomsNotAvailableException;
import com.hotel.reservation.service.ReservationService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;


@RestController
public class BookingController {
    @Autowired
    ReservationService reservationService;

    @GetMapping("/findRoom/{date}")
    public Room getAvailableRoomsByDate(@PathVariable String date)
    {
        return reservationService.getAvailableRoomsByDate(LocalDate.parse(date));
    }

    @GetMapping("/findBooking/{guest}")
    public ArrayList<Reservation> getReservationsForGuest(@PathVariable String guest)
    {
        return reservationService.getReservationsForGuest(guest);
    }

    @PostMapping("/saveBooking")
    public ResponseEntity<Object> BookRoom(@Valid @RequestBody Reservation reservation){
        boolean success = reservationService.saveReservation(reservation);
        if(!success){
            throw new RoomsNotAvailableException("All rooms booked or bookings not Available for past date");
        }
        URI location= ServletUriComponentsBuilder.fromCurrentRequest().path("/{guest}").buildAndExpand(reservation.getGuest()).toUri();
        return ResponseEntity.created(location).build();
    }
}

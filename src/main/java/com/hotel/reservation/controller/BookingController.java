package com.hotel.reservation.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hotel.booking.constant.ReservationStatus;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.exception.RoomsNotAvailableException;
import com.hotel.reservation.service.ReservationService;

@RestController
public class BookingController {
	@Autowired
	ReservationService reservationService;

	@GetMapping("/findRoom/{date}")
	public Room getAvailableRoomsByDate(@PathVariable String date) {
		return reservationService.getAvailableRoomsByDate(LocalDate.parse(date));
	}

	@GetMapping("/findBooking/{guest}")
	public ArrayList<Reservation> getReservationsForGuest(@PathVariable String guest) {
		return reservationService.getReservationsForGuest(guest);
	}

	@PostMapping("/saveBooking")
	public ResponseEntity<Object> BookRoom(@RequestBody Reservation reservation) {
		ReservationStatus status = reservationService.saveReservation(reservation);
		if (!ReservationStatus.BOOK_SUCCESS.equals(status))
			throw new RoomsNotAvailableException(status.getStatus());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{guest}")
				.buildAndExpand(reservation.getGuestName()).toUri();
		return ResponseEntity.created(location).build();
	}
}

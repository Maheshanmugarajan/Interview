package com.hotel.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.repository.ReservationRep;

@Service
public class ReservationServiceImpl implements ReservationService {

	// service pattern to manage transactionals
	// and handle services for reservation between server and client

	public ReservationServiceImpl(ReservationRep reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Autowired
	ReservationRep reservationRepository;

	@Override
	public Room getAvailableRoomsByDate(LocalDate date) {
		return reservationRepository.getAvailableRoomsByDate(date);
	}

	// get all reservations for logger user
	@Override
	public ArrayList<Reservation> getReservationsForGuest(String guest) {
		return (ArrayList<Reservation>) reservationRepository.getReservationsForGuest(guest);
	}

	// transfer data between temp reservation and Reservation class after check it
	// to save it
	@Override
	public boolean saveReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}
}

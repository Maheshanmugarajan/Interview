package com.hotel.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.booking.constant.ReservationStatus;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.repository.ReservationRep;

@Service
public class ReservationServiceImpl implements ReservationService {

	public ReservationServiceImpl(ReservationRep reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@Autowired
	ReservationRep reservationRepository;

	@Override
	public Room getAvailableRoomsByDate(LocalDate date) {
		return reservationRepository.getAvailableRoomsByDate(date);
	}

	@Override
	public ArrayList<Reservation> getReservationsForGuest(String guest) {
		return (ArrayList<Reservation>) reservationRepository.getReservationsForGuest(guest);
	}

	@Override
	public ReservationStatus saveReservation(Reservation reservation) {
		return reservationRepository.save(reservation);
	}
}

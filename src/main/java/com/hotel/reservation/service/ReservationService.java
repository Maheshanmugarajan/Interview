package com.hotel.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotel.booking.constant.ReservationStatus;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;

//Service Pattern for Reservation
public interface ReservationService {

	public Room getAvailableRoomsByDate(LocalDate date);

	public ArrayList<Reservation> getReservationsForGuest(String guest);

	public ReservationStatus saveReservation(Reservation currentReservation);

}

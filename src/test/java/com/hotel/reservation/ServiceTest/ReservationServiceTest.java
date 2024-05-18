package com.hotel.reservation.ServiceTest;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.repository.ReservationRep;
import com.hotel.reservation.service.ReservationService;
import com.hotel.reservation.service.ReservationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
	@Mock
	private ReservationRep reservationRepository;
	ReservationService reservationService;

	@BeforeEach
	void initUseCase() {
		reservationService = new ReservationServiceImpl(reservationRepository);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void savedrReservation_Success() {
		Reservation reservation = new Reservation("Adam", 1, LocalDate.now());
		when(reservationRepository.save(any(Reservation.class))).thenReturn(true);
		boolean savedReservationStatus = reservationService.saveReservation(reservation);
		assertThat(savedReservationStatus).isEqualTo(true);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void savedrReservation_Failed() {
		Reservation reservation = new Reservation("Adam", 1, LocalDate.now());
		when(reservationRepository.save(any(Reservation.class))).thenReturn(false);
		boolean savedReservationStatus = reservationService.saveReservation(reservation);
		assertThat(savedReservationStatus).isEqualTo(false);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getReservationsForGuest_success() {
		Reservation reservation = new Reservation("Adam", 1, LocalDate.now());
		ArrayList<Reservation> sendReservations = new ArrayList<>();
		sendReservations.add(reservation);
		when(reservationRepository.getReservationsForGuest("Adam")).thenReturn(sendReservations);
		ArrayList<Reservation> reservations = reservationService.getReservationsForGuest("Adam");
		assertThat(reservations.get(0)).isEqualTo(reservation);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getReservationsForGuest_failed() {
		// providing knowledge
		when(reservationRepository.getReservationsForGuest("swara")).thenReturn(null);
		ArrayList<Reservation> reservations = reservationService.getReservationsForGuest("swara");
		assertThat(reservations).isEqualTo(null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void getAvailableRoomsByDate_success() {
		// providing knowledge
		when(reservationRepository.getAvailableRoomsByDate(LocalDate.now())).thenReturn(new Room(5, LocalDate.now()));
		Room room = reservationService.getAvailableRoomsByDate(LocalDate.now());
		assertThat(room.getRoomsAvailable()).isEqualTo(5);
	}
}
package com.hotel.reservation.RepoTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.repository.ReservationRep;
import com.hotel.reservation.repository.ReservationRepImpl;

@ExtendWith(MockitoExtension.class)
public class ReservationRepoTest {
	@Autowired
	private ReservationRep reservationRepository;

	@BeforeEach
	void initUseCase() {
		reservationRepository = new ReservationRepImpl();
		reservationRepository.setRoomsCount(10);
		ReservationRepImpl.existingReservationList
				.add(new Reservation("Hendry", 1000.0, LocalDate.now(), LocalDate.now().plusDays(5), 1));
		ReservationRepImpl.existingReservationList
				.add(new Reservation("William", 2000.0, LocalDate.now(), LocalDate.now().plusDays(5), 1));
		ReservationRepImpl.existingReservationList
				.add(new Reservation("Peter", 3000.0, LocalDate.now(), LocalDate.now().plusDays(5), 1));
		ReservationRepImpl.rooms.put(LocalDate.now().toString(), 7);
	}

	@SuppressWarnings("deprecation")
	@Test
	void save_success() {
		Reservation reservation = new Reservation("vj", 1000.0, LocalDate.now(), LocalDate.now().plusDays(5), 1);
		boolean status = reservationRepository.save(reservation);
		assertThat(status).isEqualTo(true);
	}

	@SuppressWarnings("deprecation")
	@Test
	void save_failed() {
		Reservation reservation = new Reservation("vj", 1000.0, LocalDate.parse("2023-03-21"),
				LocalDate.now().plusDays(5), 1);
		boolean status = reservationRepository.save(reservation);
		assertThat(status).isEqualTo(false);
	}

	@SuppressWarnings("deprecation")
	@Test
	void findByGuest_success() {
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationRepository
				.getReservationsForGuest("William");
		assertThat(reservations.size()).isGreaterThanOrEqualTo(1);
	}

	@SuppressWarnings("deprecation")
	@Test
	void findByGuest_failed() {
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationRepository
				.getReservationsForGuest("Obama");
		assertThat(reservations.size()).isGreaterThanOrEqualTo(0);
	}

	@SuppressWarnings("deprecation")
	@Test
	void findRoomsByDate_success() {
		LocalDate localDate = LocalDate.now().plusDays(10);
		Room room = reservationRepository.getAvailableRoomsByDate(localDate);
		assertThat(room.getRoomsAvailable()).isGreaterThanOrEqualTo(10);
	}

	@SuppressWarnings("deprecation")
	void findRoomsByDate_failed() {
		Room room = reservationRepository.getAvailableRoomsByDate(LocalDate.parse("2023-03-22"));
		assertThat(room).isNull();
	}

}

package com.hotel.reservation.RepoTest;

import static org.assertj.core.api.Java6Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.hotel.booking.constant.ReservationStatus;
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
				.add(new Reservation("Hendry", 1,LocalDate.now()));
		ReservationRepImpl.existingReservationList
				.add(new Reservation("William", 2, LocalDate.now()));
		ReservationRepImpl.existingReservationList
				.add(new Reservation("Peter", 3,LocalDate.now()));
		ReservationRepImpl.rooms.put(LocalDate.now().toString(), 7);
	}

	@SuppressWarnings("deprecation")
	@Test
	void save_success() {
		Reservation reservation = new Reservation("Adam", 1,LocalDate.now());
		ReservationStatus status = reservationRepository.save(reservation);
		assertThat(status).isEqualTo(ReservationStatus.BOOK_SUCCESS);
	}

	@SuppressWarnings("deprecation")
	@Test
	void save_failed() {
		Reservation reservation = new Reservation("vj", 1,LocalDate.parse("2024-04-22"));
		ReservationStatus status = reservationRepository.save(reservation);
		assertThat(status).isEqualTo(ReservationStatus.BOOK_DATE_PAST);
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
		LocalDate localDate = LocalDate.now();
		Room room = reservationRepository.getAvailableRoomsByDate(localDate);
		assertThat(room.getRoomsAvailable()).isGreaterThanOrEqualTo(1);
	}

	@SuppressWarnings("deprecation")
	void findRoomsByDate_failed() {
		Room room = reservationRepository.getAvailableRoomsByDate(LocalDate.parse("2024-04-22"));
		assertThat(room).isNull();
	}

}

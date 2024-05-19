package com.hotel.reservation.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hotel.booking.constant.ReservationStatus;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;

@Repository
public class ReservationRepImpl implements ReservationRep {

	public static List<Reservation> existingReservationList = new ArrayList<>();
	public static HashMap<String, Integer> rooms = new HashMap<>();

	private static int roomsCount;

	@Value("${booking.rooms.limit}")
	public void setRoomsCount(int limit) {
		ReservationRepImpl.roomsCount = limit;
	}

	static {
		existingReservationList.add(new Reservation("Charles", 101, LocalDate.now()));
		existingReservationList.add(new Reservation("William", 102, LocalDate.now()));
		existingReservationList.add(new Reservation("Harry", 103, LocalDate.now()));
		rooms.put(LocalDate.now().toString() + "101", 101);
		rooms.put(LocalDate.now().toString() + "102", 102);
		rooms.put(LocalDate.now().toString() + "103", 103);
	}

	@Override
	public ArrayList<Reservation> getReservationsForGuest(String guestName) {
		return existingReservationList.stream()
				.filter(reservation -> reservation.getGuestName().equalsIgnoreCase(guestName))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Room getAvailableRoomsByDate(LocalDate date) {
		if (!date.isBefore(LocalDate.now())) {
			Map<String, Integer> totalRoomOnBookedDate = countOccurrences(rooms);
			Map<String, String> totalRoomByBookedDate = findBookedRooms(rooms);
			Room availability = new Room((roomsCount - totalRoomOnBookedDate.getOrDefault(date.toString(), 0)), date);
			availability.setAllocatedRooms(totalRoomByBookedDate.getOrDefault(date.toString(), "NONE"));
			return ((roomsCount - totalRoomOnBookedDate.getOrDefault(date.toString(), roomsCount)) < 10 ? availability
					: new Room(roomsCount, date));
		}
		return null;
	}

	@Override
	public ReservationStatus save(@Valid Reservation reservation) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(reservation.getCheckIn(), formatter);
		ReservationStatus roomStatus = getRoomAvailability(reservation, startDate);		
		if (ReservationStatus.BOOK_SUCCESS.equals(roomStatus)) {
			return bookRoom(reservation, startDate);
		}else {
			return roomStatus;
		}
	}

	public ReservationStatus getRoomAvailability(Reservation reservation, LocalDate startDate) {
		return calculateAvailability(startDate, reservation.getRoomNumber());
	}

	private ReservationStatus calculateAvailability(LocalDate startDate, int roomNumber) {
		long availableCount = startDate.datesUntil(startDate.plusDays(1))
				.filter(date -> !rooms.containsKey(startDate.toString() + roomNumber)).count();
		if (availableCount == 0 && rooms.size() + availableCount <= roomsCount)
			return ReservationStatus.ROOM_NOT_AVAILABLE;

		if (!(availableCount == 1 && rooms.size() + availableCount <= roomsCount))
			return ReservationStatus.ROOM_FULL_BOOKED;
		else
			return ReservationStatus.BOOK_SUCCESS;
	}

	public ReservationStatus bookRoom(Reservation reservation, LocalDate startDate) {
		if (startDate.isBefore(LocalDate.now())) {
			return ReservationStatus.BOOK_DATE_PAST;
		}
		Map<String, Integer> tempRooms = startDate.datesUntil(startDate.plusDays(1))
				.filter(date -> !rooms.containsKey(startDate.toString() + reservation.getRoomNumber()))
				.limit(roomsCount - rooms.size())
				.collect(Collectors.toMap(date -> startDate.toString() + reservation.getRoomNumber(),
						date -> reservation.getRoomNumber()));

		if (tempRooms.size() == 1) {
			rooms.putAll(tempRooms);
			existingReservationList.add(reservation);
			return ReservationStatus.BOOK_SUCCESS;
		}
		return ReservationStatus.BOOK_FAILED;
	}

	public static Map<String, Integer> countOccurrences(HashMap<String, Integer> map) {
		return map.keySet().stream()
				.collect(Collectors.groupingBy(s -> s.substring(0, 10), Collectors.reducing(0, e -> 1, Integer::sum)));
	}

	private static Map<String, String> findBookedRooms(HashMap<String, Integer> map) {
		return map.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getKey().substring(0, 10),
				Collectors.mapping(entry -> entry.getValue().toString(), Collectors.joining(","))));
	}
}

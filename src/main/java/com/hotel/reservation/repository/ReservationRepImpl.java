package com.hotel.reservation.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;

@Repository
public class ReservationRepImpl implements ReservationRep {

	/*
	 * Made below two variables public to access in Unit tests as no DB is used Due
	 * to time constraint didn't create a clone of below ArrayList and use that
	 * clone using the setter in Unit testing.
	 *
	 */
	public static List<Reservation> existingReservationList = new ArrayList<Reservation>();
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
		return (ArrayList<Reservation>) existingReservationList.stream()
				.filter(reservation -> reservation.getGuestName().equalsIgnoreCase(guestName))
				.collect(Collectors.toList());
	}

	@Override
	public Room getAvailableRoomsByDate(LocalDate date) {
		if (!date.isBefore(LocalDate.now())) {
			Map<String, Integer> counts = countOccurrences(rooms);
			Room availability = new Room((roomsCount - counts.getOrDefault(date.toString(), roomsCount)), date);
			availability.setAllocatedRooms(findRoomList(rooms));
			return ((roomsCount - counts.getOrDefault(date.toString(), roomsCount)) < 10 ? availability
					: new Room(0, date));
		}
		return null;
	}

	@Override
	public boolean save(Reservation reservation) {
		if (getRoomAvailablity(reservation)) {
			return BookRoom(reservation);
		}
		return false;
	}

	public boolean getRoomAvailablity(Reservation reservation) {
		int i = 0, j = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(reservation.getCheckIn(), formatter);
		int stayDays = ((int) ChronoUnit.DAYS.between(LocalDate.now(), startDate) + 1);
		if (rooms == null)
			return true;

		while (i < stayDays) {
			startDate.plusDays(i);
			if (!rooms.containsKey((startDate.toString() + reservation.getRoomNumber()))) {
				if (rooms.size() < roomsCount) {
					j++;
				} else {
					return false;
				}
			}
			i++;
		}
		if (i == j) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean BookRoom(Reservation reservation) {
		int i = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(reservation.getCheckIn(), formatter);
		if (startDate.isBefore(LocalDate.now())) {
			return false;
		}
		int stayDays = ((int) ChronoUnit.DAYS.between(LocalDate.now(), startDate) + 1);
		HashMap<String, Integer> tempRooms = new HashMap();
		while (i < stayDays) {
			if (i > 0) {
				startDate = startDate.plusDays(1);
			}
			if (!rooms.containsKey((startDate.toString() + reservation.getRoomNumber()))) {
				if (rooms.size() <= roomsCount)
					tempRooms.put((startDate.toString() + reservation.getRoomNumber()), reservation.getRoomNumber());
			} else {
				return false;
			}
			i++;
		}
		if (i == tempRooms.size()) {
			try {
				rooms.putAll(tempRooms);
				existingReservationList.add(reservation);
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static Map<String, Integer> countOccurrences(HashMap<String, Integer> map) {
		Map<String, Integer> counts = new HashMap<>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			String key = (entry.getKey()).substring(0, 10);
			int count = counts.getOrDefault(key, 0);
			counts.put(key, count + 1);
		}
		return counts;
	}

	private String findRoomList(HashMap<String, Integer> map) {
		List<Integer> roomlist = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			roomlist.add(entry.getValue());
		}
		return roomlist.stream().map(Object::toString).collect(Collectors.joining(", "));
	}
}

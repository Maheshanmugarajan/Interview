package com.hotel.reservation.entity;

import java.time.LocalDate;

public class Room {

	private int roomsAvailable;
	private LocalDate BookingDate;
	private String allocatedRooms;

	public Room(int roomsAvailable, LocalDate arrivalDate) {
		this.roomsAvailable = roomsAvailable;
		BookingDate = arrivalDate;
	}

	public void setRoomsAvailable(int roomsAvailable) {
		this.roomsAvailable = roomsAvailable;
	}

	public void setBookingDate(LocalDate bookingDate) {
		BookingDate = bookingDate;
	}

	public int getRoomsAvailable() {
		return roomsAvailable;
	}

	public String  getBookingDate() {
		return BookingDate.toString();
	}

	public String getAllocatedRooms() {
		return allocatedRooms;
	}

	public void setAllocatedRooms(String allocatedRooms) {
		this.allocatedRooms = allocatedRooms;
	}	
}

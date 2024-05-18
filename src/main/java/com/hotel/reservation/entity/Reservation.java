package com.hotel.reservation.entity;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;

public class Reservation {

	@Size(min = 2, message = "The guest name is too small")
	private String guestName;

	@Size(min = 1, message = "Minimum one room should be allocate")
	private int roomNumber;

	@FutureOrPresent(message = "Booking Date should be current date or future dates")
	private LocalDate checkIn;

	public Reservation() {
	}

	public Reservation(String guestName, int roomNumber, LocalDate checkIn) {
		this.guestName = guestName;
		this.roomNumber = roomNumber;
		this.checkIn = checkIn;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String  getCheckIn() {
		return checkIn.toString();
	}

	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}

	@Override
	public String toString() {
		return "Booking Details " + "[GuestName=" + guestName + ", RoomNumber=" + roomNumber + ", checkIn Date="
				+ checkIn + "]";
	}
}

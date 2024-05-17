package com.hotel.reservation.entity;


import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Reservation {

	@FutureOrPresent(message="Booking Date should be current date or future dates")
	private LocalDate checkIn;
	
	@FutureOrPresent(message="Booking Date should be current date or future dates")
	private LocalDate checkOut;

	@Size(min=2,message="The guest name is too small")
	private String guest;
	
	@Size(min=1,message="Minimum one room should be allocate")
	private int numberOfRooms;

	private double price;

	public Reservation() {
	}

	public Reservation(String guest, double price, LocalDate checkIn, LocalDate checkOut, int numberOfRooms) {
		this.guest = guest;
		this.price = price;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.numberOfRooms = numberOfRooms;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}

	public LocalDate getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	
	@Override
	public String toString() {
		return "Reservation " + "[guest=" + guest + ", price=" + price  + ", checkIn=" + checkIn  + ", checkOut=" + checkOut + "]";
	}
}

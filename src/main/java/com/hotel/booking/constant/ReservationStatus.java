package com.hotel.booking.constant;

public enum ReservationStatus {
    BOOK_SUCCESS("BOOKING IS SUCCESSFULL"),
    BOOK_FAILED("BOOKING IS FAILED, KINDLY VALIDATE THE INPUT DETAILS"),
    BOOK_DATE_PAST("BOOKING DATE SHOULD BE PRESENT OR FUTURE DATE"),
	ROOM_NOT_AVAILABLE("CURRENT BOOKING ROOM IS NOT AVAILABLE"),
	ROOM_FULL_BOOKED("ALL ROOMS ARE BOOKED FOR THE CURRENT DATE");
	
    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

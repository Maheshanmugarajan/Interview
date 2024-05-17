package com.hotel.reservation.exception;

import java.util.Date;

public class ExceptionResponse {

	private Date TimeStamp;
	private String error;

	public Date getTimeStamp() {
		return TimeStamp;
	}

	public ExceptionResponse(Date timeStamp, String error, String details) {
		super();
		TimeStamp = timeStamp;
		this.error = error;
		this.details = details;
	}

	public String getError() {
		return error;
	}

	public String getDetails() {
		return details;
	}

	private String details;

}

package com.hotel.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class RoomsNotAvailableException extends RuntimeException {

	public RoomsNotAvailableException(String message) {
		super(message);

	}

	private static final long serialVersionUID = 1L;
}

package com.hotel.reservation.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotel.reservation.controller.BookingController;
import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;
import com.hotel.reservation.service.ReservationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({BookingController.class})
public class ReservationRestControllerInteTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    ReservationService reservationService;


    @Test
    public void save_reservation_success() throws Exception {
        Reservation reservation=new Reservation("swara",1000.0, LocalDate.now(),LocalDate.now().plusDays(5),1);

        when(reservationService.saveReservation(any())).thenReturn(Boolean.TRUE);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/saveBooking")
                                .content(asJsonString(reservation))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    public void save_reservation_failed() throws Exception {
        Reservation reservation=new Reservation("swara",1000.0, LocalDate.now(),LocalDate.now().plusDays(5),11);
        when(reservationService.saveReservation(any())).thenReturn(Boolean.FALSE);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/saveBooking")
                                .content(asJsonString(reservation))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isPreconditionFailed());
    }
    @Test
    public void get_reservation_by_guest() throws Exception {

        ArrayList<Reservation> reservations=new ArrayList<>();
        reservations.add(new Reservation("vj",1000.0,LocalDate.now(),LocalDate.now().plusDays(5),1));
        reservations.add(new Reservation("vj",2000.0,LocalDate.now(),LocalDate.now().plusDays(5),1));

        when(reservationService.getReservationsForGuest("vj")).thenReturn(reservations);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/findBooking/vj"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));
    }

    @Test
    public void get_rooms_by_date() throws Exception {
        LocalDate date=LocalDate.now();
        when(reservationService.getAvailableRoomsByDate(date)).thenReturn(new Room(5,LocalDate.now()));
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/findRoom/"+date))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
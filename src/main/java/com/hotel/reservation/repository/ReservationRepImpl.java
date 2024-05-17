package com.hotel.reservation.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hotel.reservation.entity.Reservation;
import com.hotel.reservation.entity.Room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepImpl implements ReservationRep{

    /*
    *  Made below two variables public to access in Unit tests as no DB is used
     * Due to time constraint didn't create a clone of below ArrayList and use that clone using the setter in Unit testing.
     *
    */
    public static List<Reservation> existingReservationList = new ArrayList<Reservation>();
    public static HashMap<String,Integer> rooms = new HashMap<>();

    private static int roomsCount;
    @Value("${booking.rooms.limit}")
    public void setRoomsCount(int limit){
        ReservationRepImpl.roomsCount = limit;
    }
    static {
    	existingReservationList.add(new Reservation("Charles",1000.0,LocalDate.now(),LocalDate.now().plusDays(5),1));
    	existingReservationList.add(new Reservation("William",2000.0,LocalDate.now(),LocalDate.now().plusDays(6),1));
    	existingReservationList.add(new Reservation("Harry",3000.0,LocalDate.now(),LocalDate.now().plusDays(7),1));
        rooms.put(LocalDate.now().toString(),7);

    }
    @Override
    public ArrayList<Reservation> getReservationsForGuest(String guest) {
        return (ArrayList<Reservation>) existingReservationList
                .stream()
                .filter(reservation -> reservation.getGuest().equalsIgnoreCase(guest))
                .collect(Collectors.toList());
    }

    @Override
    public Room getAvailableRoomsByDate(LocalDate date) {
        if(!date.isBefore(LocalDate.now())){
            return new Room(rooms.getOrDefault(date.toString(), roomsCount), date);
        }
        return null;
    }

    @Override
    public boolean save(Reservation reservation) {
        if(getRoomAvailablity(reservation)){
            return BookRoom(reservation);
        }
        return false;
    }

    public boolean getRoomAvailablity(Reservation reservation){
        int i=0;
        int j=0;
        LocalDate startDate =reservation.getCheckIn();
        int stayDays = (int) ChronoUnit.DAYS.between(startDate, reservation.getCheckOut());
        if(rooms==null){
            return true;
        }
        while(i<stayDays) {
            startDate.plusDays(i);
            if (!rooms.containsKey(startDate.toString()) || rooms.get(startDate.toString())>0) {
               j++;
            }
            i++;
        }
        if(i==j) {
            return true;
        }
        return false;
    }

    public boolean BookRoom(Reservation reservation){
        int i=0;
        LocalDate startDate =reservation.getCheckIn();
        if(reservation.getCheckIn().isBefore(LocalDate.now())){
            return false;
        }
        int stayDays = (int) ChronoUnit.DAYS.between(startDate, reservation.getCheckOut());
        int numberOfRooms = reservation.getNumberOfRooms();
        HashMap<String,Integer> tempRooms= new HashMap();
        while(i<stayDays) {
            if(i>0) {
                startDate = startDate.plusDays(1);
            }
            if (!rooms.containsKey(startDate.toString()) || rooms.get(startDate.toString())>0) {
                int roomsCntTemp=!rooms.containsKey(startDate.toString())? roomsCount :
                        rooms.get(startDate.toString())>0 ?  rooms.get(startDate.toString()):0;
                if(roomsCntTemp-numberOfRooms>=0) {
                    roomsCntTemp = rooms.containsKey(startDate.toString()) ? rooms.get(startDate.toString()) - numberOfRooms
                            : roomsCntTemp - numberOfRooms;
                    tempRooms.put(startDate.toString(), roomsCntTemp);
                }
                else{
                    return false;
                }
            }
            i++;
        }
        if(i==tempRooms.size()){
            rooms.putAll(tempRooms);
            existingReservationList.add(reservation);
            return true;
        }
        return false;
    }

    //Need to add a cleanup method to clean the rooms hashmap periodically by date.
}

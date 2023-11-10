package model;

import api.IRoomManager;

import java.util.*;

public class Room implements IRoomManager {
    private String name;
    private int capacity;
    private List<String> roomList;

    public Room(String name) {
        this.name = name;
        this.roomList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<String> roomList) {
        this.roomList = roomList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
    @Override
    public String getRoom(String roomName) {
        for (String room : this.roomList) {
            if (room.equals(roomName)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<String> getAllRooms() {
        return this.roomList;
    }

    @Override
    public void updateRoom(String roomName, String updatedRoom) {
        // treba popuniti
    }

    @Override
    public void deleteRoom(String roomName) {
        this.roomList.remove(roomName);
    }

    @Override
    public void addRoom(String newRoom) {
        this.roomList.add(newRoom);
    }

    @Override
    public void addAdditionalProperty(String key, Object value) {

    }

}

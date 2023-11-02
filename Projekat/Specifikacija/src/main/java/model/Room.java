package model;

import api.IRoomManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements IRoomManager {
    private String name;
    private int capacity;
    private Map<String, Object> additionalProperties;
    private List<String> roomList;

    public Room(String name) {
        this.name = name;
        this.additionalProperties = new HashMap<>();
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

    public Object getAdditionalProperty(String key) {
        return additionalProperties.get(key);
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
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
        this.additionalProperties.put(key, value);
    }
}

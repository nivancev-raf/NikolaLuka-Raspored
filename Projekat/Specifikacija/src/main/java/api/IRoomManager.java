package api;

import model.Room;

import java.util.List;

public interface IRoomManager {
    String getRoom(String roomName); // vraca prostoriju na osnovu imena
    List<String> getAllRooms(); // vraca listu svih prostorija
    void updateRoom(String roomName, String updatedRoom); // azurira info o prostoriji
    void deleteRoom(String roomName);
    void addRoom(String newRoom);
    void addAdditionalProperty(String key, Object value);

//    void getRoomByCapacity(int capacity);
//    void getRoomByLocation(String location);
//    void getRoomByCapacityAndLocation(int capacity, String location);

}

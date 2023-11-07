package manager;

import api.IRoomManager;
import model.Room;

import java.util.List;

public class RoomManager implements IRoomManager {

    private ScheduleManager scheduleManager;

    public RoomManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }


    @Override
    public String getRoom(String roomName) {
        return null;
    }

    @Override
    public List<String> getAllRooms() {
        return null;
    }

    @Override
    public void updateRoom(String roomName, String updatedRoom) {

    }



    @Override
    public void deleteRoom(String s) {

    }

    @Override
    public void addRoom(String newRoom) {

    }

    @Override
    public void addAdditionalProperty(String key, Object value) {

    }


}

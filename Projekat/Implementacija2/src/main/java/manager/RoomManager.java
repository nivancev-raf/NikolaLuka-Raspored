package manager;

import api.IRoomManager;
import model.Room;

public class RoomManager implements IRoomManager {

    private ScheduleManager scheduleManager;

    public RoomManager(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
    }

    @Override
    public void getRoom(String s) {

    }

    @Override
    public void getAllRooms() {

    }

    @Override
    public void updateRoom(String s, Room room) {

    }

    @Override
    public void deleteRoom(String s) {

    }

    @Override
    public void addRoom(Room room) {

    }
}

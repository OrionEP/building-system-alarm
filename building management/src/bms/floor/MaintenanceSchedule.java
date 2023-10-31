package bms.floor;

import bms.room.Room;
import bms.room.RoomState;
import bms.room.RoomType;
import bms.util.Encodable;
import bms.util.TimedItem;

import java.util.List;

/**
 * the maintenance to a given order rooms.
 */
public class MaintenanceSchedule extends Object implements TimedItem
        , Encodable {
    /**
     * list of room order for room to be maintained in the floor
     */
    private List<Room> roomOrder;
    /**
     * current room index in the room order
     */
    private int currentRoomIndex = 0;
    /**
     * time elapsed when maintain the room
     */
    private int elapsedTime;


    /**
     * Creates a  maintenance schedule for a floor's list of rooms.
     *
     * @param roomOrder list in order of rooms on which to perform maintenance
     * @requires roomOrder != null && roomOrder.size() > 0
     */

    public MaintenanceSchedule(List<Room> roomOrder) {
        this.roomOrder = roomOrder;


    }

    /**
     * time taken to perform maintenance on the given room.
     *
     * @param room Room to maintain in the floor room order.
     * @return int room's maintenance time in minutes.
     */
    public int getMaintenanceTime(Room room) {
        float maintenanceTime = 0;
        double difference = room.getArea() - Room.getMinArea();
        if (room.getType() == RoomType.STUDY) {
            maintenanceTime = (float) (1 * (difference * 0.2));
        }
        if (room.getType() == RoomType.LABORATORY) {
            maintenanceTime = (float) (2 * (difference * 0.2));

        }
        if (room.getType() == RoomType.OFFICE) {
            maintenanceTime = (float) (1.5 * (difference * 0.2));
        }
        return Math.round(maintenanceTime + Room.getMinArea());

    }

    /**
     * get the current room maintaining.
     *
     * @return Room room now maintain.
     */
    public Room getCurrentRoom() {
        return roomOrder.get(currentRoomIndex);

    }

    /**
     * the number of minutes that have elapsed while maintaining the current
     * room.
     *
     * @return int elapsed time how long have maintained current room.
     */
    public int getTimeElapsedCurrentRoom() {
        return elapsedTime;

    }

    /**
     * skip the room now current in maintaining to next room.
     */
    public void skipCurrentMaintenance() {
        currentRoomIndex += 1;
        currentRoomIndex %= roomOrder.size();
        elapsedTime = 0;

    }

    /**
     * Progresses the maintenance schedule by one minute.
     */
    @Override
    public void elapseOneMinute() {

        Room room = roomOrder.get(currentRoomIndex);
        if (room.evaluateRoomState() == RoomState.EVACUATE) ;
        else {
            elapsedTime++;
            if (getTimeElapsedCurrentRoom() >= getMaintenanceTime(room)) {
                room.setMaintenance(false);
                skipCurrentMaintenance();
            }
            roomOrder.get(currentRoomIndex).setMaintenance(true);

        }


    }

    /**
     * human-readable string representation of this maintenance schedule.
     *
     * @return string representation of this maintenance schedule.
     */
    @Override
    public String toString() {
        return "MaintenanceSchedule: " + "currentRoom=#"
                + getCurrentRoom().getRoomNumber() + ", currentElapsed="
                + elapsedTime;
    }

    /**
     * machine-readable string representation of this maintenance schedule.
     *
     * @return string encoded string representation of this maintenance
     * schedule.
     */
    @Override
    public String encode() {
        StringBuilder encode = new StringBuilder();
        for (Room room : roomOrder) {
            encode.append(room.getRoomNumber()).append(",");
        }
        return encode.deleteCharAt(encode.length() - 1).toString();
    }
}

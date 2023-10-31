package bms.floor;

import bms.exceptions.DuplicateRoomException;
import bms.exceptions.InsufficientSpaceException;
import bms.room.Room;
import bms.room.RoomType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MaintenanceScheduleTest {


    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getMaintenanceTime() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(201, RoomType.OFFICE, 20));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        Room room = roomOrder.get(0);
        assertEquals(10, maintenanceSchedule.getMaintenanceTime(room));
    }

    @Test
    public void getCurrentRoom() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(701, RoomType.LABORATORY, 25.0));
        roomOrder.add(new Room(702, RoomType.STUDY, 5.0));
        roomOrder.add(new Room(703, RoomType.OFFICE, 5.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        assertEquals(roomOrder.get(0), maintenanceSchedule.getCurrentRoom());
        maintenanceSchedule.skipCurrentMaintenance();
        assertEquals(roomOrder.get(1), maintenanceSchedule.getCurrentRoom());
    }

    @Test
    public void getTimeElapsedCurrentRoom() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(201, RoomType.OFFICE, 10.0));
        roomOrder.add(new Room(203, RoomType.STUDY, 6.0));
        roomOrder.add(new Room(204, RoomType.OFFICE, 13.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        assertEquals(0,maintenanceSchedule.getTimeElapsedCurrentRoom());
        maintenanceSchedule.elapseOneMinute();
        assertEquals(1,maintenanceSchedule.getTimeElapsedCurrentRoom());



    }

    @Test
    public void skipCurrentMaintenance() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(105, RoomType.OFFICE, 20.0));
        roomOrder.add(new Room(106, RoomType.STUDY, 16.0));
        roomOrder.add(new Room(107, RoomType.OFFICE, 23.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        maintenanceSchedule.skipCurrentMaintenance();
    }

    @Test
    public void elapseOneMinute() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(105, RoomType.OFFICE, 20.0));
        roomOrder.add(new Room(106, RoomType.STUDY, 16.0));
        roomOrder.add(new Room(107, RoomType.OFFICE, 23.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        for (int i = 0; i < maintenanceSchedule.getMaintenanceTime(roomOrder.get(0)); i++) {
            maintenanceSchedule.elapseOneMinute();

        }
        assertEquals(roomOrder.get(1), maintenanceSchedule.getCurrentRoom());
        assertTrue(maintenanceSchedule.getCurrentRoom().maintenanceOngoing());
    }

    @Test
    public void testToString() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(108, RoomType.OFFICE, 20.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        maintenanceSchedule.elapseOneMinute();
        assertEquals("MaintenanceSchedule: currentRoom=#108, currentElapsed=3",maintenanceSchedule.toString());

    }

    @Test
    public void encode() {
        List<Room> roomOrder = new ArrayList<>();
        roomOrder.add(new Room(105, RoomType.OFFICE, 29.0));
        roomOrder.add(new Room(106, RoomType.STUDY, 17.0));
        roomOrder.add(new Room(107, RoomType.OFFICE, 21.0));
        roomOrder.add(new Room(108, RoomType.LABORATORY, 12.0));
        MaintenanceSchedule maintenanceSchedule = new MaintenanceSchedule(roomOrder);
        assertEquals("105,106,107,108",maintenanceSchedule.encode());

    }
}
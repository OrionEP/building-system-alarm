package bms.building;

import bms.exceptions.*;
import bms.floor.Floor;
import bms.hazardevaluation.HazardEvaluator;
import bms.hazardevaluation.RuleBasedHazardEvaluator;
import bms.room.Room;
import bms.room.RoomType;
import bms.sensors.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BuildingInitialiserTest {


    @Test
    public void loadBuildings() throws IOException, FileFormatException, DuplicateFloorException, NoFloorBelowException
            , FloorTooSmallException, InsufficientSpaceException, DuplicateRoomException, DuplicateSensorException {

        List<Building> buildings = BuildingInitialiser.loadBuildings("saves/uqstlucia.txt");
        List<Building> buildings1=new ArrayList<>();

        Building building = new Building("Forgan Smith Building");
        Room room = new Room(101, RoomType.STUDY,23.8);
        Room room1 = new Room(102,RoomType.STUDY,20);
        Room room2 = new Room(103,RoomType.STUDY,28.5);
        NoiseSensor noiseSensor = new NoiseSensor(new int[]{52, 42, 53, 56},2);
        room2.addSensor(noiseSensor);
        Room room3 = new Room(104,RoomType.OFFICE,35);
        Room room4 = new Room(105,RoomType.STUDY,20);
        Room room5 = new Room(106,RoomType.STUDY,25.5);
        Room room6 = new Room(107,RoomType.OFFICE,40);
        CarbonDioxideSensor carbonDioxideSensor = new CarbonDioxideSensor(new int[]{745,1320,2782,3216,5043,3528,1970},3,700,300);
        OccupancySensor occupancySensor =new OccupancySensor(new int[]{11,13,13,13,10},3,20);
        List<HazardSensor> sensors = new ArrayList<>();
        sensors.add(carbonDioxideSensor);
        sensors.add(occupancySensor);
        RuleBasedHazardEvaluator ruleBasedHazardEvaluator = new RuleBasedHazardEvaluator(sensors);
        room6.addSensor(carbonDioxideSensor);
        room6.addSensor(occupancySensor);
        room6.setHazardEvaluator(ruleBasedHazardEvaluator);
        Room room7 = new Room(108,RoomType.STUDY,20);
        Room room8 = new Room(109,RoomType.STUDY,21.2);
        Room room9 = new Room(110,RoomType.STUDY,20);

        Floor floor = new Floor(1,8.5,40);
        floor.addRoom(room);
        floor.addRoom(room1);
        floor.addRoom(room2);
        floor.addRoom(room3);
        floor.addRoom(room4);
        floor.addRoom(room5);
        floor.addRoom(room6);
        floor.addRoom(room7);
        floor.addRoom(room8);
        floor.addRoom(room9);
        List<Room>roomOrder = new ArrayList<>();
        roomOrder.add(room3);
        roomOrder.add(room6);
        roomOrder.add(room8);
        roomOrder.add(room6);
        floor.createMaintenanceSchedule(roomOrder);
        building.addFloor(floor);
        building.equals(buildings.get(1));
        assertEquals(building.encode(),buildings.get(1).encode());




    }

    @Test(expected = FileFormatException.class)
    public void loadBuildings1() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/floornotequal.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings2() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/sensornotequal.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings3() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/matainroomnotexist.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings4() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/consecutiveroomorder.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings5() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/duplicatefloornumber.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings6() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/floorlessmin.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings7() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/nofloorbelow.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings8() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/floortolarge.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings9() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/duplicateroomnumber.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings10() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/nospaceroom.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings11() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/roomtypeinvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings12() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/lessminarea.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings13() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/roomtypenotexist.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings14() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/hazardtypeinvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings15() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/weightinginvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings16() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/weightinginvalid1.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings17() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/weightinginvalid2.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings18() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/twosensorssametype.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings19() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/nosensortype.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings20() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/updatefrequencyinvalid.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings21() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/updatefrequencyinvalid2.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings22() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/carboninvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings23() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/morecolon.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings24() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/spaceinvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings25() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/numfloorsinvalid.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings26() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/numnotparse.txt");

    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings27() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/floornumsinvalid1.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings28() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/idealvalueless0.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings29() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/variationlimitless0.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings30() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/capacityless0.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings31() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/sensorreadings0.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings32() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves//numsensorinvalid.txt");
    }
    @Test(expected = FileFormatException.class)
    public void loadBuildings33() throws IOException, FileFormatException{
        BuildingInitialiser.loadBuildings("saves/numroomsinvalid.txt");
    }


}
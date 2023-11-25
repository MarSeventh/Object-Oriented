import java.util.ArrayList;

public class RoomTable {
    private static final RoomTable instance = new RoomTable();
    private ArrayList<RoomState> roomStateTable;

    public RoomTable() {
        roomStateTable = new ArrayList<>();
    }

    public static RoomTable getInstance() {
        return instance;
    }

    public void initial(int roomNum) {
        for (int i = 0; i < roomNum; i++) {
            roomStateTable.add(RoomState.SPARE);
        }
    }

    public synchronized void setRoomState(int roomId, RoomState state) {
        roomStateTable.set(roomId, state);
    }

    public synchronized RoomState getRoomState(int roomId) {
        return roomStateTable.get(roomId);
    }

    public synchronized int getSpareRoom() {
        for (int i = 0; i < roomStateTable.size(); i++) {
            if (RoomState.SPARE.equals(roomStateTable.get(i))) {
                return i;
            }
        }
        return -1;
    }

}

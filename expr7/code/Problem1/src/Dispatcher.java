import java.util.ArrayList;

public class Dispatcher {
    private String workerId;
    private String workerName;
    private ArrayList<String> roomStateTable;
    private Cleaner cleaner;
    private boolean isCleanOperation;

    public Dispatcher(String id, String name) {
        this.workerId = id;
        this.workerName = name;
        this.roomStateTable = new ArrayList<>();
        //默认有5间房间
        for (int i = 0; i < 5; i++) {
            roomStateTable.add("SPARE");
        }
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setCleaner(Cleaner cleaner) {
        this.cleaner = cleaner;
    }

    private int findSpareRoom() {
        for (int i = 0; i < 10; i++) {
            if (roomStateTable.get(i).equals("SPARE")) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCleanOperation() {
        return isCleanOperation;
    }

    private boolean isCheckOutOperation() {
        return !isCleanOperation;
    }

    public int checkIn() {
        int spareRoomId = findSpareRoom();
        if (spareRoomId != -1) {
            System.out.println("Dispatcher " + getWorkerId() +
                    " " + getWorkerName() +
                    " : room " + spareRoomId + " is available to move in");             //管理员反馈找到合适的房间
            roomStateTable.set(spareRoomId, "OCCUPIED");
        } else {
            System.out.println("Dispatcher " + getWorkerId() +
                    " " + getWorkerName() +
                    " : sorry, there are no available rooms right now");                //管理员反馈没有空房
        }
        return spareRoomId;
    }

    public void checkOut(int roomId) {
        this.isCleanOperation = false;
        arrange2clean(roomId);
        System.out.println("Dispatcher " + getWorkerId() + " " + getWorkerName() +      //管理员反馈完成退房
                " : finish check out room " + roomId);
    }

    public void clean(int roomId) {
        this.isCleanOperation = true;
        arrange2clean(roomId);
    }

    private void arrange2clean(int roomId) {
        System.out.println("Dispatcher " + getWorkerId() +
                " " + getWorkerName() +
                " : arrange cleaner to clean room " + roomId);                          //管理员反馈安排清洁工前往清洁
        roomStateTable.set(roomId, "CLEANING");
        cleaner.cleanRoom(roomId);
        finishClean(roomId);
    }

    private void finishClean(int roomId) {
        if (isCleanOperation()) {
            roomStateTable.set(roomId, "OCCUPIED");
        } else if (isCheckOutOperation()) {
            roomStateTable.set(roomId, "SPARE");
        }
    }

}

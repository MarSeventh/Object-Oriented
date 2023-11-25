public class Dispatcher extends Worker {

    public Dispatcher(String workerId, String workerName, RequestList requestList) {
        super(workerId, workerName, requestList);
    }

    @Override
    public void run() {
        while (true) {
            Request request = getRequestList().getRequest();
            if (request == null) {
                break;
            }
            switch (request.getRequestTag()) {
                case REC_DIS_CHECKIN:
                    checkIn(request);
                    break;
                case REC_DIS_CHECKOUT:
                    arrangeClean4CheckOut(request);
                    break;
                case REC_DIS_CLEAN:
                    arrangeCleanOnly(request);
                    break;
                case CLE_DIS_CLEAN_ONLY:
                    finishCleanOnly(request);
                    Controller.getInstance().finishCommand();
                    break;
                case CLE_DIS_CLEAN_CHECKOUT:
                    checkOut(request);
                    break;
                default:
                    System.out.println("Dispatcher " + getWorkerId() +
                            " " + getWorkerName() +
                            " : receive unknown request");
            }
        }
        System.out.println("Dispatcher " + getWorkerId() +
                " " + getWorkerName() +
                " : good Bye!");
    }

    private void checkIn(Request request) {
        int spareRoomId = RoomTable.getInstance().getSpareRoom();
        if (spareRoomId != -1) {
            RoomTable.getInstance().setRoomState(spareRoomId, RoomState.OCCUPIED);
            Controller.getInstance().checkInSuccess(request, spareRoomId);
            System.out.println("Dispatcher " + getWorkerId() +
                    " " + getWorkerName() +
                    " : room " + spareRoomId + " is available to move in");                         //管理员反馈有空房
        } else {
            Controller.getInstance().checkInFailed(request);
            System.out.println("Dispatcher " + getWorkerId() +
                    " " + getWorkerName() +
                    " : sorry, there are no available rooms right now");                            //管理员无空房
        }
    }

    private void arrangeClean4CheckOut(Request request) {
        RoomTable.getInstance().setRoomState(request.getRoomId(), RoomState.CLEANING);
        Controller.getInstance().clean4CheckOut(request);
        System.out.println("Dispatcher " + getWorkerId() +
                " " + getWorkerName() +
                " : arrange cleaner to clean room " + request.getRoomId() + " for check out");      //管理员安排退房前清洁
    }

    private void arrangeCleanOnly(Request request) {
        RoomTable.getInstance().setRoomState(request.getRoomId(), RoomState.CLEANING);
        Controller.getInstance().cleanOnly(request);
        System.out.println("Dispatcher " + getWorkerId() +
                " " + getWorkerName() +
                " : arrange cleaner to clean room " + request.getRoomId() + " only");               //管理员安排房间清洁
    }

    private void finishCleanOnly(Request request) {
        RoomTable.getInstance().setRoomState(request.getRoomId(), RoomState.CLEANING);
    }


    private void checkOut(Request request) {
        RoomTable.getInstance().setRoomState(request.getRoomId(), RoomState.SPARE);
        Controller.getInstance().finishCheckOut(request);
        System.out.println("Dispatcher " + getWorkerId() +
                " " + getWorkerName() +
                " : finish check out room " + request.getRoomId());                                  //管理员反馈退房成功
    }

}

public class Receptionist extends Worker {

    public Receptionist(String workerId, String workerName, RequestList requestList) {
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
                case GUEST_REC_CHECKIN:
                    Controller.getInstance().incCommand();
                    apply4checkIn(request);
                    break;
                case GUEST_REC_CHECKOUT:
                    Controller.getInstance().incCommand();
                    apply4checkOut(request);
                    break;
                case GUEST_REC_CLEAN:
                    Controller.getInstance().incCommand();
                    apply4clean(request);
                    break;
                case DIS_REC_CHECKIN_SUCCESS:
                    replyCheckIn4success(request);
                    Controller.getInstance().finishCommand();
                    break;
                case DIS_REC_CHECKIN_FAILED:
                    replyCheckIn4failed(request);
                    Controller.getInstance().finishCommand();
                    break;
                case DIS_REC_CHECKOUT_SUCCESS:
                    replyCheckOut(request);
                    Controller.getInstance().finishCommand();
                    break;
                default:
                    System.out.println("Receptionist " + getWorkerId() +
                            " " + getWorkerName() +
                            " : recieve unknown request");
            }
        }
        System.out.println("Receptionist " + getWorkerId() +
                " " + getWorkerName() +
                " : good Bye!");        //下班啦
    }

    private void apply4checkIn(Request request) {
        Controller.getInstance().arrangeCheckIn(request);
    }

    private void apply4checkOut(Request request) {
        Controller.getInstance().arrangeCheckOut(request);
    }

    private void apply4clean(Request request) {
        Controller.getInstance().arrangeClean(request);
    }

    private void replyCheckIn4success(Request request) {
        System.out.println("Receptionist " + getWorkerId() +
                " " + getWorkerName() +
                " : Mr." + request.getGuestName() +
                ", you can now move in room " + request.getRoomId());           //前台反馈入住成功
    }

    private void replyCheckIn4failed(Request request) {
        System.out.println("Receptionist " + getWorkerId() +
                " " + getWorkerName() +
                " : Mr." + request.getGuestName() +
                ", There are no available rooms right now. Sorry!");            //前台反馈入住失败
    }

    private void replyCheckOut(Request request) {
        System.out.println("Receptionist " + getWorkerId() +
                " " + getWorkerName() +
                " : Mr." + request.getGuestName() +
                "you have checked out successfully. Have a nice day!");         //前台反馈退房成功
    }


}

public class Cleaner extends Worker {

    public Cleaner(String workerId, String workerName, RequestList requestList) {
        super(workerId, workerName, requestList);
    }

    @Override
    public void run() {
        while (true) {
            Request request = getRequestList().getRequest();
            if (request == null) {
                break;
            }
            if (RequestTag.DIS_CLE_CLEAN_ONLY.equals(request.getRequestTag()) ||
                    RequestTag.DIS_CLE_CLEAN_CHECKOUT.equals(request.getRequestTag())) {
                cleanRoom(request);
            } else {
                System.out.println("Cleaner " + getWorkerId() +
                        " " + getWorkerName() +
                        " : receive unknown request");
            }
        }
        System.out.println("CLeaner " + getWorkerId() +
                " " + getWorkerName() +
                " : good Bye!");                                //模拟结束,下班啦！
    }

    private void cleanRoom(Request request) {
        System.out.println("Cleaner " + getWorkerId() +
                " " + getWorkerName() +
                " : clean room " + request.getRoomId());        //清洁工反馈完成清洁
        Controller.getInstance().cleanFinish(request);
    }

}

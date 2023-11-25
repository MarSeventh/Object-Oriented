public class Worker extends Thread {

    private final String workerId;
    private final String workerName;
    private final RequestList requestList;

    public Worker(String workerId, String workerName, RequestList requestList) {
        this.workerId = workerId;
        this.workerName = workerName;
        this.requestList = requestList;
    }


    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public RequestList getRequestList() {
        return requestList;
    }

}

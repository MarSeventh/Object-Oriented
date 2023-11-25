public class Cleaner {
    private String workerId;
    private String workerName;

    public Cleaner(String id, String name) {
        this.workerId = id;
        this.workerName = name;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void cleanRoom(int roomId) {
        System.out.println("Cleaner " + getWorkerId() +
                " " + getWorkerName() +
                " : clean room " + roomId);                 //清洁工反馈完成清洁
    }
}

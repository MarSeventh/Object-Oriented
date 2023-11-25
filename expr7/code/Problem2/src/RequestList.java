import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requestList;

    public RequestList() {
        requestList = new ArrayList<>();
    }

    public synchronized void addRequest(Request request) {
        requestList.add(request);
        notifyAll();
    }

    public synchronized Request getRequest() { //从队列中取出一个请求
        Request request = null;
        while (!Controller.getInstance().isEnd()) {
            if (requestList.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                request = requestList.get(0);
                requestList.remove(0);
                break;
            }
        }
        return request;
    }
}

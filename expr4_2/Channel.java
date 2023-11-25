import com.oocourse.TimableOutput;
import com.oocourse.exp4.TaskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Channel {
    private volatile static Channel singleton;
    private final List<TaskRequest> list;
    private final Semaphore plates;
    private boolean allReceived;
    
    private Channel() {
        this.list = new ArrayList<>();
        this.plates = new Semaphore(3);
        this.allReceived = false;
    }
    
    public static Channel getSingleton() {
        if (singleton == null) {
            synchronized (Channel.class) {
                if (singleton == null) {
                    singleton = new Channel();
                }
            }
        }
        return singleton;
    }
    
    public synchronized void addTask(TaskRequest task) {
        list.add(task);
        TimableOutput.println(task + " is added to channel");
        notifyAll();
    }
    
    public synchronized TaskRequest fetchTask(int id) {
        while (list.isEmpty() && !allReceived) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        TaskRequest task = list.get(0);
        list.remove(0);
        TimableOutput.println(task + " is removed from channel by " + id);
        notifyAll();
        return task;
    }
    
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }
    
    public synchronized boolean isAllReceived() {
        return allReceived;
    }
    
    public synchronized void setAllReceived(boolean allReceived) {
        this.allReceived = allReceived;
        notifyAll();
    }
    
    public void acquirePlate() {
        try {
            //TODO
            //请替换sentence1为合适内容(1)
            plates.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void releasePlate() {
        //TODO
        //请替换sentence2为合适内容(2)
        plates.release();
    }
}

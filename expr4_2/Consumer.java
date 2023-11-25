import com.oocourse.TimableOutput;
import com.oocourse.exp4.TaskRequest;

import static java.lang.Thread.sleep;

public class Consumer implements Runnable {
    private final int id;
    private final Channel channel;

    public Consumer(int id) {
        this.id = id;
        this.channel = Channel.getSingleton();
    }

    @Override
    public void run() {
        while (true) {
            //TODO
            //请替换sentence为合适内容(5)
            if (channel.isAllReceived() && channel.isEmpty()) {
                break;
            }
            TaskRequest task = channel.fetchTask(this.id);
            if (task != null) {
                channel.releasePlate();
                solveTask(task);
            }
        }
        TimableOutput.println(String.format("Consumer %d is free now", this.id));
    }

    public void solveTask(TaskRequest task) {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimableOutput.println(task + " is solved now");
    }
}

import com.oocourse.TimableOutput;
import com.oocourse.exp4.TaskInput;
import com.oocourse.exp4.TaskRequest;

public class Provider implements Runnable {
    private final Channel channel;

    public Provider() {
        this.channel = Channel.getSingleton();
    }

    private void initConsumers() {
        for (int i = 1; i <= 3; i++) {
            Consumer consumer = new Consumer(i);
            //TODO
            //请替换sentence1为合适内容(3)
            new Thread(consumer).start();
        }
    }

    @Override
    public void run() {
        TaskInput taskInput = new TaskInput(System.in);
        initConsumers();
        while (true) {
            TaskRequest taskRequest = taskInput.nextRequest();
            if (taskRequest == null) {
                break;
            } else {
                //TODO
                //请替换sentence2为合适内容(4)
                channel.acquirePlate();
                channel.addTask(taskRequest);
            }
        }
        Channel.getSingleton().setAllReceived(true);
        TimableOutput.println("Provider is free now");
    }
}

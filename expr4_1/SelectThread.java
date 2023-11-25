import com.oocourse.TimableOutput;

import java.util.concurrent.atomic.AtomicInteger;

public class SelectThread extends Thread {
    private final Database<String,String> database;
    private final String key;
    private static final AtomicInteger atomicCounter = new AtomicInteger(0);
    
    public SelectThread(Database<String,String> database, String key) {
        this.database = database;
        this.key = key;
    }
    
    public void run() {
        while (true) {
            int counter = atomicCounter.incrementAndGet();
            String value = database.select(key);
            TimableOutput.println(counter + ":" + key + " => " + value);
        }
    }
}

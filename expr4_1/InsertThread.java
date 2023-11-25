import java.util.Random;

public class InsertThread extends Thread {
    private static final Random random = new Random(314159);
    private final Database<String,String> database;
    private final String key;
    private final String value;
    
    public InsertThread(Database<String,String> database, String key, String value) {
        this.database = database;
        this.key = key;
        this.value = value;
    }
    
    public void run() {
        while (true) {
            database.insert(key, value);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

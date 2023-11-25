import java.util.HashMap;

public class CountController {
    private final int mx = 4;
    private final int nx = 2;
    private final HashMap<Integer, Integer> servicecounts;
    private final HashMap<Integer, Integer> inservicecounts;

    public CountController() {
        this.servicecounts = new HashMap<>();
        this.inservicecounts = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            servicecounts.put(i, 0);
            inservicecounts.put(i, 0);
        }
    }

    public synchronized void wantExchange(int floor, int onlyin) {
        try {
            if (onlyin == 0) {
                while (servicecounts.get(floor) >= mx) {
                    wait();
                }
                servicecounts.put(floor, servicecounts.get(floor) + 1);
            } else {
                while (servicecounts.get(floor) >= mx || inservicecounts.get(floor) >= nx) {
                    wait();
                }
                servicecounts.put(floor, servicecounts.get(floor) + 1);
                inservicecounts.put(floor, inservicecounts.get(floor) + 1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void endExchange(int floor, int onlyin) {
        if (onlyin == 0) {
            servicecounts.put(floor, servicecounts.get(floor) - 1);
        } else {
            servicecounts.put(floor, servicecounts.get(floor) - 1);
            inservicecounts.put(floor, inservicecounts.get(floor) - 1);
        }
        notifyAll();
    }
}

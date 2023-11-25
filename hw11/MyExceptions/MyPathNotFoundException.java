import com.oocourse.spec3.exceptions.PathNotFoundException;

import java.util.HashMap;

public class MyPathNotFoundException extends PathNotFoundException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyPathNotFoundException(int id) {
        this.id = id;
        count++;
        idcount.merge(id, 1, Integer::sum);
    }

    public void print() {
        System.out.printf("pnf-%d, %d-%d%n", count, id, idcount.get(id));
    }
}

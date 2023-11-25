import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        count++;
        if (idcount.containsKey(id)) {
            idcount.put(id, idcount.get(id) + 1);
        } else {
            idcount.put(id, 1);
        }
    }

    public void print() {
        System.out.printf("pinf-%d, %d-%d%n", count, id, idcount.get(id));
    }
}

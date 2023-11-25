import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.util.HashMap;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        count++;
        idcount.merge(id, 1, Integer::sum);
    }

    public void print() {
        System.out.printf("ginf-%d, %d-%d%n", count, id, idcount.get(id));
    }
}
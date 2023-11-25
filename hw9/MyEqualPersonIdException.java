import com.oocourse.spec1.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualPersonIdException(int id) {
        count++;
        if (idcount.containsKey(id)) {
            idcount.put(id, idcount.get(id) + 1);
        } else {
            idcount.put(id, 1);
        }
        this.id = id;
    }

    public void print() {
        System.out.printf("epi-%d, %d-%d%n", count, id, idcount.get(id));
    }
}

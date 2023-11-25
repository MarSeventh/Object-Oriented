import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualPersonIdException(int id) {
        count++;
        idcount.merge(id, 1, Integer::sum);
        this.id = id;
    }

    public void print() {
        System.out.printf("epi-%d, %d-%d%n", count, id, idcount.get(id));
    }
}
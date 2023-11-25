import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualGroupIdException(int id) {
        count++;
        idcount.merge(id, 1, Integer::sum);
        this.id = id;
    }

    public void print() {
        System.out.printf("egi-%d, %d-%d%n", count, id, idcount.get(id));
    }
}
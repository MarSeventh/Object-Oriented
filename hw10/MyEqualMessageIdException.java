import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualMessageIdException(int id) {
        count++;
        idcount.merge(id, 1, Integer::sum);
        this.id = id;
    }

    public void print() {
        System.out.printf("emi-%d, %d-%d%n", count, id, idcount.get(id));
    }
}
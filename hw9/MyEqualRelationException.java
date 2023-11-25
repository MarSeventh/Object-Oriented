import com.oocourse.spec1.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private final int id1;
    private final int id2;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        count++;
        if (id1 <= id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
        if (id1 == id2) {
            if (idcount.containsKey(id1)) {
                idcount.put(id1, idcount.get(id1) + 1);
            } else {
                idcount.put(id1, 1);
            }
        } else {
            if (idcount.containsKey(id1)) {
                idcount.put(id1, idcount.get(id1) + 1);
            } else {
                idcount.put(id1, 1);
            }
            if (idcount.containsKey(id2)) {
                idcount.put(id2, idcount.get(id2) + 1);
            } else {
                idcount.put(id2, 1);
            }
        }
    }

    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d%n", count, id1, idcount.get(id1), id2,
                idcount.get(id2));
    }
}

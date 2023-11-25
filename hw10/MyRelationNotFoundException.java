import com.oocourse.spec2.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int id1;
    private final int id2;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyRelationNotFoundException(int id1, int id2) {
        count++;
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        idcount.merge(id1, 1, Integer::sum);
        idcount.merge(id2, 1, Integer::sum);
    }

    public void print() {
        System.out.printf("rnf-%d, %d-%d, %d-%d%n", count, id1, idcount.get(id1), id2,
                idcount.get(id2));
    }
}
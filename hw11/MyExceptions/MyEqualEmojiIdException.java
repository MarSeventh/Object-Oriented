import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEqualEmojiIdException(int id) {
        count++;
        idcount.merge(id, 1, Integer::sum);
        this.id = id;
    }

    public void print() {
        System.out.printf("eei-%d, %d-%d%n", count, id, idcount.get(id));
    }
}

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private final int id;
    private static int count = 0;
    private static final HashMap<Integer, Integer> idcount = new HashMap<>();

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        count++;
        idcount.merge(id, 1, Integer::sum);
    }

    public void print() {
        System.out.printf("einf-%d, %d-%d%n", count, id, idcount.get(id));
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class ColdEmojiOkTest {
    public int okTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                      ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        final HashMap<Integer, Integer> beforeEmojiList = beforeData.get(0);
        final HashMap<Integer, Integer> beforeMessageList = beforeData.get(1);
        final HashMap<Integer, Integer> afterEmojiList = afterData.get(0);
        final HashMap<Integer, Integer> afterMessageList = afterData.get(1);
        for (int id : beforeEmojiList.keySet()) {
            if (beforeEmojiList.get(id) >= limit && !afterEmojiList.containsKey(id)) {
                return 1;
            }
        }
        for (int id : afterEmojiList.keySet()) {
            if (!beforeEmojiList.containsKey(id) ||
                    !beforeEmojiList.get(id).equals(afterEmojiList.get(id))) {
                return 2;
            }
        }
        int size = 0;
        for (int id : beforeEmojiList.keySet()) {
            if (beforeEmojiList.get(id) >= limit) {
                size++;
            }
        }
        if (afterEmojiList.size() != size) {
            return 3;
        }
        for (int id : beforeMessageList.keySet()) {
            if (beforeMessageList.get(id) != null) {
                if (afterEmojiList.containsKey(beforeMessageList.get(id))) {
                    if (!afterMessageList.containsKey(id) ||
                            !afterMessageList.get(id).equals(beforeMessageList.get(id))) {
                        return 5;
                    }
                }
            }
        }
        for (int id : beforeMessageList.keySet()) {
            if (beforeMessageList.get(id) == null) {
                if (!afterMessageList.containsKey(id) || afterMessageList.get(id) != null) {
                    return 6;
                }
            }
        }
        int messageSize = 0;
        for (int id : beforeMessageList.keySet()) {
            if (beforeMessageList.get(id) == null) {
                messageSize++;
            } else if (afterEmojiList.containsKey(beforeMessageList.get(id))) {
                messageSize++;
            }
        }
        if (afterMessageList.size() != messageSize) {
            return 7;
        }
        if (result != afterEmojiList.size()) {
            return 8;
        }
        return 0;
    }
}

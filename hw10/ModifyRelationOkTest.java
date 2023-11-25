import java.util.HashMap;

public class ModifyRelationOkTest {
    public int test(int id1, int id2, int value,
                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (beforeData.containsKey(id1) && beforeData.containsKey(id2) &&
                beforeData.get(id1).containsKey(id2) && beforeData.get(id2).containsKey(id1)) {
            if (beforeData.size() != afterData.size()) {
                return 1;
            }
            if (!beforeData.keySet().equals(afterData.keySet())) {
                return 2;
            }
            for (int id : beforeData.keySet()) {
                if (id != id1 && id != id2 && !beforeData.get(id).equals(afterData.get(id))) {
                    return 3;
                }
            }
            if (beforeData.get(id1).get(id2) + value > 0) {
                return bigTest(id1, id2, value, beforeData, afterData);
            } else {
                return smallTest(id1, id2, value, beforeData, afterData);
            }
        } else {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    public int bigTest(int id1, int id2, int value,
                       HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                       HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
            return 4;
        }
        if (!afterData.get(id1).get(id2).equals(beforeData.get(id1).get(id2) + value)) {
            return 5;
        }
        if (!afterData.get(id2).get(id1).equals(beforeData.get(id2).get(id1) + value)) {
            return 6;
        }
        if (afterData.get(id1).size() != beforeData.get(id1).size()) {
            return 7;
        }
        if (afterData.get(id2).size() != beforeData.get(id2).size()) {
            return 8;
        }
        if (!afterData.get(id1).keySet().equals(beforeData.get(id1).keySet())) {
            return 9;
        }
        if (!afterData.get(id2).keySet().equals(beforeData.get(id2).keySet())) {
            return 10;
        }
        for (int id : beforeData.get(id1).keySet()) {
            if (id != id2 &&
                    !beforeData.get(id1).get(id).equals(afterData.get(id1).get(id))) {
                return 11;
            }
        }
        for (int id : beforeData.get(id2).keySet()) {
            if (id != id1 &&
                    !beforeData.get(id2).get(id).equals(afterData.get(id2).get(id))) {
                return 12;
            }
        }
        /*if (afterData.get(id1).containsValue(0)) {
            return 13;
        }
        if (afterData.get(id2).containsValue(0)) {
            return 14;
        }*/
        return 0;
    }

    public int smallTest(int id1, int id2, int value,
                         HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                         HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
            return 15;
        }
        if (afterData.get(id1).size() != beforeData.get(id1).size() - 1) {
            return 16;
        }
        if (afterData.get(id2).size() != beforeData.get(id2).size() - 1) {
            return 17;
        }
        /*if (afterData.get(id1).containsValue(0)) {
            return 18;
        }
        if (afterData.get(id2).containsValue(0)) {
            return 19;
        }*/
        HashMap<Integer, Integer> newacq1 = new HashMap<>(beforeData.get(id1));
        HashMap<Integer, Integer> newacq2 = new HashMap<>(beforeData.get(id2));
        newacq1.remove(id2);
        newacq2.remove(id1);
        if (!newacq1.equals(afterData.get(id1))) {
            return 20;
        }
        if (!newacq2.equals(afterData.get(id2))) {
            return 21;
        }
        return 0;
    }
}

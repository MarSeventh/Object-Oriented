import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private int qtsum;
    private final ArrayList<HashMap<Integer, Person>> blocks;

    public MyNetwork() {
        this.people = new HashMap<>();
        this.qtsum = 0;
        this.blocks = new ArrayList<>();
    }

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        return people.getOrDefault(id, null);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            HashMap<Integer, Person> newblock = new HashMap<>();
            newblock.put(person.getId(), person);
            blocks.add(newblock);
        }
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            MyPerson myPerson1 = (MyPerson) getPerson(id1);
            MyPerson myPerson2 = (MyPerson) getPerson(id2);
            myPerson1.addAcq(myPerson2, value);
            myPerson2.addAcq(myPerson1, value);
            manageTriple(id1, id2);
            manageBlock(id1, id2);
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }

    public void manageBlock(int id1, int id2) {
        HashMap<Integer, Person> block1 = new HashMap<>();
        for (HashMap<Integer, Person> block : blocks) {
            if (block.containsKey(id1)) {
                block1 = block;
                break;
            }
        }
        if (!block1.containsKey(id2)) {
            HashMap<Integer, Person> block2 = new HashMap<>();
            for (HashMap<Integer, Person> block : blocks) {
                if (block.containsKey(id2)) {
                    block2 = block;
                    blocks.remove(block);
                    break;
                }
            }
            block1.putAll(block2);
        }
    }

    public void manageTriple(int id1, int id2) {
        for (int id : people.keySet()) {
            if (id == id1 || id == id2) {
                continue;
            }
            if (people.get(id).isLinked(getPerson(id1))
                    && people.get(id).isLinked(getPerson(id2))) {
                qtsum++;
            }
        }
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            return getPerson(id1).queryValue(getPerson(id2));
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (contains(id1) && contains(id2)) {
            /*HashMap<Integer, Person> searched = new HashMap<>();
            return dfsCircle(id1, id2, searched);*/
            for (HashMap<Integer, Person> block : blocks) {
                if (block.containsKey(id1) && block.containsKey(id2)) {
                    return true;
                }
            }
            return false;
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }

    public boolean dfsCircle(int start, int des, HashMap<Integer, Person> searched) {
        if (start == des) {
            return true;
        }
        searched.put(start, getPerson(start));
        MyPerson myPerson1 = (MyPerson) getPerson(start);
        ArrayList<Person> nextnodes = myPerson1.getAcqArray();
        for (Person person : nextnodes) {
            if (!searched.containsKey(person.getId())) {
                if (dfsCircle(person.getId(), des, searched)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int queryBlockSum() {
        /*ArrayList<Integer> allid = new ArrayList<>(people.keySet());
        int block = 0;
        for (int i = 0; i < allid.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < i; j++) {
                try {
                    if (isCircle(allid.get(i), allid.get(j))) {
                        flag = false;
                    }
                } catch (PersonIdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (flag) {
                block++;
            }
        }
        return block;*/
        return blocks.size();
    }

    public int queryTripleSum() {
        /*int trip = 0;
        ArrayList<Integer> allid = new ArrayList<>(people.keySet());
        for (int i = 0; i < allid.size(); i++) {
            for (int j = i + 1; j < allid.size(); j++) {
                for (int z = j + 1; z < allid.size(); z++) {
                    Person person1 = getPerson(allid.get(i));
                    Person person2 = getPerson(allid.get(j));
                    Person person3 = getPerson(allid.get(z));
                    if (person1.isLinked(person2) && person2.isLinked(person3)
                            && person3.isLinked(person1)) {
                        trip++;
                    }
                }
            }
        }
        return trip;*/
        return qtsum;
    }

    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        boolean assignflag = true;
        boolean resultflag = true;
        if (!beforeData.equals(afterData)) {
            assignflag = false;
        }
        MyNetwork network = new MyNetwork();
        ArrayList<Integer> added = new ArrayList<>();
        for (int id : beforeData.keySet()) {
            MyPerson person = new MyPerson(id, "default_name", 0);
            try {
                network.addPerson(person);
            } catch (EqualPersonIdException e) {
                throw new RuntimeException(e);
            }
        }
        for (int id : beforeData.keySet()) {
            for (int acqid : beforeData.get(id).keySet()) {
                if (added.contains(acqid)) {
                    continue;
                }
                try {
                    network.addRelation(id, acqid, beforeData.get(id).get(acqid));
                } catch (EqualRelationException | PersonIdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            added.add(id);
        }
        if (network.queryTripleSum() != result) {
            resultflag = false;
        }
        return assignflag && resultflag;
    }
}

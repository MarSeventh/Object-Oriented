import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;

    private final HashMap<Integer, Person> acquaintance;
    private int bestvalue;
    private int bestid;
    private final HashMap<Integer, Integer> value;
    private int socialValue;
    private ArrayList<Message> messages;
    private int money;

    private int leastmoments;
    private boolean lvalid;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.bestvalue = -1;
        this.bestid = -1;
        this.value = new HashMap<>();
        this.socialValue = 0;
        this.messages = new ArrayList<>();
        this.leastmoments = -1;
        this.lvalid = false;
    }

    public int getLeastMoments(HashMap<Integer, Person> block) {
        if (lvalid) {
            return leastmoments;
        } else {
            LeastMoments lm = new LeastMoments(block, this);
            leastmoments = lm.getLeastMoments();
            lvalid = true;
            return leastmoments;
        }
    }

    public void breakLeastValid() {
        this.lvalid = false;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public ArrayList<Person> getAcqArray() {
        return new ArrayList<>(acquaintance.values());
    }

    public int getBestAcquaintance() {
        return bestid;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Person) {
            return ((Person) obj).getId() == id;
        } else {
            return false;
        }
    }

    public boolean isLinked(Person person) {
        if (person.getId() == id) {
            return true;
        } else {
            return acquaintance.containsKey(person.getId());
        }
    }

    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    public void addAcq(Person person, int value1) {
        acquaintance.put(person.getId(), person);
        value.put(person.getId(), value1);
        if (value1 > bestvalue) {
            bestvalue = value1;
            bestid = person.getId();
        } else if (value1 == bestvalue && person.getId() < bestid) {
            bestid = person.getId();
        }
    }

    public void modifyAcq(Person person, int val) {
        if (queryValue(person) + val > 0) {
            value.merge(person.getId(), val, Integer::sum);
        } else {
            value.remove(person.getId());
            acquaintance.remove(person.getId());
        }
        if (person.getId() == bestid && val >= 0) {
            bestvalue += val;
        } else if (person.getId() == bestid && val < 0) {
            findBestAcquaintance();
        } else if (person.getId() != bestid && val > 0) {
            if (value.get(person.getId()) > bestvalue) {
                bestvalue = value.get(person.getId());
                bestid = person.getId();
            } else if (value.get(person.getId()) == bestvalue) {
                if (person.getId() < bestid) {
                    bestid = person.getId();
                }
            }
        }
    }

    public void findBestAcquaintance() {
        bestid = -1;
        bestvalue = -1;
        for (int id : value.keySet()) {
            if (value.get(id) >= bestvalue) {
                if (value.get(id) > bestvalue) {
                    bestvalue = value.get(id);
                    bestid = id;
                } else if (id < bestid) {
                    bestid = id;
                }
            }
        }
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages1) {
        messages = messages1;
    }

    public List<Message> getReceivedMessages() {
        ArrayList<Message> receive = new ArrayList<>();
        if (messages.size() < 5) {
            receive.addAll(messages);
        } else {
            for (int i = 0; i < 5; i++) {
                receive.add(messages.get(i));
            }
        }
        return receive;
    }

    public void addMoney(int num) {
        money = money + num;
    }

    public int getMoney() {
        return money;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
}
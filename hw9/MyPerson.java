import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;

    private final HashMap<Integer, Person> acquaintance;
    private final HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    public ArrayList<Person> getAcqArray() {
        return new ArrayList<>(acquaintance.values());
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
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

}

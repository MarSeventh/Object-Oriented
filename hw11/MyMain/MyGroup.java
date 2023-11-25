import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyGroup implements Group {
    private final int id;
    private int valuesum;
    private int agesum;
    private int agemean;
    private int agevar;
    private final HashMap<Integer, Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.valuesum = 0;
        this.agesum = 0;
        this.agemean = 0;
        this.agevar = 0;
        this.people = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            return ((Group) obj).getId() == id;
        } else {
            return false;
        }
    }

    public void addSocialValue(int num) {
        for (int id : people.keySet()) {
            people.get(id).addSocialValue(num);
        }
    }

    public void manageAge(int age, boolean ap) {
        if (ap) {
            agesum += age;
            agemean = agesum / people.size();
            int agevarsum = 0;
            for (int id : people.keySet()) {
                agevarsum += Math.pow(people.get(id).getAge() - agemean, 2);
            }
            agevar = agevarsum / people.size();
        } else {
            agesum -= age;
            if (people.size() != 0) {
                agemean = agesum / people.size();
            } else {
                agemean = 0;
            }
            int agevarsum = 0;
            for (int id : people.keySet()) {
                agevarsum += Math.pow(people.get(id).getAge() - agemean, 2);
            }
            if (people.size() != 0) {
                agevar = agevarsum / people.size();
            } else {
                agevar = 0;
            }
        }
    }

    public void manageValue(Person person, int value, int opcode) {
        //opcode == 0 add person; opcode == 1 delete person;
        //opcode == 2 modify relation(add or change, use value);
        switch (opcode) {
            case 0:
                MyPerson myperson = (MyPerson) person;
                ArrayList<Person> acqarray = myperson.getAcqArray();
                for (Person person1 : acqarray) {
                    if (people.containsKey(person1.getId())) {
                        valuesum += 2 * person.queryValue(person1);
                    }
                }
                break;
            case 1:
                MyPerson myperson1 = (MyPerson) person;
                ArrayList<Person> acqarray1 = myperson1.getAcqArray();
                for (Person person1 : acqarray1) {
                    if (people.containsKey(person1.getId())) {
                        valuesum -= 2 * person.queryValue(person1);
                    }
                }
                break;
            case 2:
                valuesum += 2 * value;
                break;
            default:
                break;
        }
    }

    public void addPerson(Person person) {
        people.put(person.getId(), person);
        manageAge(person.getAge(), true);
        manageValue(person, 0, 0);
    }

    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    public int getValueSum() {
        return valuesum;
    }

    public int getAgeMean() {
        return agemean;
    }

    public int getAgeVar() {
        return agevar;
    }

    public void delPerson(Person person) {
        people.remove(person.getId());
        manageAge(person.getAge(), false);
        manageValue(person, 0, 1);
    }

    public int getSize() {
        return people.size();
    }

    public void sendRedEnvelope(RedEnvelopeMessage message) {
        int envelope = message.getMoney() / getSize();
        message.getPerson1().addMoney(-envelope * (getSize() - 1));
        for (int id : people.keySet()) {
            if (id != message.getPerson1().getId()) {
                people.get(id).addMoney(envelope);
            }
        }
    }
}
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 0;
        this.person1 = messagePerson1;
        this.person2 = messagePerson2;
        this.group = null;
    }

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.type = 1;
        this.person1 = messagePerson1;
        this.person2 = null;
        this.group = messageGroup;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public Group getGroup() {
        return group;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            return ((Message) obj).getId() == id;
        } else {
            return false;
        }
    }
}

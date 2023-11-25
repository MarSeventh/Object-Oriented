import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {
    private final String string;

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.string = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
        this.string = noticeString;
    }

    public String getString() {
        return string;
    }
}

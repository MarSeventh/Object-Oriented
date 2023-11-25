import java.util.ArrayList;

public class WaitList {
    private ArrayList<Person> waitList;
    private boolean isEnd;

    public WaitList() {
        this.waitList = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addPerson(Person person) {
        waitList.add(person);
        notifyAll();
    }

    public synchronized void setEnd(boolean flag) {
        isEnd = flag;
        notifyAll();
    }

    public ArrayList<Person> getWaitList() {
        return waitList;
    }

    public int sum() {
        return waitList.size();
    }

    public void setWaitList(ArrayList<Person> list) {
        waitList = list;
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return waitList.isEmpty();
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public Person firstPerson() {
        return waitList.get(0);
    }

    public synchronized Person getOnePerson() {
        if (waitList.isEmpty() && !isEnd) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (waitList.isEmpty()) {
            return null;
        }
        Person person = waitList.get(0);
        waitList.remove(0);
        notifyAll();
        return person;
    }
}

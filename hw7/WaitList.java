import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WaitList {
    private ArrayList<Person> waitList;
    private boolean isEnd;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
        lock.readLock().lock();
        try {
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int sum() {
        lock.readLock().lock();
        try {
            return waitList.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setWaitList(ArrayList<Person> list) {
        waitList = list;
    }

    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return waitList.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isEnd() {
        lock.readLock().lock();
        try {
            return isEnd;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Person firstPerson() {
        lock.readLock().lock();
        try {
            return waitList.get(0);
        } finally {
            lock.readLock().lock();
        }
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

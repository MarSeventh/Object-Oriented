import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Person {
    private final int id;
    private int start;
    private final int des;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Person(int id, int start, int des) {
        this.id = id;
        this.start = start;
        this.des = des;
    }

    public void setStart(int newstart) {
        lock.writeLock().lock();
        try {
            start = newstart;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getId() {
        lock.readLock().lock();
        try {
            return id;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getStart() {
        lock.readLock().lock();
        try {
            return start;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getDes() {
        lock.readLock().lock();
        try {
            return des;
        } finally {
            lock.readLock().unlock();
        }
    }
}

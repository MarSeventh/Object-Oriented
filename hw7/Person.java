import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Person {
    private final int id;
    private int start;
    private final int des;
    private int direction;
    private int transfer;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Person(int id, int start, int des) {
        this.id = id;
        this.start = start;
        this.des = des;
        this.direction = des - start;
        this.transfer = -1;
    }

    public void setStart(int newstart) {
        lock.writeLock().lock();
        try {
            start = newstart;
            direction = des - start;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setTransfer(int trans) {
        lock.writeLock().lock();
        try {
            transfer = trans;
            if (transfer != -1) {
                direction = transfer - start;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isTransFloor(int floor) {
        lock.readLock().lock();
        try {
            return transfer == floor;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getTransfer() {
        lock.readLock().lock();
        try {
            return transfer;
        } finally {
            lock.readLock().unlock();
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

    public int getDirection() {
        lock.readLock().lock();
        try {
            if (direction > 0) {
                return 1;
            } else if (direction < 0) {
                return -1;
            } else {
                return 0;
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}

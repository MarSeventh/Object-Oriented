import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Manager extends Thread {
    private final WaitList waitmap;
    private final ArrayList<WaitList> waitLists;
    private final ArrayList<Elevator> elevators;
    private boolean isEnd;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Manager(WaitList waitmap) {
        this.waitmap = waitmap;
        this.waitLists = new ArrayList<>();
        this.elevators = new ArrayList<>();
    }

    public void addElevators(Elevator elevator) {
        lock.writeLock().lock();
        try {
            elevators.add(elevator);
            waitLists.add(elevator.getWaitList());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setEnd(boolean flag) {
        lock.writeLock().lock();
        try {
            isEnd = flag;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeElevators(int id) {
        lock.writeLock().lock();
        try {
            for (Elevator elevator : elevators) {
                if (elevator.getID() == id) {
                    waitLists.remove(elevator.getWaitList());
                    elevators.remove(elevator);
                    break;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isFinished() {
        int flag = 1;
        if (!waitmap.isEmpty()) {
            flag = 0;
        }
        lock.readLock().lock();
        try {
            for (Elevator elevator : elevators) {
                if (!elevator.getWaitList().isEmpty() || elevator.getInnum() != 0) {
                    flag = 0;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        if (flag == 1 && isEnd) {
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        while (true) {
            synchronized (waitmap) {
                if (isFinished()) {
                    waitmap.setEnd(true);
                    for (WaitList waitList : waitLists) {
                        waitList.setEnd(true);
                    }
                    waitmap.notifyAll();
                    return;
                }
            }
            Person person = waitmap.getOnePerson();
            if (person == null) {
                continue;
            }
            getWaitList(person).addPerson(person);
        }
    }

    public WaitList getWaitList(Person person) {
        int des = person.getDes();
        int start = person.getStart();
        int distance = 20;
        WaitList waitList = null;
        lock.readLock().lock();
        try {
            for (Elevator elevator : elevators) {
                int dir = elevator.getDirection();
                int pos = elevator.getPos();
                int innum = elevator.getInnum();
                WaitList waitList1 = elevator.getWaitList();
                synchronized (waitList1) {
                    if ((des - start) * dir >= 0 && innum < 6
                            && (waitList1.sum() < 6 - innum)) {
                        if (dir == 0) {
                            if (Math.abs(pos - start) < distance) {
                                distance = Math.abs(pos - start);
                                waitList = waitList1;
                            }
                        } else if (dir > 0) {
                            if (pos <= start && Math.abs(pos - start) < distance) {
                                distance = Math.abs(pos - start);
                                waitList = waitList1;
                            }
                        } else {
                            if (pos >= start && Math.abs(pos - start) < distance) {
                                distance = Math.abs(pos - start);
                                waitList = waitList1;
                            }
                        }
                    }
                }
            }
            if (waitList == null) {
                int sum = 1000;
                for (Elevator elevator : elevators) {
                    int innum = elevator.getInnum();
                    WaitList waitList1 = elevator.getWaitList();
                    synchronized (waitList1) {
                        int outnum = waitList1.sum();
                        if (innum + outnum < sum) {
                            sum = innum + outnum;
                            waitList = waitList1;
                        }
                    }
                }
            }
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }
}

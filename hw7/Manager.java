import java.util.ArrayList;
import java.util.HashMap;
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
                    lock.readLock().lock();
                    for (WaitList waitList : waitLists) {
                        waitList.setEnd(true);
                    }
                    lock.readLock().unlock();
                    waitmap.notifyAll();
                    return;
                }
            }
            Person person = waitmap.getOnePerson();
            if (person == null) {
                continue;
            }
            lock.readLock().lock();
            getWaitList(person).addPerson(person);
            lock.readLock().unlock();
        }
    }

    public WaitList getWaitList(Person person) {
        WaitList waitList;
        lock.readLock().lock();
        try {
            waitList = noTransAndFastSearch(person);
            if (waitList == null) {
                waitList = noTransSearch(person);
            }
            if (waitList == null) {
                waitList = transSearch(person);
            }
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public WaitList noTransAndFastSearch(Person person) {
        lock.readLock().lock();
        try {
            WaitList waitList = null;
            int des = person.getDes();
            int start = person.getStart();
            int distance = 200;
            for (Elevator elevator : elevators) {
                int dir = elevator.getDirection();
                int pos = elevator.getPos();
                int innum = elevator.getInnum();
                int inmax = elevator.getInmax();
                WaitList waitList1 = elevator.getWaitList();
                synchronized (waitList1) {
                    int samedir = 0;
                    int newdistance = 200;
                    if (innum == 0 && !waitList1.isEmpty()) {
                        Person person1 = waitList1.getWaitList().get(0);
                        if ((start - person1.getStart()) * (start - person1.getDes()) > 0) {
                            samedir = -1;
                        } else {
                            samedir = (des - start) * person1.getDirection();
                            newdistance = Math.abs(person1.getStart() - pos)
                                    + Math.abs(person1.getStart() - start);
                        }
                    } else {
                        samedir = (des - start) * dir;
                        newdistance = Math.abs(start - pos);
                    }
                    if (samedir >= 0 && (innum + waitList1.sum()) < inmax
                            && (start - pos) * dir >= 0 && newdistance < distance &&
                            elevator.isAccessible(des) && elevator.isAccessible(start)) {
                        if (innum == 0 && !waitList1.isEmpty()) {
                            Person person1 = waitList1.getWaitList().get(0);
                            if (canNotBring(des, start, pos, person1)) {
                                continue;
                            }
                        }
                        distance = newdistance;
                        waitList = waitList1;
                    }
                }
            }
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public WaitList noTransSearch(Person person) {
        lock.readLock().lock();
        try {
            WaitList waitList = null;
            int des = person.getDes();
            int start = person.getStart();
            int sum = 1000;
            for (Elevator elevator : elevators) {
                WaitList waitList1 = elevator.getWaitList();
                int innum = elevator.getInnum();
                synchronized (waitList1) {
                    int outnum = waitList1.sum();
                    if ((innum + outnum < sum) && elevator.isAccessible(start)
                            && elevator.isAccessible(des)) {
                        sum = innum + outnum;
                        waitList = waitList1;
                    }
                }
            }
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public WaitList transSearch(Person person) {
        lock.readLock().lock();
        try {
            ArrayList<HashMap<Elevator, Integer>> alllines = new ArrayList<>();
            HashMap<Elevator, Integer> line = new HashMap<>();
            searchAllLines(alllines, line, person.getStart(), person.getDes());
            WaitList waitList;
            waitList = transSearchFind(person, alllines);
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public WaitList transSearchFind(Person person, ArrayList<HashMap<Elevator, Integer>> alllines) {
        lock.readLock().lock();
        try {
            WaitList waitList = null;
            int trans = -1;
            int count = 1000;
            int sum = 1000;
            int distance = 200;
            for (HashMap<Elevator, Integer> li : alllines) {
                Elevator ele = getFirstEle(li, person);
                int innum = ele.getInnum();
                int outnum = ele.getWaitList().sum();
                int pos = ele.getPos();
                int des = li.get(ele) % 100;
                int start = person.getStart();
                boolean change = false;
                if (li.size() < count) {
                    change = true;
                    count = li.size();
                    distance = Math.abs(start - pos);
                } else if (li.size() == count) {
                    int flag = 0;
                    int nearflag = isNearestEle(distance, person, li, ele);
                    if (nearflag != 0) {
                        if (innum == 0 && !ele.getWaitList().isEmpty() && canNotBring(des, start,
                                pos, ele.getWaitList().getWaitList().get(0))) {
                            flag = 1;
                        }
                        if (flag == 0) {
                            change = true;
                            synchronized (waitList) {
                                if (nearflag == 1 && !waitList.isEmpty()) {
                                    Person person1 = waitList.getWaitList().get(0);
                                    distance = Math.abs(person1.getStart() - pos)
                                            + Math.abs(person1.getStart() - start);
                                } else {
                                    distance = Math.abs(start - pos);
                                }
                            }
                            flag = 2;
                        }
                    }
                    if (innum + outnum < sum && flag != 2) {
                        change = true;
                        distance = Math.abs(start - pos);
                    }
                }
                if (change) {
                    waitList = ele.getWaitList();
                    trans = li.get(ele) % 100;
                    sum = innum + outnum;
                }
            }
            person.setTransfer(trans);
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Elevator getFirstEle(HashMap<Elevator, Integer> line, Person person) {
        Elevator ele = null;
        for (Elevator elevator : line.keySet()) {
            if (line.get(elevator) / 100 == person.getStart()) {
                ele = elevator;
                break;
            }
        }
        return ele;
    }

    public int isNearestEle(int distance, Person person,
                            HashMap<Elevator, Integer> li, Elevator ele) {
        lock.readLock().lock();
        try {
            int flag = 0;
            int innum = ele.getInnum();
            int outnum = ele.getWaitList().sum();
            int pos = ele.getPos();
            int dir = ele.getDirection();
            int inmax = ele.getInmax();
            int start = person.getStart();
            int des = li.get(ele) % 100;
            int samedir = 0;
            int newdistance = 200;
            if (innum == 0 && outnum > 0) {
                Person person1 = ele.getWaitList().getWaitList().get(0);
                if ((start - person1.getStart()) * (start - person1.getDes()) > 0) {
                    samedir = -1;
                } else {
                    samedir = (des - start) * person1.getDirection();
                    newdistance = Math.abs(person1.getStart() - pos)
                            + Math.abs(person1.getStart() - start);
                    flag = 1;
                }
            } else {
                samedir = (des - start) * dir;
                newdistance = Math.abs(start - pos);
                flag = 2;
            }
            if (samedir >= 0
                    && (dir * (start - pos)) >= 0
                    && (innum + outnum) < inmax && newdistance < distance) {
                return flag;
            } else {
                return 0;
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean canNotBring(int des, int start, int pos, Person person1) {
        lock.readLock().lock();
        try {
            boolean flag = false;
            if ((des - start)
                    * person1.getDirection() < 0) {
                if (!((des - pos) * (start - pos) >= 0
                        && (des - pos) * (person1.getStart() - pos) >= 0
                        && Math.abs(des - pos) <=
                        Math.abs(person1.getStart() - pos))) {
                    flag = true;
                }
            }
            return flag;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void searchAllLines(ArrayList<HashMap<Elevator, Integer>> alllines,
                               HashMap<Elevator, Integer> line, int start, int des) {
        lock.readLock().lock();
        try {
            if (start == des) {
                alllines.add(line);
                return;
            }
            for (Elevator elevator : elevators) {
                if (!line.containsKey(elevator) && elevator.isAccessible(start)) {
                    for (int i = 1; i <= 11; i++) {
                        if (i != start && elevator.isAccessible(i)) {
                            HashMap<Elevator, Integer> newline = new HashMap<>(line);
                            newline.put(elevator, 100 * start + i);
                            searchAllLines(alllines, newline, i, des);
                        }
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}

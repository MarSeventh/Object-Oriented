import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Elevator extends Thread {
    private ArrayList<Person> inlist;
    private final WaitList waitList;
    private int innum;
    private final int id;
    private int pos;
    private final int inmax;
    private final long movetime;
    private int direction;
    private int inflag;
    private int outflag;
    private final Strategy strategy;
    private boolean mataining;
    private final int access;
    private final Manager manager;
    private final WaitList waitmap;
    private final CountController countController;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Elevator(int id, Manager manager, WaitList waitmap, CountController countController) {
        this.inlist = new ArrayList<>();
        this.waitList = new WaitList();
        this.innum = 0;
        this.id = id;
        this.pos = 1;
        this.inmax = 6;
        this.movetime = 401;
        this.direction = 0;
        this.inflag = 0;
        this.outflag = 0;
        this.mataining = false;
        this.access = 0b111_1111_1111;
        this.strategy = new Strategy(inlist, waitList, pos, direction, this.inmax, access);
        this.manager = manager;
        this.waitmap = waitmap;
        this.countController = countController;
    }

    public Elevator(int id, int pos, int inmax, double movetime, int access,
                    Manager manager, WaitList waitmap, CountController countController) {
        this.inlist = new ArrayList<>();
        this.waitList = new WaitList();
        this.innum = 0;
        this.id = id;
        this.pos = pos;
        this.inmax = inmax;
        this.movetime = Math.round(movetime * 1000 + 1);
        this.direction = 0;
        this.inflag = 0;
        this.outflag = 0;
        this.mataining = false;
        this.access = access;
        this.strategy = new Strategy(inlist, waitList, this.pos, direction,
                this.inmax, this.access);
        this.manager = manager;
        this.waitmap = waitmap;
        this.countController = countController;
    }

    public void setMataining(boolean matain) {
        lock.writeLock().lock();
        try {
            mataining = matain;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public WaitList getWaitList() {
        lock.readLock().lock();
        try {
            return waitList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getPos() {
        lock.readLock().lock();
        try {
            return pos;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getDirection() {
        lock.readLock().lock();
        try {
            return direction;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getInnum() {
        lock.readLock().lock();
        try {
            return innum;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getID() {
        lock.readLock().lock();
        try {
            return id;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isAccessible(int floor) {
        lock.readLock().lock();
        try {
            return (access & (1 << (floor - 1))) != 0;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getInmax() {
        lock.readLock().lock();
        try {
            return inmax;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (mataining) {
                manager.removeElevators(id);
                kickout();
                synchronized (waitmap) {
                    waitmap.notifyAll();
                }
                return;
            }
            synchronized (waitList) {
                if (inlist.isEmpty() && waitList.isEmpty() && waitList.isEnd()) {
                    return;
                }
                waitList.notifyAll();
            }
            exchange();
            moveon();
        }
    }

    public void moveon() {
        try {
            strategy.setDirection(direction);
            direction = strategy.getDirection();
            if (direction == 0) {
                synchronized (waitList) {
                    if (!waitList.isEnd()) {
                        synchronized (waitmap) {
                            waitmap.notifyAll();
                        }
                        waitList.wait();
                    }
                    waitList.notifyAll();
                }
            } else if (direction == 1) {
                pos++;
                sleep(movetime);
                TimableOutput.println("ARRIVE-" + pos + "-" + id);
            } else {
                pos--;
                sleep(movetime);
                TimableOutput.println("ARRIVE-" + pos + "-" + id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void kickout() {
        try {
            if (!inlist.isEmpty()) {
                countController.wantExchange(pos,0);
                TimableOutput.println("OPEN-" + pos + "-" + id);
                for (Person person : inlist) {
                    TimableOutput.println("OUT-" + person.getId() + "-" +
                            pos + "-" + id);
                    innum--;
                    if (person.getDes() != pos) {
                        person.setStart(pos);
                        waitmap.addPerson(person);
                    }
                }
                synchronized (waitList) {
                    if (!waitList.isEmpty()) {
                        for (Person person : waitList.getWaitList()) {
                            waitmap.addPerson(person);
                        }
                    }
                    waitList.notifyAll();
                }
                sleep(400);
                TimableOutput.println("CLOSE-" + pos + "-" + id);
                countController.endExchange(pos,0);
            } else {
                synchronized (waitList) {
                    if (!waitList.isEmpty()) {
                        for (Person person : waitList.getWaitList()) {
                            waitmap.addPerson(person);
                        }
                    }
                    waitList.notifyAll();
                }
            }
            TimableOutput.println("MAINTAIN_ABLE-" + id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exchange() {
        try {
            strategy.setDirection(direction);
            strategy.setPos(pos);
            outflag = strategy.getOut();
            direction = strategy.getDirection();
            int flag = 0;
            if (outflag == 1) {
                flag = 1;
                countController.wantExchange(pos, 0);
                TimableOutput.println("OPEN-" + pos + "-" + id);
                exchangeOut();
                sleep(200);
            }
            strategy.setDirection(direction);
            direction = strategy.getDirection();
            inflag = strategy.getIn();
            if (inflag == 1) {
                if (flag == 0) {
                    countController.wantExchange(pos, 1);
                    TimableOutput.println("OPEN-" + pos + "-" + id);
                    sleep(200);
                    flag = 2;
                }
                exchangeIn();
            }
            if (flag != 0) {
                sleep(200);
                getIn();
                TimableOutput.println("CLOSE-" + pos + "-" + id);
                if (flag == 1) {
                    countController.endExchange(pos, 0);
                } else {
                    countController.endExchange(pos, 1);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exchangeOut() {
        ArrayList<Person> newinlist = new ArrayList<>();
        for (Person person : inlist) {
            if (person.getDes() == pos) {
                TimableOutput.println("OUT-" + person.getId() + "-" +
                        pos + "-" + id);
                innum--;
            } else if (person.isTransFloor(pos)) {
                TimableOutput.println("OUT-" + person.getId() + "-" +
                        pos + "-" + id);
                innum--;
                person.setStart(pos);
                person.setTransfer(-1);
                waitmap.addPerson(person);
            } else {
                newinlist.add(person);
            }
        }
        inlist = newinlist;
        strategy.setInlist(newinlist);
    }

    public void exchangeIn() {
        synchronized (waitList) {
            ArrayList<Person> newwaitlist = new ArrayList<>();
            for (Person person : waitList.getWaitList()) {
                if (((person.getDirection() * direction) >= 0)
                        && innum < inmax && person.getStart() == pos) {
                    TimableOutput.println("IN-" + person.getId() + "-" +
                            pos + "-" + id);
                    inlist.add(person);
                    innum++;
                } else {
                    newwaitlist.add(person);
                }
            }
            waitList.setWaitList(newwaitlist);
            waitList.notifyAll();
        }
    }

    public void getIn() {
        strategy.setDirection(direction);
        direction = strategy.getDirection();
        inflag = strategy.getIn();
        if (inflag == 1) {
            synchronized (waitList) {
                ArrayList<Person> newwaitlist = new ArrayList<>();
                for (Person person : waitList.getWaitList()) {
                    if (((person.getDirection() * direction) >= 0)
                            && innum < inmax && person.getStart() == pos) {
                        TimableOutput.println("IN-" + person.getId() + "-" +
                                pos + "-" + id);
                        inlist.add(person);
                        innum++;
                    } else {
                        newwaitlist.add(person);
                    }
                }
                waitList.setWaitList(newwaitlist);
                waitList.notifyAll();
            }
        }
    }
}
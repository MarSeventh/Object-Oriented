import com.oocourse.elevator2.TimableOutput;

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
    private final Manager manager;
    private final WaitList waitmap;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Elevator(int id, Manager manager, WaitList waitmap) {
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
        this.strategy = new Strategy(inlist, waitList, pos, direction, this.inmax);
        this.mataining = false;
        this.manager = manager;
        this.waitmap = waitmap;
    }

    public Elevator(int id, int pos, int inmax, double movetime,
                    Manager manager, WaitList waitmap) {
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
        this.strategy = new Strategy(inlist, waitList, this.pos, direction, this.inmax);
        this.mataining = false;
        this.manager = manager;
        this.waitmap = waitmap;
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

    @Override
    public void run() {
        try {
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
                strategy.setDirection(direction);
                strategy.setPos(pos);
                outflag = strategy.getOut();
                direction = strategy.getDirection();
                exchange();
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
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void kickout() {
        try {
            if (!inlist.isEmpty()) {
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
            int flag = 0;
            if (outflag == 1) {
                flag = 1;
                TimableOutput.println("OPEN-" + pos + "-" + id);
                ArrayList<Person> newinlist = new ArrayList<>();
                for (Person person : inlist) {
                    if (person.getDes() == pos) {
                        TimableOutput.println("OUT-" + person.getId() + "-" +
                                pos + "-" + id);
                        innum--;
                    } else {
                        newinlist.add(person);
                    }
                }
                inlist = newinlist;
                strategy.setInlist(newinlist);
                sleep(200);
            }
            strategy.setDirection(direction);
            direction = strategy.getDirection();
            inflag = strategy.getIn();
            if (inflag == 1) {
                if (flag == 0) {
                    TimableOutput.println("OPEN-" + pos + "-" + id);
                    sleep(200);
                    flag = 1;
                }
                synchronized (waitList) {
                    ArrayList<Person> newwaitlist = new ArrayList<>();
                    for (Person person : waitList.getWaitList()) {
                        if (((person.getDes() - person.getStart()) * direction) >= 0
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
            if (flag == 1) {
                sleep(200);
                getIn();
                TimableOutput.println("CLOSE-" + pos + "-" + id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                    if (((person.getDes() - person.getStart()) * direction) >= 0
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
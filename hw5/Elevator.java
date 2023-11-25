import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;

public class Elevator extends Thread {
    private ArrayList<Person> inlist;
    private final WaitList waitList;
    private int innum;
    private final int id;
    private int pos;
    private int direction;
    private int inflag;
    private int outflag;
    private final Strategy strategy;

    public Elevator(int id) {
        this.inlist = new ArrayList<>();
        this.waitList = new WaitList();
        this.innum = 0;
        this.id = id;
        this.pos = 1;
        this.direction = 0;
        this.inflag = 0;
        this.outflag = 0;
        this.strategy = new Strategy(inlist, waitList, pos, direction);
    }

    public WaitList getWaitList() {
        return waitList;
    }

    public int getPos() {
        return pos;
    }

    public int getDirection() {
        return direction;
    }

    public int getInnum() {
        return innum;
    }

    @Override
    public void run() {
        try {
            while (true) {
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
                            waitList.wait();
                        }
                        waitList.notifyAll();
                    }
                } else if (direction == 1) {
                    pos++;
                    sleep(400);
                    TimableOutput.println("ARRIVE-" + pos + "-" + id);
                } else {
                    pos--;
                    sleep(400);
                    TimableOutput.println("ARRIVE-" + pos + "-" + id);
                }
            }
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
                                && innum < 6 && person.getStart() == pos) {
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
                            && innum < 6 && person.getStart() == pos) {
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
import java.util.ArrayList;

public class Manager extends Thread {
    private final WaitList waitmap;
    private final ArrayList<WaitList> waitLists;
    private final ArrayList<Elevator> elevators;
    private int sum;

    public Manager(WaitList waitmap) {
        this.waitmap = waitmap;
        this.waitLists = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.sum = 0;
    }

    public void addWaitLists(WaitList waitList) {
        waitLists.add(waitList);
    }

    public void addElevators(Elevator elevator) {
        elevators.add(elevator);
    }

    public void run() {
        while (true) {
            synchronized (waitmap) {
                if (waitmap.isEmpty() && waitmap.isEnd()) {
                    for (WaitList waitList : waitLists) {
                        waitList.setEnd(true);
                    }
                    return;
                }
                waitmap.notifyAll();
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
            waitList = waitLists.get(sum % 6);
        }
        sum++;
        return waitList;
    }
}

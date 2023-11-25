import java.util.ArrayList;

public class Strategy {
    private ArrayList<Person> inlist;
    private final WaitList waitList;
    private int pos;
    private int direction;

    private final int inmax;

    public Strategy(ArrayList<Person> inlist, WaitList waitList,
                    int pos, int direction, int inmax) {
        this.inlist = inlist;
        this.waitList = waitList;
        this.pos = pos;
        this.direction = direction;
        this.inmax = inmax;
    }

    public void setInlist(ArrayList<Person> list) {
        inlist = list;
    }

    public void setPos(int position) {
        pos = position;
    }

    public void setDirection(int dir) {
        synchronized (waitList) {
            if (!inlist.isEmpty()) {
                direction = dir;
            } else if (!waitList.isEmpty()) {
                Person person = waitList.firstPerson();
                int des = person.getStart();
                if (des < pos) {
                    direction = -1;
                } else if (des > pos) {
                    direction = 1;
                } else {
                    if ((person.getDes() - person.getStart()) > 0) {
                        direction = 1;
                    } else {
                        direction = -1;
                    }
                }
            } else {
                direction = 0;
            }
            waitList.notifyAll();
        }
    }

    public int getDirection() {
        return direction;
    }

    public int getIn() {
        synchronized (waitList) {
            if (inlist.isEmpty()) {
                if (!waitList.isEmpty()) {
                    for (Person per : waitList.getWaitList()) {
                        if (per.getStart() == pos
                                && (per.getDes() - per.getStart()) * direction >= 0) {
                            waitList.notifyAll();
                            return 1;
                        }
                    }
                }
            } else {
                if (!waitList.isEmpty() && inlist.size() < inmax) {
                    for (Person person : waitList.getWaitList()) {
                        if (((person.getDes() - person.getStart()) * direction >= 0)
                                && person.getStart() == pos) {
                            waitList.notifyAll();
                            return 1;
                        }
                    }
                }
            }
            waitList.notifyAll();
            return 0;
        }
    }

    public int getOut() {
        for (Person person : inlist) {
            if (person.getDes() == pos) {
                return 1;
            }
        }
        return 0;
    }
}

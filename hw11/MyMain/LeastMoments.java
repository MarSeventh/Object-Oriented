import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class LeastMoments {
    private final HashMap<Integer, Person> block;
    private final Person start;
    private final HashMap<Integer, Boolean> confirmed;
    private final HashMap<Integer, Integer> distances;
    private int globallength;
    private ArrayList<Person> globalpath;
    private final HashMap<Integer, ArrayList<Person>> paths;
    private PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));

    public LeastMoments(HashMap<Integer, Person> block, Person startperson) {
        this.block = block;
        this.start = startperson;
        confirmed = new HashMap<>();
        distances = new HashMap<>();
        globallength = 0;
        globalpath = new ArrayList<>();
        paths = new HashMap<>();
        for (int id : block.keySet()) {
            confirmed.put(id, false);
            distances.put(id, -1);
        }
        confirmed.put(startperson.getId(), true);
        distances.put(startperson.getId(), 0);
        globalpath.add(startperson);
        paths.put(startperson.getId(), globalpath);
    }

    public int getEdge(Person person1, Person person2) {
        return person1.queryValue(person2);
    }
    public void dijkstra(Person start) {
        if (!confirmed.containsValue(false)) {
            return;
        }
        for (Person friend : ((MyPerson) start).getAcqArray()) {
            if (!confirmed.get(friend.getId()) && (distances.get(friend.getId()) == -1 ||
                    (globallength + getEdge(start, friend) < distances.get(friend.getId())))) {
                ArrayList<Person> newpath = new ArrayList<>(globalpath);
                newpath.add(friend);
                distances.put(friend.getId(), globallength + getEdge(start, friend));
                paths.put(friend.getId(), newpath);
                queue.add(new int[]{friend.getId(), distances.get(friend.getId())});
            }
        }
        Person nextperson = null;
        while (queue.size() > 0) {
            nextperson = block.get(queue.poll()[0]);
            if (!confirmed.get(nextperson.getId())) {
                break;
            }
        }
        /*int mindis = -1;
        for (int id : distances.keySet()) {
            if (!confirmed.get(id) && distances.get(id) != -1 &&
                    (mindis == -1 || distances.get(id) < mindis)) {
                mindis = distances.get(id);
                nextperson = block.get(id);
            }
        }*/
        if (nextperson != null) {
            confirmed.put(nextperson.getId(), true);
            globallength = distances.get(nextperson.getId());
            globalpath = paths.get(nextperson.getId());
            dijkstra(nextperson);
        }
    }

    public int getLeastMoments() {
        dijkstra(start);
        int min = -1;
        HashMap<Person, Integer> searched = new HashMap<>();
        for (int id1 : block.keySet()) {
            Person person1 = block.get(id1);
            searched.put(person1, person1.getId());
            for (Person person2 : ((MyPerson) person1).getAcqArray()) {
                if (person2.equals(person1) || searched.containsKey(person2)) {
                    continue;
                }
                int id2 = person2.getId();
                if (paths.get(id1).size() == 1 || paths.get(id2).size() == 1) {
                    if (paths.get(id1).size() + paths.get(id2).size() >= 4) {
                        int newdis = distances.get(id1) + distances.get(id2) +
                                getEdge(person1, person2);
                        if (min == -1 || newdis < min) {
                            min = newdis;
                        }
                    }
                } else {
                    if (!paths.get(id1).get(1).equals(paths.get(id2).get(1))) {
                        int newdis = distances.get(id1) + distances.get(id2) +
                                getEdge(person1, person2);
                        if (min == -1 || newdis < min) {
                            min = newdis;
                        }
                    }
                }
            }
        }
        return min;
    }
}

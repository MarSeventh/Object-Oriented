public class Person {
    private final int id;
    private final int start;
    private final int des;

    public Person(int id, int start, int des) {
        this.id = id;
        this.start = start;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getDes() {
        return des;
    }
}

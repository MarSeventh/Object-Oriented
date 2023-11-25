
public class Book {
    private final String name;
    private final String type;
    private final String id;
    private int copies;
    private boolean smeared;
    private String date;

    public Book(String name, String type, String id, int copies) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.copies = copies;
        this.smeared = false;
        this.date = null;
    }

    public Book(String name, String date) { //student copy
        this.name = name;
        this.type = null;
        this.id = null;
        this.copies = 1;
        this.smeared = false;
        this.date = date;
    }

    public void setSmeared() {
        smeared = true;
    }

    public boolean isSmeared() {
        return smeared;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public int getRemainedCopies() {
        return copies;
    }

    public void borrowOne() {
        copies--;
    }

    public void returnMore(int num) {
        copies += num;
    }
}

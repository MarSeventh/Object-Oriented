
public class Book {
    private final String name;
    private final String type;
    private final String id;
    private int copies;
    private boolean smeared;
    private String date;
    private boolean canBorrowOut;
    private final String schoolName;

    public Book(String name, String type, String id, int copies,
                String canBorrowOut, String schoolName) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.copies = copies;
        this.smeared = false;
        this.date = null;
        this.canBorrowOut = canBorrowOut.equals("Y");
        this.schoolName = schoolName;
    }

    public Book(String name, String date, String schoolName) { //student copy
        this.name = name;
        this.type = null;
        this.id = null;
        this.copies = 1;
        this.smeared = false;
        this.date = date;
        this.canBorrowOut = true;
        this.schoolName = schoolName;
    }

    public void setSmeared() {
        smeared = true;
    }

    public boolean isSmeared() {
        return smeared;
    }

    public String getSchoolName() {
        return schoolName;
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

    public boolean canBorrowOut() {
        return canBorrowOut;
    }
}

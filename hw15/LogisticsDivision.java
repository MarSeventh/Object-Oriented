import java.util.HashMap;

public class LogisticsDivision {
    private final HashMap<String, Integer> repairedBooks;
    private final Library library;
    private final String schoolName;

    public LogisticsDivision(Library library) {
        this.repairedBooks = new HashMap<>();
        this.library = library;
        this.schoolName = library.getName();
    }

    public void repairBook(String[] cmd) {
        String bookName = cmd[3].split(" ")[1];
        String date = cmd[0];
        String school = cmd[3].split(" ")[0];
        System.out.println(date + " " + school + "-" + bookName +
                " got repaired by logistics division in " + schoolName);
        System.out.println("(State) " + date + " " + bookName +
                " transfers from Returned to Repaired");
        if (school.equals(schoolName)) {
            repairedBooks.merge(bookName, 1, Integer::sum);
        } else {
            library.returnOutBook(new Book(bookName, null, school));
        }

    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> books = new HashMap<>(repairedBooks);
        repairedBooks.clear();
        return books;
    }
}

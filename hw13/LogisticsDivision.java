import java.util.HashMap;

public class LogisticsDivision {
    private final HashMap<String, Integer> repairedBooks;

    public LogisticsDivision() {
        this.repairedBooks = new HashMap<>();
    }

    public void repairBook(String[] cmd) {
        String bookName = cmd[3];
        String date = cmd[0];
        System.out.println(date + " " + bookName +
                " got repaired by logistics division");
        repairedBooks.merge(bookName, 1, Integer::sum);
    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> books = new HashMap<>(repairedBooks);
        repairedBooks.clear();
        return books;
    }
}

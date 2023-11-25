import java.util.HashMap;

public class BorrowAndReturnLibrarian {
    private final HashMap<String, Integer> stayedBooks;
    private final Library library;

    public BorrowAndReturnLibrarian(Library library) {
        this.stayedBooks = new HashMap<>();
        this.library = library;
    }

    public void borrowClassB(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        if (student.canBorrowB()) {
            book.borrowOne();
            student.addBorrowBook(book.getName(), date);
            System.out.println(date + " " + cmd[1] + " borrowed " +
                    cmd[3] + " from borrowing and returning librarian");
        } else {
            book.borrowOne();
            stayedBooks.merge(cmd[3], 1, Integer::sum);
        }
    }

    public void arrangeLost(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        student.arrangeLost(book.getName());
        System.out.println(date + " " + cmd[1] +
                " got punished by borrowing and returning librarian");
    }

    public void arrangeReturn(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        if (student.getBookCopy(book.getName()).isSmeared()) {
            System.out.println(date + " " + cmd[1] +
                    " got punished by borrowing and returning librarian");
            student.returnBook(book.getName());
            System.out.println(date + " " + cmd[1] + " returned " +
                    cmd[3] + " to borrowing and returning librarian");
            library.arrangeRepairBook(cmd);
        } else {
            student.returnBook(book.getName());
            stayedBooks.merge(book.getName(), 1, Integer::sum);
            System.out.println(date + " " + cmd[1] + " returned " +
                    cmd[3] + " to borrowing and returning librarian");
        }
    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> books = new HashMap<>(stayedBooks);
        stayedBooks.clear();
        return books;
    }
}

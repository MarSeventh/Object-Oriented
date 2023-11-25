import java.util.HashMap;

public class Machine {
    private final HashMap<String, Book> allBooks;
    private final HashMap<String, Integer> stayedBooks;
    private final Library library;

    public Machine(Library library) {
        this.library = library;
        this.allBooks = library.getAllBooks();
        this.stayedBooks = new HashMap<>();
    }

    public boolean checkRest(String[] cmd) {
        String name = cmd[3];
        String date = cmd[0];
        String studentId = cmd[1];
        System.out.println(date + " " + studentId + " queried " +
                name + " from self-service machine");
        return allBooks.containsKey(name) && allBooks.get(name).getRemainedCopies() > 0;
    }

    public void borrowClassC(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        if (student.canBorrowC(cmd[3])) {
            book.borrowOne();
            student.addBorrowBook(book.getName(), date);
            System.out.println(date + " " + cmd[1] + " borrowed " +
                    cmd[3] + " from self-service machine");
        } else {
            book.borrowOne();
            stayedBooks.merge(cmd[3], 1, Integer::sum);
        }
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
                    cmd[3] + " to self-service machine");
            library.arrangeRepairBook(cmd);
        } else {
            student.returnBook(book.getName());
            stayedBooks.merge(book.getName(), 1, Integer::sum);
            System.out.println(date + " " + cmd[1] + " returned " +
                    cmd[3] + " to self-service machine");
        }
    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> books = new HashMap<>(stayedBooks);
        stayedBooks.clear();
        return books;
    }
}

import java.util.HashMap;

public class BorrowAndReturnLibrarian {
    private final HashMap<String, Integer> stayedBooks;
    private final Library library;
    private final String departName = "borrowing and returning librarian";

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
            student.addBorrowBook(book, date);
            System.out.println(date + " " + departName + " lent " + book.getSchoolName() + "-" +
                    book.getName() + " to " + student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from Init to InSchool");
            System.out.println("(Sequence) " +
                    date + " :Library sends a message to :BorrowAndReturnLibrarian");
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " borrowed " + book.getSchoolName() + "-" + book.getName() +
                    " from " + departName);
        } else {
            book.borrowOne();
            System.out.println(date + " " + departName + " refused lending " +
                    book.getSchoolName() + "-" + book.getName() + " to " +
                    student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from Init to InSchool");
            System.out.println("(Sequence) " +
                    date + " :BorrowAndReturnLibrarian sends a message to :Library");
            stayedBooks.merge(cmd[3], 1, Integer::sum);
        }
    }

    public void arrangeLost(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = student.getBookCopy(cmd[3]);
        String date = cmd[0];
        student.arrangeLost(book.getName());
        System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                " got punished by " + departName);
        System.out.println(date + " " + departName + " received " +
                student.getSchoolName() + "-" + student.getId() + "'s fine");
    }

    public void arrangeReturn(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = student.getBookCopy(cmd[3]);
        String date = cmd[0];
        String[] returnDate = date.substring(1, date.length() - 1).split("-");
        int returnMonth = Integer.parseInt(returnDate[1]);
        int returnDay = Integer.parseInt(returnDate[2]);
        String[] borrowDate = book.getDate().substring(1, book.getDate().length() - 1).split("-");
        int borrowMonth = Integer.parseInt(borrowDate[1]);
        int borrowDay = Integer.parseInt(borrowDate[2]);
        int borrowDays = Library.getNumOfDate(returnMonth, returnDay) -
                Library.getNumOfDate(borrowMonth, borrowDay);
        if (student.getBookCopy(book.getName()).isSmeared() || borrowDays > 30) {
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " got punished by " + departName);
            System.out.println(date + " " + departName + " received " +
                    student.getSchoolName() + "-" + student.getId() + "'s fine");
            student.returnBook(book.getName());
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " returned " + book.getSchoolName() +
                    "-" + book.getName() + " to " + departName);
            System.out.println(date + " " + departName + " collected " +
                    book.getSchoolName() + "-" + book.getName() + " from " +
                    student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from InSchool to Returned");
            if (book.isSmeared()) {
                cmd[3] = book.getSchoolName() + " " + cmd[3];
                library.arrangeRepairBook(cmd);
            } else {
                if (!book.getSchoolName().equals(library.getName())) {
                    library.returnOutBook(book);
                } else {
                    stayedBooks.merge(book.getName(), 1, Integer::sum);
                }
            }
        } else {
            student.returnBook(book.getName());
            if (!book.getSchoolName().equals(library.getName())) {
                library.returnOutBook(book);
            } else {
                stayedBooks.merge(book.getName(), 1, Integer::sum);
            }
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " returned " + book.getSchoolName() +
                    "-" + book.getName() + " to " + departName);
            System.out.println(date + " " + departName + " collected " +
                    book.getSchoolName() + "-" + book.getName() + " from " +
                    student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from InSchool to Returned");
        }
    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> books = new HashMap<>(stayedBooks);
        stayedBooks.clear();
        return books;
    }
}

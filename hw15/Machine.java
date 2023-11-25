import java.util.HashMap;

public class Machine {
    private final HashMap<String, Book> allBooks;
    private final HashMap<String, Integer> stayedBooks;
    private final Library library;
    private final String schoolName;
    private final String departName = "self-service machine";

    public Machine(Library library) {
        this.library = library;
        this.allBooks = library.getAllBooks();
        this.stayedBooks = new HashMap<>();
        this.schoolName = library.getName();
    }

    public boolean checkRest(String[] cmd) {
        String name = cmd[3];
        String date = cmd[0];
        String studentId = cmd[1];
        System.out.println(date + " " + schoolName + "-" + studentId + " queried " +
                name + " from " + departName);
        System.out.println("(Sequence) " + date + " :Library sends a message to :Machine");
        System.out.println(date + " " + departName + " provided information of " + name);
        System.out.println("(Sequence) " + date + " :Machine sends a message to :Library");
        return allBooks.containsKey(name) && allBooks.get(name).getRemainedCopies() > 0;
    }

    public void borrowClassC(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        if (student.canBorrowC(cmd[3])) {
            book.borrowOne();
            student.addBorrowBook(book, date);
            System.out.println(date + " " + departName + " lent " + book.getSchoolName() + "-" +
                    book.getName() + " to " + student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from Init to InSchool");
            System.out.println("(Sequence) " +
                    date + " :Library sends a message to :Machine");
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
                    date + " :Machine sends a message to :Library");
            stayedBooks.merge(cmd[3], 1, Integer::sum);
        }
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
        if (student.getBookCopy(book.getName()).isSmeared() || borrowDays > 60) {
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " got punished by borrowing and returning librarian");
            System.out.println(date + " borrowing and returning librarian received " +
                    student.getSchoolName() + "-" + student.getId() + "'s fine");
            student.returnBook(book.getName());
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " returned " + book.getSchoolName() + "-" +
                    book.getName() + " to self-service machine");
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
            if (!book.getSchoolName().equals(schoolName)) {
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

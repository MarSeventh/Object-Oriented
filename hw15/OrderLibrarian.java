import java.util.ArrayList;
import java.util.HashMap;

public class OrderLibrarian {
    private ArrayList<Student> registerStudents;
    private ArrayList<String> registerBooks;
    private ArrayList<Student> unsatisfiedStudents;
    private ArrayList<String> unsatisfiedBooks;
    private String date;
    private final Library library;
    private final String departName = "ordering librarian";
    private final String schoolName;

    public OrderLibrarian(Library library) {
        this.library = library;
        this.registerStudents = new ArrayList<>();
        this.registerBooks = new ArrayList<>();
        this.unsatisfiedStudents = new ArrayList<>();
        this.unsatisfiedBooks = new ArrayList<>();
        this.schoolName = library.getName();
        this.date = null;
    }

    public void saveUnsatisfiedOrder(String[] cmd) {
        date = cmd[0];
        Student student = library.getStudent(cmd[1]);
        String bookName = cmd[3];
        unsatisfiedStudents.add(student);
        unsatisfiedBooks.add(bookName);
    }

    public void arrangeUnsatisfiedOrder() {
        for (int i = 0; i < unsatisfiedBooks.size(); i++) {
            String bookName = unsatisfiedBooks.get(i);
            Student student = unsatisfiedStudents.get(i);
            if (library.inOtherSchool(bookName)) {
                if (student.canBorrowFromOut(bookName)) {
                    String borrowSchool = library.borrowFromOut(bookName, student);
                    student.borrowFromOut(bookName, date, borrowSchool);
                }
            } else {
                orderNewBook(student, bookName);
            }
        }
        unsatisfiedBooks.clear();
        unsatisfiedStudents.clear();
    }

    public void orderNewBook(Student student, String bookName) {
        if (student.canRegisterBook(bookName, date)) {
            registerStudents.add(student);
            registerBooks.add(bookName);
            student.addRegisterBook(bookName, date);
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " ordered " + schoolName + "-" +
                    bookName + " from " + departName);
            System.out.println("(Sequence) " +
                    date + " :Library sends a message to :OrderLibrarian");
            System.out.println(date + " " + departName + " recorded " + student.getSchoolName() +
                    "-" + student.getId() + "'s order of " +
                    schoolName + "-" + bookName);
            if (library.getBook(bookName) == null) {
                library.purchaseBook(bookName, student);
            }
        }
    }

    public void getOrderedBook(HashMap<String, Integer> arrangedBooks, String date) {
        ArrayList<String> newRegBooks = new ArrayList<>();
        ArrayList<Student> newRegStudents = new ArrayList<>();
        for (int i = 0; i < registerBooks.size(); i++) {
            String bookName = registerBooks.get(i);
            Student regStudent = registerStudents.get(i);
            if (!regStudent.hasRegistered(bookName)) {
                continue;
            }
            if (arrangedBooks.containsKey(bookName)) {
                arrangedBooks.merge(bookName, -1, Integer::sum);
                if (arrangedBooks.get(bookName) == 0) {
                    arrangedBooks.remove(bookName);
                }
                //give book to regStudent
                regStudent.deleteRegisterBook(bookName);
                regStudent.addBorrowBook(new Book(bookName, date, schoolName), date);
                System.out.println(date + " " + departName + " lent " + schoolName + "-" +
                        bookName + " to " + regStudent.getSchoolName() + "-" + regStudent.getId());
                System.out.println("(State) " + date + " " + bookName +
                        " transfers from Init to InSchool");
                System.out.println("(Sequence) " +
                        date + " :Library sends a message to :OrderLibrarian");
                System.out.println(date + " " + regStudent.getSchoolName() + "-" +
                        regStudent.getId() + " borrowed " + schoolName + "-" + bookName +
                        " from " + departName);
            } else {
                newRegBooks.add(bookName);
                newRegStudents.add(regStudent);
            }
        }
        registerBooks = newRegBooks;
        registerStudents = newRegStudents;
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class OrderLibrarian {
    private ArrayList<Student> registerStudents;
    private ArrayList<String> registerBooks;
    private final Library library;

    public OrderLibrarian(Library library) {
        this.library = library;
        this.registerStudents = new ArrayList<>();
        this.registerBooks = new ArrayList<>();
    }

    public void register(String[] cmd) {
        Student student = library.getStudent(cmd[1]);
        Book book = library.getBook(cmd[3]);
        String date = cmd[0];
        if (student.canRegisterBook(cmd[3], date)) {
            registerStudents.add(student);
            registerBooks.add(cmd[3]);
            student.addRegisterBook(book, date);
            System.out.println(date + " " + cmd[1] + " ordered " +
                    cmd[3] + " from ordering librarian");
        }
    }

    public void assignOrderedBooks(HashMap<String, Integer> arrangedBooks, String date) {
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
                regStudent.addBorrowBook(bookName, date);
                System.out.println(date + " " + regStudent.getId() + " borrowed " +
                        bookName + " from ordering librarian");
            } else {
                newRegBooks.add(bookName);
                newRegStudents.add(regStudent);
            }
        }
        registerBooks = newRegBooks;
        registerStudents = newRegStudents;
    }
}

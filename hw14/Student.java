import java.util.HashMap;

public class Student {
    private final String id;
    private final HashMap<String, Book> borrowBooks;
    private HashMap<String, String> registerBooks;
    private final HashMap<String, Integer> registerTimeTable;
    private final String schoolName;
    private final HashMap<String, Book> borrowFromOutBooks;

    public Student(String id, String schoolName) {
        this.schoolName = schoolName;
        this.id = id;
        this.borrowBooks = new HashMap<>();
        this.registerBooks = new HashMap<>();
        this.registerTimeTable = new HashMap<>();
        this.borrowFromOutBooks = new HashMap<>();
    }

    public void addBorrowBook(Book book, String date) {
        String bookName = book.getName();
        borrowBooks.put(bookName, new Book(bookName, date, book.getSchoolName()));
        if (!book.getSchoolName().equals(schoolName)) {
            borrowFromOutBooks.remove(bookName);
        }
        if (bookName.contains("B")) {
            HashMap<String, String> newRegisterBooks = new HashMap<>();
            for (String na : registerBooks.keySet()) {
                if (!na.contains("B")) {
                    newRegisterBooks.put(na, registerBooks.get(na));
                }
            }
            registerBooks = newRegisterBooks;
        }
    }

    public String getSchoolName() {
        return schoolName;
    }

    public boolean hasRegistered(String bookName) {
        return registerBooks.containsKey(bookName);
    }

    public void addRegisterBook(String bookName, String date) {
        registerBooks.put(bookName, date);
        registerTimeTable.merge(date, 1, Integer::sum);
    }

    public void deleteRegisterBook(String name) {
        registerBooks.remove(name);
        HashMap<String, String> newRegisterBooks = new HashMap<>();
        if (name.contains("B")) {
            for (String na : registerBooks.keySet()) {
                if (!na.contains("B")) {
                    newRegisterBooks.put(na, registerBooks.get(na));
                }
            }
            registerBooks = newRegisterBooks;
        }
    }

    public String getId() {
        return id;
    }

    public boolean canBorrowB() {
        for (String name : borrowBooks.keySet()) {
            if (name.contains("B")) {
                return false;
            }
        }
        return true;
    }

    public boolean canBorrowC(String bookName) {
        return !borrowBooks.containsKey(bookName);
    }

    public boolean canBorrowFromOut(String bookName) {
        if (bookName.contains("B") && !canBorrowB()) {
            return false;
        }
        if (bookName.contains("C") && !canBorrowC(bookName)) {
            return false;
        }
        if (bookName.contains("B")) {
            for (String name : borrowFromOutBooks.keySet()) {
                if (name.contains("B")) {
                    return false;
                }
            }
        }
        if (bookName.contains("C")) {
            if (borrowFromOutBooks.containsKey(bookName)) {
                return false;
            }
        }
        return true;
    }

    public void borrowFromOut(String bookName, String date, String fromSchool) {
        borrowFromOutBooks.put(bookName, new Book(bookName, date, fromSchool));
    }

    public boolean canRegisterBook(String bookName, String date) {
        if (bookName.contains("B") && !canBorrowB()) {
            return false;
        }
        if (bookName.contains("C") && !canBorrowC(bookName)) {
            return false;
        }
        if (registerTimeTable.containsKey(date) && registerTimeTable.get(date) >= 3) {
            return false;
        }
        if (registerBooks.containsKey(bookName)) {
            return false;
        }
        return true;
    }

    public void arrangeLost(String name) {
        borrowBooks.remove(name);
    }

    public void returnBook(String name) {
        borrowBooks.remove(name);
    }

    public void smearBook(String name) {
        if (borrowBooks.containsKey(name)) {
            borrowBooks.get(name).setSmeared();
        }
    }

    public Book getBookCopy(String name) {
        return borrowBooks.getOrDefault(name, null);
    }
}

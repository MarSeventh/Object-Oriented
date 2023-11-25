import java.util.HashMap;

public class Student {
    private final String id;
    private final HashMap<String, Book> borrowBooks;
    private HashMap<String, String> registerBooks;
    private final HashMap<String, Integer> registerTimeTable;

    public Student(String id) {
        this.id = id;
        this.borrowBooks = new HashMap<>();
        this.registerBooks = new HashMap<>();
        this.registerTimeTable = new HashMap<>();
    }

    public void addBorrowBook(String bookName, String date) {
        borrowBooks.put(bookName, new Book(bookName, date));
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

    public boolean hasRegistered(String bookName) {
        return registerBooks.containsKey(bookName);
    }

    public void addRegisterBook(Book book, String date) {
        registerBooks.put(book.getName(), date);
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
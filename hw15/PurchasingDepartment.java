import java.util.ArrayList;
import java.util.HashMap;

public class PurchasingDepartment {
    private final Library library;
    private ArrayList<String> borrowFromOutBooks;
    private ArrayList<String> borrowFromSchools;
    private ArrayList<Student> borrowFromOutStudents;
    private ArrayList<String> borrowOutSchools;
    private ArrayList<String> borrowOutBooks;
    private ArrayList<String> purchaseBooks;
    private ArrayList<Student> purchaseStudents;
    private ArrayList<Book> returnOutBooks;
    private ArrayList<String> returnInBooks;
    private final HashMap<String, Integer> stayedBooks;
    private final String schoolName;
    private final String departName = "purchasing department";
    private final HashMap<String, Integer> purchased;

    public PurchasingDepartment(Library library) {
        this.library = library;
        this.borrowFromOutBooks = new ArrayList<>();
        this.borrowFromOutStudents = new ArrayList<>();
        this.borrowFromSchools = new ArrayList<>();
        this.borrowOutSchools = new ArrayList<>();
        this.borrowOutBooks = new ArrayList<>();
        this.purchaseBooks = new ArrayList<>();
        this.purchaseStudents = new ArrayList<>();
        this.schoolName = library.getName();
        this.purchased = new HashMap<>();
        this.returnOutBooks = new ArrayList<>();
        this.returnInBooks = new ArrayList<>();
        this.stayedBooks = new HashMap<>();
    }

    public void addReturnOutBook(Book book) {
        returnOutBooks.add(book);
    }

    public void addReturnInBook(String bookName) {
        returnInBooks.add(bookName);
    }

    public void addBorrowFromOutBooks(String bookName, Student student, String fromSchool) {
        borrowFromOutBooks.add(bookName);
        borrowFromOutStudents.add(student);
        borrowFromSchools.add(fromSchool);
    }

    public void addBorrowOutBooks(String bookName, String school) {
        borrowOutBooks.add(bookName);
        borrowOutSchools.add(school);
    }

    public void addPurchaseBooks(String bookName, Student student) {
        purchaseBooks.add(bookName);
        purchaseStudents.add(student);
    }

    public void makeSurePurchase(String date) {
        for (int i = 0; i < purchaseBooks.size(); i++) {
            String bookName = purchaseBooks.get(i);
            if (purchaseStudents.get(i).hasRegistered(bookName)) {
                if (!purchased.containsKey(bookName)) {
                    System.out.println(date + " " + schoolName + "-" + bookName +
                            " got purchased by " + departName + " in " + schoolName);
                    library.addPurchaseBook(bookName);
                }
                purchased.merge(bookName, 1, Integer::sum);
            }
        }
        for (String name : purchased.keySet()) {
            if (purchased.get(name) < 3) {
                purchased.put(name, 3);
            }
        }
        purchaseBooks.clear();
        purchaseStudents.clear();
    }

    public HashMap<String, Integer> giveBooks() {
        HashMap<String, Integer> purchasedTmp = new HashMap<>(purchased);
        for (String name : stayedBooks.keySet()) {
            purchasedTmp.merge(name, 1, Integer::sum);
        }
        purchased.clear();
        stayedBooks.clear();
        return purchasedTmp;
    }

    public void transportOut(String date) {
        for (String outBook : borrowOutBooks) {
            System.out.println(date + " " + schoolName + "-" + outBook +
                    " got transported by " + departName + " in " + schoolName);
            System.out.println("(State) " + date + " " + outBook +
                    " transfers from Init to TransportOut");
        }
        for (Book book : returnOutBooks) {
            System.out.println(date + " " + book.getSchoolName() + "-" + book.getName() +
                    " got transported by " + departName + " in " + schoolName);
            System.out.println("(State) " + date + " " + book.getName() +
                    " transfers from Init to TransportOut");
        }
        borrowOutBooks.clear();
        borrowOutSchools.clear();
        returnOutBooks.clear();
    }

    public void transportIn(String date) {
        for (int i = 0; i < borrowFromOutBooks.size(); i++) {
            String inBook = borrowFromOutBooks.get(i);
            String fromSchool = borrowFromSchools.get(i);
            System.out.println(date + " " + fromSchool + "-" + inBook +
                    " got received by " + departName + " in " + schoolName);
            System.out.println("(State) " + date + " " + inBook +
                    " transfers from Init to TransportIn");
        }
        for (String name : returnInBooks) {
            System.out.println(date + " " + schoolName + "-" + name +
                    " got received by " + departName + " in " + schoolName);
            System.out.println("(State) " + date + " " + name +
                    " transfers from Init to TransportIn");
            stayedBooks.merge(name, 1, Integer::sum);
        }
    }

    public void assignOutBooks(String date) {
        for (int i = 0; i < borrowFromOutBooks.size(); i++) {
            String bookName = borrowFromOutBooks.get(i);
            String fromSchool = borrowFromSchools.get(i);
            Student student = borrowFromOutStudents.get(i);
            System.out.println(date + " " + departName + " lent " + fromSchool + "-" +
                    bookName + " to " + student.getSchoolName() + "-" + student.getId());
            System.out.println("(State) " + date + " " + bookName +
                    " transfers from TransportIn to InSchool");
            System.out.println(date + " " + student.getSchoolName() + "-" + student.getId() +
                    " borrowed " + fromSchool + "-" + bookName +
                    " from " + departName);
            student.addBorrowBook(new Book(bookName, date, fromSchool), date);
        }
        borrowFromOutBooks.clear();
        borrowFromSchools.clear();
        borrowFromOutStudents.clear();
        returnInBooks.clear();
    }
}

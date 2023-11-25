import java.util.HashMap;

public class ArrangeLibrarian {
    private final Library library;

    public ArrangeLibrarian(Library library) {
        this.library = library;
    }

    public void arrangeBook(String date) {
        HashMap<String, Integer> arrangedBooks = library.getAbnormalBooks(date);
        library.assignOrderedBooks(arrangedBooks, date);
        HashMap<String, Book> allBooks = library.getAllBooks();
        for (String bookName : arrangedBooks.keySet()) {
            allBooks.get(bookName).returnMore(arrangedBooks.get(bookName));
        }
    }
}

import java.util.ArrayList;

public class Manager {
    private final ArrayList<Library> libraries;
    private int arrangeMonth = 1;
    private int arrangeDay = 1;
    private int lastMonth = 1;
    private int lastDay = 1;
    private boolean init = true;

    public Manager() {
        this.libraries = new ArrayList<>();
    }

    public void addLibrary(Library library) {
        libraries.add(library);
    }

    public void assignRequest(String cmd) {
        String[] paras = cmd.split("\\s+");
        String schoolAndId = paras[1];
        String schoolName = schoolAndId.split("-")[0];
        String id = schoolAndId.split("-")[1];
        finishYesterday(paras);
        makeSurePurchase(paras);
        checkCalender(paras);
        for (Library library : libraries) {
            if (library.getName().equals(schoolName)) {
                String newCmd = paras[0] + " " + id + " " + paras[2] + " " + paras[3];
                library.manager(newCmd);
                break;
            }
        }
        String[] time = paras[0].substring(1, paras[0].length() - 1).split("-");
        lastMonth = Integer.parseInt(time[1]);
        lastDay = Integer.parseInt(time[2]);
    }

    public void finishEnd() {
        for (Library library : libraries) {
            library.arrangeUnsatisfiedOrder();
        }
        //闭馆后，运送图书
        int[] date = Library.getDate(Library.getNumOfDate(lastMonth, lastDay));
        String transDate = "[2023-" + String.format("%02d", date[0]) + "-" +
                String.format("%02d", date[1]) +
                "]";
        for (Library library : libraries) {
            library.transportOut(transDate);
        }
    }

    public void finishYesterday(String[] cmd) {
        String[] time = cmd[0].substring(1, cmd[0].length() - 1).split("-");
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        if (Library.getNumOfDate(month, day) - Library.getNumOfDate(lastMonth, lastDay) >= 1) {
            for (Library library : libraries) {
                library.arrangeUnsatisfiedOrder();
            }
            //闭馆后，运送图书
            int[] date = Library.getDate(Library.getNumOfDate(lastMonth, lastDay));
            String transDate = "[2023-" + String.format("%02d", date[0]) + "-" +
                    String.format("%02d", date[1]) +
                    "]";
            for (Library library : libraries) {
                library.transportOut(transDate);
            }
            //第二天，运入校际图书
            date = Library.getDate(Library.getNumOfDate(lastMonth, lastDay) + 1);
            transDate = "[2023-" + String.format("%02d", date[0]) + "-" +
                    String.format("%02d", date[1]) +
                    "]";
            for (Library library : libraries) {
                library.transportIn(transDate);
            }
            //发放校际图书
            for (Library library : libraries) {
                library.assignOutBooks(transDate);
            }
        }
    }

    public void makeSurePurchase(String[] cmd) {
        String[] time = cmd[0].substring(1, cmd[0].length() - 1).split("-");
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        if (Library.getNumOfDate(month, day) -
                Library.getNumOfDate(arrangeMonth, arrangeDay) >= 3) {
            int[] date = Library.getDate(Library.getNumOfDate(arrangeMonth, arrangeDay) + 3);
            String arrDate = "[2023-" + String.format("%02d", date[0]) + "-" +
                    String.format("%02d", date[1]) +
                    "]";
            for (Library library : libraries) {
                library.makeSurePurchase(arrDate);
            }
        }
    }

    public void checkCalender(String[] cmd) {
        String[] time = cmd[0].substring(1, cmd[0].length() - 1).split("-");
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        if (init) {
            System.out.println("[2023-01-01] arranging librarian arranged all the books");
            init = false;
        }
        boolean flag = true;
        while (Library.getNumOfDate(month, day) -
                Library.getNumOfDate(arrangeMonth, arrangeDay) >= 3) {
            int[] date = Library.getDate(Library.getNumOfDate(arrangeMonth, arrangeDay) + 3);
            System.out.println("[2023-" + String.format("%02d", date[0]) + "-" +
                    String.format("%02d", date[1]) +
                    "] arranging librarian arranged all the books");
            if (flag) {
                for (Library library : libraries) {
                    library.arrangeBooks(date[0], date[1]);
                }
                flag = false;
            }
            int lastArrangeDay = Library.getNumOfDate(arrangeMonth, arrangeDay);
            int newArrangeDay = lastArrangeDay + 3;
            int[] newDate = Library.getDate(newArrangeDay);
            arrangeMonth = newDate[0];
            arrangeDay = newDate[1];
        }
    }

    public boolean inOtherSchool(String bookName) {
        for (Library library : libraries) {
            if (library.getBook(bookName) != null && library.getBook(bookName).getRemainedCopies()
                    > 0 && library.getBook(bookName).canBorrowOut()) {
                return true;
            }
        }
        return false;
    }

    public String borrowFromOut(String bookName, String schoolName) {
        for (Library library : libraries) {
            if (library.getBook(bookName) != null && library.getBook(bookName).getRemainedCopies()
                    > 0 && library.getBook(bookName).canBorrowOut()) {
                library.arrangeBorrowOut(bookName, schoolName);
                return library.getName();
            }
        }
        return null;
    }

    public void returnBook(Book book) {
        String schoolName = book.getSchoolName();
        for (Library library : libraries) {
            if (library.getName().equals(schoolName)) {
                library.returnInBook(book.getName());
                break;
            }
        }
    }
}

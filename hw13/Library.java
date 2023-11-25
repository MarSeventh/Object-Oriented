import java.util.HashMap;

public class Library {
    private final Machine machine;
    private final BorrowAndReturnLibrarian borrowAndReturnLibrarian;
    private final ArrangeLibrarian arrangeLibrarian;
    private final OrderLibrarian orderLibrarian;
    private final LogisticsDivision logisticsDivision;
    private final HashMap<String, Book> allBooks;
    private final HashMap<String, Student> students;
    private int arrangeMonth;
    private int arrangeDay;

    public Library() {
        this.allBooks = new HashMap<>();
        this.students = new HashMap<>();
        this.machine = new Machine(this);
        this.arrangeLibrarian = new ArrangeLibrarian(this);
        this.orderLibrarian = new OrderLibrarian(this);
        this.borrowAndReturnLibrarian = new BorrowAndReturnLibrarian(this);
        this.logisticsDivision = new LogisticsDivision();
        this.arrangeMonth = 1;
        this.arrangeDay = 1;
    }

    public HashMap<String, Integer> getAbnormalBooks() {
        HashMap<String, Integer> abnormalBooks = new HashMap<>();
        HashMap<String, Integer> brBooks = borrowAndReturnLibrarian.giveBooks();
        HashMap<String, Integer> maBooks = machine.giveBooks();
        HashMap<String, Integer> ldBooks = logisticsDivision.giveBooks();
        abnormalBooks.putAll(brBooks);
        for (String name : maBooks.keySet()) {
            abnormalBooks.merge(name, maBooks.get(name), Integer::sum);
        }
        for (String name : ldBooks.keySet()) {
            abnormalBooks.merge(name, ldBooks.get(name), Integer::sum);
        }
        return abnormalBooks;
    }

    public HashMap<String, Book> getAllBooks() {
        return allBooks;
    }

    public Student getStudent(String id) {
        return students.getOrDefault(id, null);
    }

    public Book getBook(String name) {
        return allBooks.getOrDefault(name, null);
    }

    public void manager(String cmd) {
        if (!cmd.startsWith("[")) {
            addBook(cmd);
            return;
        }
        String[] cmds = cmd.split("\\s+");
        tryArrange(cmds);
        if (cmd.contains("borrowed")) {
            arrangeBorrow(cmds);
            return;
        }
        if (cmd.contains("lost")) {
            arrangeLost(cmds);
            return;
        }
        if (cmd.contains("smeared")) {
            arrangeSmear(cmds);
            return;
        }
        if (cmd.contains("returned")) {
            arrangeReturn(cmds);
        }
    }

    public void addBook(String cmd) {
        String[] paras = cmd.split("\\s+");
        String tid = paras[0];
        String[] str = tid.split("-");
        Book newbook = new Book(tid, str[0], str[1], Integer.parseInt(paras[1]));
        allBooks.put(paras[0], newbook);
    }

    public void tryArrange(String[] cmd) {
        String[] time = cmd[0].substring(1, cmd[0].length() - 1).split("-");
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        if (getNumOfDate(month, day) - getNumOfDate(arrangeMonth, arrangeDay) >= 3) {
            int[] date = getDate(getNumOfDate(arrangeMonth, arrangeDay) + 3);
            arrangeBooks(date[0], date[1]);
            int toady = getNumOfDate(month, day);
            int lastArrangeDay = getNumOfDate(arrangeMonth, arrangeDay);
            int newArrangeDay = lastArrangeDay + 3 * ((toady - lastArrangeDay) / 3);
            int[] newDate = getDate(newArrangeDay);
            arrangeMonth = newDate[0];
            arrangeDay = newDate[1];
        }
    }

    public int[] getDate(int numOfDate) {
        int[] date = new int[2];
        if (numOfDate > 334) {
            date[0] = 12;
            date[1] = numOfDate - 334;
        } else if (numOfDate > 304) {
            date[0] = 11;
            date[1] = numOfDate - 304;
        } else if (numOfDate > 273) {
            date[0] = 10;
            date[1] = numOfDate - 273;
        } else if (numOfDate > 243) {
            date[0] = 9;
            date[1] = numOfDate - 243;
        } else if (numOfDate > 212) {
            date[0] = 8;
            date[1] = numOfDate - 212;
        } else if (numOfDate > 181) {
            date[0] = 7;
            date[1] = numOfDate - 181;
        } else if (numOfDate > 151) {
            date[0] = 6;
            date[1] = numOfDate - 151;
        } else if (numOfDate > 120) {
            date[0] = 5;
            date[1] = numOfDate - 120;
        } else if (numOfDate > 90) {
            date[0] = 4;
            date[1] = numOfDate - 90;
        } else if (numOfDate > 59) {
            date[0] = 3;
            date[1] = numOfDate - 59;
        } else if (numOfDate > 31) {
            date[0] = 2;
            date[1] = numOfDate - 31;
        } else {
            date[0] = 1;
            date[1] = numOfDate;
        }
        return date;
    }

    public int getNumOfDate(int month, int day) {
        int sumDay = 0;
        switch (month) {
            case 12:
                sumDay = day + 334;
                break;
            case 11:
                sumDay = day + 304;
                break;
            case 10:
                sumDay = day + 273;
                break;
            case 9:
                sumDay = day + 243;
                break;
            case 8:
                sumDay = day + 212;
                break;
            case 7:
                sumDay = day + 181;
                break;
            case 6:
                sumDay = day + 151;
                break;
            case 5:
                sumDay = day + 120;
                break;
            case 4:
                sumDay = day + 90;
                break;
            case 3:
                sumDay = day + 59;
                break;
            case 2:
                sumDay = day + 31;
                break;
            case 1:
                sumDay = day;
                break;
            default:
        }
        return sumDay;
    }

    public void arrangeBooks(int month, int day) {
        arrangeLibrarian.arrangeBook("[2023-" + String.format("%02d", month) +
                "-" + String.format("%02d", day) + "]");
    }

    public void arrangeBorrow(String[] cmd) {
        String date = cmd[0];
        String studentId = cmd[1];
        String bookName = cmd[3];
        if (!students.containsKey(studentId)) {
            Student student = new Student(studentId);
            students.put(studentId, student);
        }
        if (machine.checkRest(cmd)) {
            if (bookName.contains("B")) {
                borrowAndReturnLibrarian.borrowClassB(cmd);
            } else if (bookName.contains("C")) {
                machine.borrowClassC(cmd);
            }
        } else {
            orderLibrarian.register(cmd);
        }
    }

    public void arrangeLost(String[] cmd) {
        borrowAndReturnLibrarian.arrangeLost(cmd);
    }

    public void arrangeReturn(String[] cmd) {
        String bookName = cmd[3];
        if (bookName.contains("B")) {
            borrowAndReturnLibrarian.arrangeReturn(cmd);
        } else if (bookName.contains("C")) {
            machine.arrangeReturn(cmd);
        }
    }

    public void arrangeSmear(String[] cmd) {
        String studentId = cmd[1];
        String bookName = cmd[3];
        getStudent(studentId).smearBook(bookName);
    }

    public void arrangeRepairBook(String[] cmd) {
        logisticsDivision.repairBook(cmd);
    }

    public void assignOrderedBooks(HashMap<String, Integer> arrangedBooks, String date) {
        orderLibrarian.assignOrderedBooks(arrangedBooks, date);
    }
}

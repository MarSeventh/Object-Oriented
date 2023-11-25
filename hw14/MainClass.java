import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Manager manager = new Manager();
        int t;
        int n;
        int m;
        Scanner scanner = new Scanner(System.in);
        t = Integer.parseInt(scanner.nextLine());
        for (int k = 0; k < t; k++) {
            String tmp = scanner.nextLine();
            String[] temp = tmp.split("\\s+");
            String schoolName = temp[0];
            String bookNum = temp[1];
            Library library = new Library(schoolName, manager);
            manager.addLibrary(library);
            n = Integer.parseInt(bookNum);
            for (int i = 0; i < n; i++) {
                String cmd = scanner.nextLine();
                library.manager(cmd);
            }
        }
        m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            String cmd = scanner.nextLine();
            manager.assignRequest(cmd);
        }
        manager.finishEnd();
    }
}

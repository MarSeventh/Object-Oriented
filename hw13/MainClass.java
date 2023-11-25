import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Library library = new Library();
        int n;
        int m;
        Scanner scanner = new Scanner(System.in);
        n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String cmd = scanner.nextLine();
            library.manager(cmd);
        }
        m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            String cmd = scanner.nextLine();
            library.manager(cmd);
        }
    }
}

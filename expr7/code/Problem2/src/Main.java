import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Controller.getInstance().initial();             //初始化
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {                     //识别输入,处理指令
            String command = scanner.next();
            if ("end".equals(command)) {
                break;
            }
            String guestName = scanner.next();
            if ("checkIn".equals(command)) {
                Controller.getInstance().ask2checkIn(guestName);
            } else if ("checkOut".equals(command)) {
                String roomId = scanner.next();
                Controller.getInstance().ask2checkOut(guestName, Integer.parseInt(roomId));
            } else if ("clean".equals(command)) {
                String roomId =scanner.next();
                Controller.getInstance().ask2clean(guestName, Integer.parseInt(roomId));
            } else {
                System.out.println("Rejected: unknown command!");
                break;
            }
        }
        Controller.getInstance().setEndTag();
    }
}

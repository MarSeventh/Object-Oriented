import java.util.Scanner;

public class Receptionist {
    private String workerId;
    private String workerName;
    private Dispatcher dispatcher;

    public Receptionist(String id, String name) {
        this.workerId = id;
        this.workerName = name;
    }

    public String getWorkerId() {
        return workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void serve() {   //识别输入,处理指令
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String operation = scanner.next();
            if (operation.equals("end")) {
                break;
            }
            String guestName = scanner.next();
            if (operation.equals("checkIn")) {
                ask2checkIn(guestName);
            } else if (operation.equals("checkOut")) {
                int roomId = Integer.parseInt(scanner.next());
                ask2checkOut(guestName, roomId);
            } else if (operation.equals("clean")) {
                int roomId = Integer.parseInt(scanner.next());
                ask2clean(roomId);
            } else {
                System.out.println("Receptionist " + getWorkerId() + " " +
                        getWorkerName() + " : receive unknown request");
                break;
            }
        }
    }

    private void ask2checkIn(String guestName) {
        int roomId = dispatcher.checkIn();
        if (roomId != -1) {
            System.out.println("Receptionist" + getWorkerId() + " " +
                    getWorkerName() + " : Mr." + guestName +
                    ", you can now move in room " + roomId);                //前台反馈入住成功
        } else {
            System.out.println("Receptionist" + getWorkerId() + " " +
                    getWorkerName() + " : Mr." + guestName +
                    ", There are no available rooms right now. Sorry!");    //前台反馈入住失败
        }
    }

    private void ask2checkOut(String guestName, int roomId) {
        dispatcher.checkOut(roomId);
        System.out.println("Receptionist " + getWorkerId() + " " +
                getWorkerName() + " : Mr." + guestName +
                "you have checked out successfully. Have a nice day!");     //前台反馈退房成功
    }

    private void ask2clean(int roomId) {
        dispatcher.clean(roomId);
    }
}

import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        WaitList waitmap = new WaitList();
        Input input = new Input(waitmap);
        Manager manager = new Manager(waitmap);
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i);
            elevators.add(elevator);
        }
        for (Elevator elevator : elevators) {
            manager.addWaitLists(elevator.getWaitList());
            manager.addElevators(elevator);
        }
        input.start();
        manager.start();
        for (Elevator elevator : elevators) {
            elevator.start();
        }
    }
}

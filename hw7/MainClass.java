import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        CountController countController = new CountController();
        WaitList waitmap = new WaitList();
        Manager manager = new Manager(waitmap);
        Input input = new Input(waitmap, manager, countController);
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, manager, waitmap, countController);
            elevators.add(elevator);
        }
        for (Elevator elevator : elevators) {
            manager.addElevators(elevator);
            input.addElevators(elevator);
        }
        input.start();
        manager.start();
        for (Elevator elevator : elevators) {
            elevator.start();
        }
    }
}

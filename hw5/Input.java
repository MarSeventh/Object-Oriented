import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class Input extends Thread {
    private final WaitList waitmap;

    public Input(WaitList waitmap) {
        this.waitmap = waitmap;
    }

    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                try {
                    elevatorInput.close();
                    waitmap.setEnd(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            } else {
                int id = request.getPersonId();
                int start = request.getFromFloor();
                int des = request.getToFloor();
                Person person = new Person(id, start, des);
                waitmap.addPerson(person);
            }
        }
    }
}

import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();

        Provider provider = new Provider();
        new Thread(provider).start();
    }
}

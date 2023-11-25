import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        // 输入AssignThread信息
        Scanner scanner = new Scanner(System.in);
        int numInsertThread = scanner.nextInt();
        ArrayList<String> insertKeys = new ArrayList<>();
        ArrayList<String> insertValues = new ArrayList<>();
        for (int i = 0; i < numInsertThread; i++) {
            String key = scanner.next();
            insertKeys.add(key);
            String value = scanner.next();
            insertValues.add(value);
        }
        
        int numSelectThread = scanner.nextInt();
        ArrayList<String> selectKeys = new ArrayList<>();
        for (int i = 0; i < numSelectThread; i++) {
            String key = scanner.next();
            selectKeys.add(key);
        }
    
        Database<String, String> database = new Database<>();
        
        // 启动AssignThread线程
        for (int i = 0; i < numInsertThread; i++) {
            new InsertThread(database, insertKeys.get(i), insertValues.get(i)).start();
        }
        
        
        // 启动RetrieveThread线程
        for (int i = 0; i < numSelectThread; i++) {
            new SelectThread(database, selectKeys.get(i)).start();
        }
        
        // 停止约5秒
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 强制终止
        System.exit(0);
    }
}

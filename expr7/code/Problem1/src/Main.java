public class Main {
    public static void main(String[] args) {

        //先考虑各种员工只有一位
        Receptionist receptionist = new Receptionist("1", "Jack");
        Dispatcher dispatcher = new Dispatcher("2", "Bob");
        Cleaner cleaner = new Cleaner("3", "Mike");
        receptionist.setDispatcher(dispatcher);
        dispatcher.setCleaner(cleaner);

        // 开始服务
        receptionist.serve();

    }
}

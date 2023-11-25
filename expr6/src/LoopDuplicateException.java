public class LoopDuplicateException extends Exception {

    private static int count = 0;
    private int id;

    LoopDuplicateException() {
        id = count++;
    }

    public void print() {
        System.err.println("Duplicated Loop Found!" +
                String.format(" (Exception id is %d, this exception has occurred %d times)", id, count));
    }
}

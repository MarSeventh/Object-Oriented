public class Variable implements Factor {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    public String toString() {
        return var;
    }
}

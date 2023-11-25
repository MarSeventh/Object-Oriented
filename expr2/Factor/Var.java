package Factor;

public class Var implements Factor {

    private String name;

    public Var(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Factor derive() {
        Term term = new Term();
        term.addFactor(new Number("1"));
        return term;
    }

    @Override
    public Factor clone() {
        return new Var(name);
    }
}

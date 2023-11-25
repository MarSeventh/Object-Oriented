package Factor;

public class Sin implements Factor {

    private Factor factor;

    public Sin(Factor factor) {
        this.factor = factor;
    }

    private Cos toCos() {
        return new Cos(factor);
    }

    @Override
    public String toString() {
        return "sin(" + factor.toString() + ")";
    }

    @Override
    public Factor derive() {
        Term term = new Term();
        term.addFactor(toCos());
        term = Term.mergeTerm(term, (Term) factor.derive());
        return term;
    }

    @Override
    public Factor clone() {
        return new Sin(factor.clone());
    }
}

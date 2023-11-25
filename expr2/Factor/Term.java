package Factor;

import java.util.ArrayList;

public class Term implements Factor {

    private ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    // 合并两个项
    public static Term mergeTerm(Term term1, Term term2) {
        if (term1 == null) {
            return term2;
        }
        if (term2 == null) {
            return term1;
        }
        Term term = new Term();
        term1.factors.forEach(term::addFactor);
        term2.factors.forEach(term::addFactor);
        return term;
    }

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < factors.size(); i++) {
            if (i != 0) {
                sb.append("*");
            }
            sb.append(factors.get(i).toString());
        }
        return sb.toString();
    }

    public Factor expand() {
        Expr expr = new Expr();
        // 递归实现去括号
        boolean hasExprFactor = false;
        for (int i = 0; i < factors.size(); i++) {
            Factor factor = factors.get(i);
            if (factor instanceof Expr) {
                hasExprFactor = true;
                // other为其余因子组成的项
                Term other = new Term();
                for (int j = 0; j < factors.size(); j++) {
                    if (i != j) {
                        other.addFactor(factors.get(j).clone());
                    }
                }
                Expr e = (Expr) factor;
                for (Term t : e.getTerms()) {
                    expr.addTerm(mergeTerm(t, other));
                }
                break;
            }
        }
        if (hasExprFactor) {
            return expr.expand();
        } else {
            expr.addTerm((Term) this.clone());
            return expr;
        }
    }

    @Override
    public Factor derive() {
        Expr expr = new Expr();
        for (int i = 0; i < factors.size(); i++) {
            Term t = (Term) factors.get(i).derive();
            for (int j = 0; j < factors.size(); j++) {
                if (i != j) {
                    t.addFactor(factors.get(j));
                }
            }
            expr.addTerm(t);
        }
        return expr;
    }

    @Override
    public Factor clone() {
        Term term = new Term();
        for (int i = 0; i < factors.size(); i++) {
            term.addFactor(factors.get(i).clone());
        }
        return term;
    }
}

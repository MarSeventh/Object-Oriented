package Factor;

import java.util.ArrayList;

public class Expr implements Factor {

    private ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    // 合并两个表达式
    public static Expr mergeExpr(Expr expr1, Expr expr2) {
        if (expr1 == null) {
            return expr2;
        }
        if (expr2 == null) {
            return expr1;
        }
        Expr expr = new Expr();
        expr1.terms.forEach(expr::addTerm);
        expr2.terms.forEach(expr::addTerm);
        return expr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < terms.size(); i++) {
            if (i != 0) {
                sb.append("+");
            }
            sb.append(terms.get(i).toString());
        }
        return sb.toString();
    }

    public Factor expand() {
        Expr expr = new Expr();
        for (int i = 0; i < terms.size(); i++) {
            expr = mergeExpr(expr, (Expr) terms.get(i).expand());
        }
        return expr;
    }

    @Override
    public Factor derive() {
        Expr expr = new Expr();
        for (int i = 0; i < terms.size(); i++) {
            expr = mergeExpr(expr, (Expr) terms.get(i).derive());
        }
        Term term = new Term();
        term.addFactor(expr);
        return term;
    }

    @Override
    public Factor clone() {
        Expr expr = new Expr();
        for (int i = 0; i < terms.size(); i++) {
            expr.addTerm((Term) terms.get(i).clone());
        }
        return expr;
    }
}

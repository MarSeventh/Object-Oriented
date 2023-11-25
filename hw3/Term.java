import java.util.ArrayList;

public class Term implements Factor {
    private final ArrayList<Factor> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Factor factor) {
        organizeFactor(factor);
    }

    public void organizeFactor(Factor factor) {
        Factor add = new Variable("+");
        ArrayList<Factor> newfactors = new ArrayList<>();
        ArrayList<Term> newterms = new ArrayList<>();
        if (isExpr(factor)) {
            newterms = getNewTerms(factor);
            if (!factors.isEmpty()) {
                for (int i = 0; i < newterms.size(); i++) {
                    int flag = 0;
                    for (Factor value : factors) {
                        if (!value.toString().equals("+")) {
                            if (flag == 0) {
                                newfactors.add(newterms.get(i));
                                flag = 1;
                            }
                            newfactors.add(value);
                        } else {
                            flag = 0;
                            newfactors.add(add);
                        }
                    }
                    if (i < newterms.size() - 1) {
                        newfactors.add(add);
                    }
                }
            } else {
                for (int i = 0; i < newterms.size(); i++) {
                    newfactors.add(newterms.get(i));
                    if (i < newterms.size() - 1) {
                        newfactors.add(add);
                    }
                }
            }
        } else {
            if (!factors.isEmpty()) {
                int flag = 0;
                for (Factor value : factors) {
                    if (!value.toString().equals("+")) {
                        if (flag == 0) {
                            newfactors.add(factor);
                            flag = 1;
                        }
                        newfactors.add(value);
                    } else {
                        flag = 0;
                        newfactors.add(add);
                    }
                }
            } else {
                newfactors.add(factor);
            }
        }
        factors.clear();
        factors.addAll(newfactors);
    }

    public ArrayList<Term> getNewTerms(Factor factor) {
        ArrayList<Term> term = new ArrayList<>();
        String temp = factor.toString().replaceAll("\\*\\*", "^");
        Lexer le = new Lexer(temp);
        Parser pa = new Parser(le);
        term.add(pa.parseTerm());
        while (le.peek().equals("+")) {
            le.next();
            term.add(pa.parseTerm());
        }
        return term;
    }

    public boolean isExpr(Factor factor) {
        String str = factor.toString();
        boolean flag = false;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (count == 0 && str.charAt(i) == '+' && i > 0) {
                flag = true;
                return flag;
            } else if (str.charAt(i) == '(') {
                count--;
            } else if (str.charAt(i) == ')') {
                count++;
            }
        }
        return flag;
    }

    public void multiTerm(ArrayList<Factor> orlist, ArrayList<Factor> delist) {
        Factor add = new Variable("&");
        for (int i = 0; i < delist.size(); i++) {
            Term newterm = new Term();
            newterm.addFactor(delist.get(i));
            for (int j = 0; j < orlist.size(); j++) {
                if (j != i) {
                    newterm.addFactor(orlist.get(j));
                }
            }
            factors.add(newterm);
            if (i < delist.size() - 1) {
                factors.add(add);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int flag1 = 0;
        if (!factors.isEmpty()) {
            for (int i = 0; i < factors.size(); i++) {
                if (factors.get(i).toString().equals("+") ||
                        factors.get(i).toString().equals("&")) {
                    sb.append("+");
                    flag1 = 1;
                } else {
                    if (i > 0 && flag1 == 0) {
                        sb.append("*");
                    }
                    sb.append(factors.get(i).toString());
                    flag1 = 0;
                }
            }
        }
        return sb.toString();
    }
}

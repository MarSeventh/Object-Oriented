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
        if (factor.toString().contains("+")) {
            String[] split = factor.toString().split("\\+");
            for (String s : split) {
                Lexer le = new Lexer(s);
                Parser pa = new Parser(le);
                newterms.add(pa.parseTerm());
            }
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int flag1 = 0;
        if (!factors.isEmpty()) {
            for (int i = 0; i < factors.size(); i++) {
                if (factors.get(i).toString().equals("+")) {
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

public class Cos implements Factor {
    private final Expr expression;

    public Cos(Expr expression) {
        this.expression = expression;
    }

    public String toString() {
        String temp = expression.simplify(expression);
        if (temp.equals("0")) {
            return "1";
        } else {
            String str;
            String temp1 = temp.replaceAll("\\*\\*", "^");
            if (expression.isExprFactor(temp1)) {
                if (temp1.startsWith("-")) {
                    str = "cos((" + expression.reverse(temp) + "))";
                } else {
                    str = "cos((" + temp + "))";
                }
            } else {
                if (temp1.startsWith("-")) {
                    str = "cos(" + expression.reverse(temp) + ")";
                } else {
                    str = "cos(" + temp + ")";
                }
            }
            return str;
        }
    }
}

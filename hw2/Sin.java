public class Sin implements Factor {
    private final Expr expression;

    public Sin(Expr expression) {
        this.expression = expression;
    }

    public String toString() {
        String temp = expression.simplify(expression);
        if (temp.equals("0")) {
            return "0";
        } else {
            String str;
            String temp1 = temp.replaceAll("\\*\\*", "^");
            if (expression.isExprFactor(temp1)) {
                if (temp1.startsWith("-")) {
                    str = "-1*sin((" + expression.reverse(temp) + "))";
                } else {
                    str = "sin((" + temp + "))";
                }
            } else {
                if (temp1.startsWith("-")) {
                    str = "-1*sin(" + expression.reverse(temp) + ")";
                } else {
                    str = "sin(" + temp + ")";
                }
            }
            return str;
        }
    }
}

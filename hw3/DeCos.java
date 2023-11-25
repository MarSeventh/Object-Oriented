public class DeCos implements Factor {
    private final Expr expression;

    private final String para;

    public DeCos(Expr expression, String para) {
        this.expression = expression;
        this.para = para;
    }

    public String toString() {
        String temp = expression.simplify(expression);
        String temp1 = temp.replaceAll("\\*\\*", "^");
        if (temp.equals("0")) {
            return "0";
        } else if (temp.indexOf(para.charAt(0)) == -1) {
            return "0";
        } else {
            Lexer lexer = new Lexer(temp1);
            Derivative derivative = new Derivative("d" + para, lexer);
            String temp2 = derivative.deExpr().toString();
            temp2 = temp2.replaceAll("\\*\\*", "^");
            String str = "(" + reverseString(temp1) + ")" + "*(" + temp2 + ")";
            Lexer lexer1 = new Lexer(str);
            Parser parser = new Parser(lexer1);
            return parser.parseExpr().toString();
        }
    }

    public String oriString(String temp) {
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

    public String reverseString(String temp) {
        String str;
        String temp1 = temp.replaceAll("\\*\\*", "^");
        if (expression.isExprFactor(temp1)) {
            if (temp1.startsWith("-")) {
                str = "sin((" + expression.reverse(temp) + "))";
            } else {
                str = "-1*sin((" + temp + "))";
            }
        } else {
            if (temp1.startsWith("-")) {
                str = "sin(" + expression.reverse(temp) + ")";
            } else {
                str = "-1*sin(" + temp + ")";
            }
        }
        return str;
    }
}

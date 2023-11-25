import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;
    private Factor x1;
    private Factor y1;
    private Factor z1;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Parser(Lexer lexer, Factor x, Factor y, Factor z) {
        this.lexer = lexer;
        this.x1 = x;
        this.y1 = y;
        this.z1 = z;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm());

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) { // first
            expr.addTerm(parseTerm());
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        Factor temp;
        BigInteger num = new BigInteger("-1");
        Factor newnum = new Number(num);
        BigInteger num1 = new BigInteger("1");
        Factor newnum1 = new Number(num1);
        if (lexer.peek().equals("+")) {
            lexer.next();
            temp = parseFactor();
        } else if (lexer.peek().equals("-")) {
            term.addFactor(newnum);
            lexer.next();
            temp = parseFactor();
        } else {
            temp = parseFactor();
        }
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (Integer.parseInt(lexer.peek()) != 0) {
                for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                    term.addFactor(temp);
                }
            } else {
                term.addFactor(newnum1);
            }
            lexer.next();
        } else {
            term.addFactor(temp);
        }
        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("-")) {
                term.addFactor(newnum);
                lexer.next();
            }
            temp = parseFactor();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (Integer.parseInt(lexer.peek()) != 0) {
                    for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                        term.addFactor(temp);
                    }
                } else {
                    term.addFactor(newnum1);
                }
                lexer.next();
            } else {
                term.addFactor(temp);
            }
        }
        return term;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();                    // third
            return expr;
        } else if (lexer.peek().equals("-")) {
            doubleNext();
            return new Number(new BigInteger("-1"));
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            BigInteger num = new BigInteger(lexer.peek());                 // fourth
            lexer.next();
            return new Number(num);
        } else if ("fgh".indexOf(lexer.peek().charAt(0)) != -1) {
            Factor x;
            Factor y = null;
            Factor z = null;
            int count = 1;
            final String name = lexer.peek();
            doubleNext();
            x = parseExpr();
            while (lexer.peek().equals(",")) {
                lexer.next();
                count++;
                if (count == 2) {
                    y = parseExpr();
                } else {
                    z = parseExpr();
                }
            }
            lexer.next();
            return MainClass.getFunction(name).calDefine(x, y, z);
        } else if ("!@#".indexOf(lexer.peek().charAt(0)) != -1) {
            if (lexer.peek().equals("!")) {
                lexer.next();
                return x1;
            } else if (lexer.peek().equals("@")) {
                lexer.next();
                return y1;
            } else {
                lexer.next();
                return z1;
            }
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            Factor circular;
            String tri = lexer.peek();
            lexer.next();
            if (tri.equals("sin")) {
                circular = new Sin(parseExpr());
            } else {
                circular = new Cos(parseExpr());
            }
            lexer.next();
            return circular;
        } else {
            Factor var = new Variable(lexer.peek());
            lexer.next();
            return var;
        }
    }

    public void doubleNext() {
        lexer.next();
        lexer.next();
    }
}

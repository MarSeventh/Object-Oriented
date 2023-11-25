import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
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
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            BigInteger num = new BigInteger(lexer.peek());                 // fourth
            lexer.next();
            return new Number(num);
        } else {
            Factor var = new Variable(lexer.peek());
            lexer.next();
            return var;
        }
    }
}

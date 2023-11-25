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
            return parseExprFactor();
        } else if (lexer.peek().equals("-")) {
            doubleNext();
            return new Number(new BigInteger("-1"));
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            return parseNumFactor();
        } else if ("fgh".indexOf(lexer.peek().charAt(0)) != -1) {
            return parseFunFactor();
        } else if ("!@#".indexOf(lexer.peek().charAt(0)) != -1) {
            return parseFunPara();
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            return parseTriFactor();
        } else if (lexer.peek().equals("dx") || lexer.peek().equals("dy")
                || lexer.peek().equals("dz")) {
            return parseDerFactor();
        } else if ("<>?".indexOf(lexer.peek().charAt(0)) != -1) {
            return parseFunDe();
        } else {
            return parseVarFactor();
        }
    }

    public void doubleNext() {
        lexer.next();
        lexer.next();
    }

    public Factor parseFunFactor() {
        Factor x = null;
        Factor y = null;
        Factor z = null;
        int count = 1;
        final String name = lexer.peek();
        doubleNext();
        x = parseExpr();
        x = simplifyPara(x);
        while (lexer.peek().equals(",")) {
            lexer.next();
            count++;
            if (count == 2) {
                y = parseExpr();
                y = simplifyPara(y);
            } else {
                z = parseExpr();
                z = simplifyPara(z);
            }
        }
        lexer.next();
        return MainClass.getFunction(name).calDefine(x, y, z);
    }

    public Factor simplifyPara(Factor para) {
        Expr expr = (Expr) para;
        String str = expr.simplify(expr);
        str = str.replaceAll("\\*\\*", "^");
        Lexer lexer1 = new Lexer(str);
        Parser parser = new Parser(lexer1);
        return parser.parseExpr();
    }

    public Factor parseFunPara() {
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
    }

    public Factor parseTriFactor() {
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
    }

    public Factor parseVarFactor() {
        Factor var = new Variable(lexer.peek());
        lexer.next();
        return var;
    }

    public Factor parseNumFactor() {
        BigInteger num = new BigInteger(lexer.peek());                 // fourth
        lexer.next();
        return new Number(num);
    }

    public Factor parseExprFactor() {
        lexer.next();
        Factor expr = parseExpr();
        lexer.next();                    // third
        return expr;
    }

    public Factor parseDerFactor() {
        Derivative derivative;
        String name = lexer.peek();
        lexer.next();
        Expr der = parseExpr();
        StringSimplify st = new StringSimplify(der.simplify(der));
        String str = st.simplify();
        Lexer lexer1 = new Lexer(str);
        derivative = new Derivative(name, lexer1);
        lexer.next();
        return derivative.deExpr();
    }

    public Factor parseFunDe() {
        Derivative derivative;
        String name;
        if (lexer.peek().equals("<")) {
            name = "dx";
        } else if (lexer.peek().equals(">")) {
            name = "dy";
        } else {
            name = "dz";
        }
        lexer.next();
        Expr der = parseExpr();
        StringSimplify st = new StringSimplify(der.simplify(der));
        String str = st.simplify();
        Lexer lexer1 = new Lexer(str);
        derivative = new Derivative(name, lexer1);
        lexer.next();
        String dered = derivative.deExpr().toString();
        dered = dered.replaceAll("x", "!");
        dered = dered.replaceAll("y", "@");
        dered = dered.replaceAll("z", "#");
        dered = dered.replaceAll("\\*\\*", "^");
        Lexer lexer2 = new Lexer(dered);
        Parser parser = new Parser(lexer2, x1, y1, z1);
        return parser.parseExpr();
    }
}

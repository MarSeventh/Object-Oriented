import java.math.BigInteger;
import java.util.ArrayList;

public class Derivative implements Factor {
    private final String para;
    private final Lexer lexer;

    public Derivative(String name, Lexer lexer) {
        this.para = name.substring(1);
        this.lexer = lexer;
    }

    public Expr deExpr() {
        Expr expr = new Expr();
        expr.addTerm(deTerm());
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            expr.addTerm(deTerm());
        }
        return expr;
    }

    public Term deTerm() {
        Term term = new Term();
        ArrayList<Factor> orlist = new ArrayList<>();
        ArrayList<Factor> delist = new ArrayList<>();
        getFirstFactor(orlist, delist);
        getOtherFactor(orlist, delist);
        term.multiTerm(orlist, delist);
        return term;
    }

    public void getFirstFactor(ArrayList<Factor> orlist, ArrayList<Factor> delist) {
        Factor tempor;
        Factor tempde;
        Factor newnum = new Number(new BigInteger("-1"));
        Factor newnum1 = new Number(new BigInteger("1"));
        Factor newnum0 = new Number(new BigInteger("0"));
        if (lexer.peek().equals("+")) {
            Lexer lexer1 = new Lexer(lexer);
            lexer1.next();
            Parser parser = new Parser(lexer1);
            tempor = parser.parseFactor();
            lexer.next();
            tempde = deFactor();
        } else if (lexer.peek().equals("-")) {
            orlist.add(newnum);
            delist.add(newnum0);
            Lexer lexer1 = new Lexer(lexer);
            lexer1.next();
            Parser parser = new Parser(lexer1);
            tempor = parser.parseFactor();
            lexer.next();
            tempde = deFactor();
        } else {
            Lexer lexer1 = new Lexer(lexer);
            Parser parser = new Parser(lexer1);
            tempor = parser.parseFactor();
            tempde = deFactor();
        }
        if (lexer.peek().equals("^")) {
            lexer.next();
            if (Integer.parseInt(lexer.peek()) != 0) {
                for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                    orlist.add(tempor);
                    delist.add(tempde);
                }
            } else {
                orlist.add(newnum1);
                delist.add(newnum0);
            }
            lexer.next();
        } else {
            orlist.add(tempor);
            delist.add(tempde);
        }
    }

    public void getOtherFactor(ArrayList<Factor> orlist, ArrayList<Factor> delist) {
        Factor tempor;
        Factor tempde;
        Factor newnum = new Number(new BigInteger("-1"));
        Factor newnum1 = new Number(new BigInteger("1"));
        Factor newnum0 = new Number(new BigInteger("0"));
        while (lexer.peek().equals("*")) {
            lexer.next();
            if (lexer.peek().equals("-")) {
                orlist.add(newnum);
                delist.add(newnum0);
                lexer.next();
            }
            Lexer lexer1 = new Lexer(lexer);
            Parser parser = new Parser(lexer1);
            tempor = parser.parseFactor();
            tempde = deFactor();
            if (lexer.peek().equals("^")) {
                lexer.next();
                if (Integer.parseInt(lexer.peek()) != 0) {
                    for (int i = 0; i < Integer.parseInt(lexer.peek()); i++) {
                        orlist.add(tempor);
                        delist.add(tempde);
                    }
                } else {
                    orlist.add(newnum1);
                    delist.add(newnum0);
                }
                lexer.next();
            } else {
                orlist.add(tempor);
                delist.add(tempde);
            }
        }
    }

    public Factor deFactor() {
        if (lexer.peek().equals("(")) {
            return deExprFactor();
        } else if (Character.isDigit(lexer.peek().charAt(0))) {
            return deNumFactor();
        } else if ("fgh".indexOf(lexer.peek().charAt(0)) != -1) {
            return deFunFactor();
        } else if (lexer.peek().equals("sin") || lexer.peek().equals("cos")) {
            return deTriFactor();
        } else {
            return deVarFactor();
        }
    }

    public void doubleNext() {
        lexer.next();
        lexer.next();
    }

    public Factor deExprFactor() {
        lexer.next();
        Factor expr = deExpr();
        lexer.next();
        return expr;
    }

    public Factor deNumFactor() {
        BigInteger num = new BigInteger("0");                 // fourth
        lexer.next();
        return new Number(num);
    }

    public Factor deFunFactor() {
        Factor x;
        Factor y = null;
        Factor z = null;
        int count = 1;
        final String name = lexer.peek();
        doubleNext();
        Parser parser = new Parser(lexer);
        x = parser.parseExpr();
        x = simplifyPara(x);
        while (lexer.peek().equals(",")) {
            lexer.next();
            count++;
            if (count == 2) {
                y = parser.parseExpr();
                y = simplifyPara(y);
            } else {
                z = parser.parseExpr();
                z = simplifyPara(z);
            }
        }
        lexer.next();
        Expr funexpr = MainClass.getFunction(name).calDefine(x, y, z);
        String str = funexpr.simplify(funexpr);
        str = str.replaceAll("\\*\\*", "^");
        Lexer lexer1 = new Lexer(str);
        Derivative derivative = new Derivative("d" + para, lexer1);
        return derivative.deExpr();
    }

    public Factor simplifyPara(Factor para) {
        Expr expr = (Expr) para;
        String str = expr.simplify(expr);
        str = str.replaceAll("\\*\\*", "^");
        Lexer lexer1 = new Lexer(str);
        Parser parser = new Parser(lexer1);
        return parser.parseExpr();
    }

    public Factor deTriFactor() {
        Factor circular;
        String tri = lexer.peek();
        lexer.next();
        Parser parser = new Parser(lexer);
        if (tri.equals("sin")) {
            circular = new DeSin(parser.parseExpr(), para);
        } else {
            circular = new DeCos(parser.parseExpr(), para);
        }
        lexer.next();
        return circular;
    }

    public Factor deVarFactor() {
        Factor var;
        if (lexer.peek().equals(para)) {
            var = new DeVariable(lexer.peek());
        } else {
            var = new Number(new BigInteger("0"));
        }
        lexer.next();
        return var;
    }

}

import java.util.ArrayList;

public class SelfDefine implements Factor {
    private final String define;
    private final ArrayList<String> para = new ArrayList<>();

    public SelfDefine(String define) {
        StringSimplify st = new StringSimplify(define);
        String dispose = st.simplify();
        String parameter = dispose.substring(0, dispose.indexOf('='));
        for (int i = 0; i < parameter.length(); i++) {
            if ("xyz".indexOf(parameter.charAt(i)) != -1) {
                para.add(String.valueOf(parameter.charAt(i)));
            }
        }
        dispose = dispose.substring(dispose.indexOf('=') + 1);
        dispose = dispose.replaceAll("x", "!");
        dispose = dispose.replaceAll("y", "@");
        dispose = dispose.replaceAll("z", "#");
        this.define = dispose;
    }

    public Expr calDefine(Factor x, Factor y, Factor z) {
        Lexer lexer;
        Factor a = null;
        Factor b = null;
        Factor c = null;
        lexer = new Lexer(define);
        for (int i = 0; i < para.size(); i++) {
            Factor temp;
            temp = temp(i, x, y, z);
            if (para.get(i).equals("x")) {
                a = temp;
            } else if (para.get(i).equals("y")) {
                b = temp;
            } else if (para.get(i).equals("z")) {
                c = temp;
            }
        }
        Parser parser = new Parser(lexer, a, b, c);
        return parser.parseExpr();
    }

    public Factor temp(int i, Factor x, Factor y, Factor z) {
        if (i == 0) {
            return x;
        } else if (i == 1) {
            return y;
        } else {
            return z;
        }
    }
}

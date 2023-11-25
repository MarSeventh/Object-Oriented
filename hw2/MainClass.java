import java.util.Scanner;

public class MainClass {
    private static SelfDefine functionF;
    private static SelfDefine functionG;
    private static SelfDefine functionH;

    public static SelfDefine getFunction(String name) {
        if (name.equals("f")) {
            return functionF;
        } else if (name.equals("g")) {
            return functionG;
        } else {
            return functionH;
        }
    }

    public static void iniFunction(String function) {
        if (function.indexOf('f') != -1) {
            functionF = new SelfDefine(function);
        } else if (function.indexOf('g') != -1) {
            functionG = new SelfDefine(function);
        } else if (function.indexOf('h') != -1) {
            functionH = new SelfDefine(function);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String count = scanner.nextLine();
        for (int i = 0; i < Integer.parseInt(count); i++) {
            String function = scanner.nextLine();
            iniFunction(function);
        }
        String origin = scanner.nextLine();
        StringSimplify st = new StringSimplify(origin);
        String expression = st.simplify();
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        System.out.println(expr.simplify(expr));
    }
}

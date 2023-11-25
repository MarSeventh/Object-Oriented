import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String origin = scanner.nextLine();
        String expression = origin.replaceAll("[ \t]", "");
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                sb.append('(');
                if (expression.charAt(i + 1) == '+') {
                    flag = 1;
                }
            } else if (flag == 0) {
                sb.append(expression.charAt(i));
            } else {
                flag = 0;
            }
        }
        expression = sb.toString();
        expression = expression.replaceAll("(\\+\\+)|(--)", "+");
        expression = expression.replaceAll("\\*\\*", "^");
        expression = expression.replaceAll("(-\\+)|(\\+-)", "-");
        expression = expression.replaceAll("\\^\\+", "^");
        expression = expression.replaceAll("\\*\\+", "*");
        expression = expression.replaceAll("^\\+", "");
        expression = expression.replaceAll("(\\+\\+)|(--)", "+");
        expression = expression.replaceAll("(-\\+)|(\\+-)", "-");
        expression = expression.replaceAll("^\\+", "");
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);

        Expr expr = parser.parseExpr();
        expr.simplify(expr);
    }
}

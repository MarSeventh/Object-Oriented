import Factor.Expr;
import Parser.Lexer;
import Parser.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Lexer lexer = new Lexer(scanner.nextLine());
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        //先展开再求导
        System.out.println(expr.expand().derive());
    }
}
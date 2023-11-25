public class StringSimplify {
    private final String origin;

    public StringSimplify(String origin) {
        this.origin = origin;
    }

    public String simplify() {
        String expression = origin.replaceAll("[ \t]", "");
        expression = expression.replaceAll("\\(\\+", "(");
        expression = expression.replaceAll("(\\+\\+)|(--)", "+");
        expression = expression.replaceAll("\\*\\*", "^");
        expression = expression.replaceAll("(-\\+)|(\\+-)", "-");
        expression = expression.replaceAll("\\^\\+", "^");
        expression = expression.replaceAll("\\*\\+", "*");
        expression = expression.replaceAll("^\\+", "");
        expression = expression.replaceAll("(\\+\\+)|(--)", "+");
        expression = expression.replaceAll("(-\\+)|(\\+-)", "-");
        expression = expression.replaceAll("^\\+", "");
        return expression;
    }
}

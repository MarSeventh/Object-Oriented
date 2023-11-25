public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            ++pos;
        }

        return sb.toString();
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber(); // first
        } else if ("()+-*^,!@#".indexOf(c) != -1) { // second
            pos += 1;
            curToken = String.valueOf(c);
        } else if (c == 's' || c == 'c') {
            pos += 4;
            if (c == 's') {
                curToken = "sin";
            } else {
                curToken = "cos";
            }
        } else if (Character.isLetter(c)) {
            pos += 1;
            curToken = String.valueOf(c);
        }
    }

    public String peek() {
        return this.curToken;
    }
}

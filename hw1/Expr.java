import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public void simplify(Expr expr) {
        String exp = expr.toString();
        ArrayList<String> term = new ArrayList<>();
        Collections.addAll(term, exp.split("\\+"));
        HashMap<String, BigInteger> finalexp = new HashMap<>();
        finalexp.put("1", new BigInteger("0"));
        for (String s : term) {
            ArrayList<String> factor = new ArrayList<>();
            Collections.addAll(factor, s.split("\\*"));
            HashMap<String, BigInteger> map = new HashMap<>();
            BigInteger sum = new BigInteger("1");
            map.put("x", new BigInteger("0"));
            map.put("y", new BigInteger("0"));
            map.put("z", new BigInteger("0"));
            for (String value : factor) {
                if ("xyz".indexOf(value.charAt(0)) == -1) {
                    sum = sum.multiply(new BigInteger(value));
                } else {
                    map.put(value, map.get(value).add(new BigInteger("1")));
                }
            }
            StringBuilder sb = new StringBuilder();
            int flag = 0;
            if (!map.get("x").equals(new BigInteger("0"))) {
                flag = 1;
                sb = join(sb, map, "x");
            }
            if (!map.get("y").equals(new BigInteger("0"))) {
                if (flag == 1) {
                    sb.append("*");
                }
                flag = 1;
                sb = join(sb, map, "y");
            }
            if (!map.get("z").equals(new BigInteger("0"))) {
                if (flag == 1) {
                    sb.append("*");
                }
                flag = 1;
                sb = join(sb, map, "z");
            }
            if (flag == 0) {
                finalexp.put("1", finalexp.get("1").add(sum));
            } else {
                if (finalexp.containsKey(sb.toString())) {
                    finalexp.put(sb.toString(), finalexp.get(sb.toString()).add(sum));
                } else {
                    finalexp.put(sb.toString(), sum);
                }
            }
        }
        merge(finalexp);
    }

    public StringBuilder join(StringBuilder sb, HashMap<String, BigInteger> map, String str) {
        sb.append(str);
        if (!map.get(str).equals(new BigInteger("1"))) {
            if (!map.get(str).equals(new BigInteger("2"))) {
                sb.append("**");
                sb.append(map.get(str));
            } else {
                sb.append("*");
                sb.append(str);
            }
        }
        return sb;
    }

    public void merge(HashMap<String, BigInteger> finalexp) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = finalexp.keySet().iterator();
        int flag = 0;//change 1
        while (iterator.hasNext()) {
            String next = iterator.next();
            StringBuilder newsb = new StringBuilder();
            if (next.equals("1")) {
                if (!finalexp.get(next).equals(new BigInteger("0"))) {
                    if (finalexp.get(next).compareTo(new BigInteger("0")) > 0 && flag == 0) {
                        newsb.append(finalexp.get(next)).append("+");
                        for (int m = 0; m < sb.length(); m++) {
                            newsb.append(sb.charAt(m));
                        }
                        sb = newsb;
                        flag = 1;//change 3
                    } else {
                        sb.append(finalexp.get(next)).append("+");
                    }
                }
            } else {
                if (!finalexp.get(next).equals(new BigInteger("0"))) {
                    if (finalexp.get(next).compareTo(new BigInteger("0")) > 0 && flag == 0) {
                        if (!finalexp.get(next).equals(new BigInteger("1"))) {
                            newsb.append(finalexp.get(next)).append("*");
                        }
                        newsb.append(next).append("+");
                        for (int m = 0; m < sb.length(); m++) {
                            newsb.append(sb.charAt(m));
                        }
                        sb = newsb;
                        flag = 1;//change 5
                    } else {
                        if (!finalexp.get(next).equals(new BigInteger("1"))) {
                            if (!finalexp.get(next).equals(new BigInteger("-1"))) {
                                sb.append(finalexp.get(next)).append("*");
                            } else {
                                sb.append("-");
                            }
                        }
                        sb.append(next).append("+");
                    }
                }
            }
        }
        if (sb.toString().equals("")) {
            sb.append("0");
        }
        if (sb.toString().endsWith("+")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String finalstr = sb.toString();
        finalstr = finalstr.replaceAll("(-\\+)|(\\+-)", "-");
        System.out.println(finalstr);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!terms.isEmpty()) {
            for (int i = 0; i < terms.size(); i++) {
                if (i > 0) {
                    sb.append("+");
                }
                sb.append(terms.get(i));
            }
        }
        return sb.toString();
    }
}

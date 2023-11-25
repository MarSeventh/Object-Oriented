import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class Expr implements Factor {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public String simplify(Expr expr) {
        HashMap<String, BigInteger> finalexp = new HashMap<>();
        finalexp.put("1", new BigInteger("0"));
        ArrayList<String> doublesin = new ArrayList<>();
        ArrayList<HashMap<String, BigInteger>> indexsin = new ArrayList<>();
        ArrayList<String> doublecos = new ArrayList<>();
        ArrayList<HashMap<String, BigInteger>> indexcos = new ArrayList<>();
        for (String s : getTerm(expr.toString())) {
            HashMap<String, BigInteger> map = new HashMap<>();
            getFactor(s, map);
            triMatchPro(doublesin, indexsin, doublecos, indexcos, map, finalexp);
            String term = joinMap(map);
            addFinal(term, map, finalexp);
        }
        return merge(finalexp);
    }

    public void triMatchPro(ArrayList<String> doublesin,
                            ArrayList<HashMap<String, BigInteger>> indexsin,
                            ArrayList<String> doublecos,
                            ArrayList<HashMap<String, BigInteger>> indexcos,
                            HashMap<String, BigInteger> map, HashMap<String, BigInteger> finalexp) {
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            BigInteger index = map.get(next);
            if (next.startsWith("s") && index.compareTo(new BigInteger("2")) >= 0) {
                int temp = triMatch(doublecos, indexcos, map, next, finalexp);
                if (temp != 1) {
                    if (!match(doublesin, indexsin, map, next)) {
                        doublesin.add(next);
                        indexsin.add(map);
                    }
                }
                if (temp != 0) {
                    triMatchPro(doublesin, indexsin, doublecos, indexcos, map, finalexp);
                }
            } else if (next.startsWith("c") && index.compareTo(new BigInteger("2")) >= 0) {
                int temp = triMatch(doublesin, indexsin, map, next, finalexp);
                if (temp != 1) {
                    if (!match(doublecos, indexcos, map, next)) {
                        doublecos.add(next);
                        indexcos.add(map);
                    }
                }
                if (temp != 0) {
                    triMatchPro(doublesin, indexsin, doublecos, indexcos, map, finalexp);
                }
            }
        }
    }

    public void deepCopy(HashMap<String, BigInteger> des,
                         HashMap<String, BigInteger> src) {
        for (String next : src.keySet()) {
            des.put(next, src.get(next));
        }
    }

    public int triMatch(ArrayList<String> name, ArrayList<HashMap<String, BigInteger>> index,
                        HashMap<String, BigInteger> maindex, String match,
                        HashMap<String, BigInteger> finalexp) {
        int flag = 0;
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).substring(3).equals(match.substring(3))) {
                if (triMapEqual(index.get(i), name.get(i), maindex, match)) {
                    flag = 1;
                    String change = joinMap(index.get(i));
                    if (finalexp.containsKey(change)) {
                        if (finalexp.get(change).compareTo(maindex.get("1")) > 0) {
                            finalexp.put(change, finalexp.get(change).subtract(maindex.get("1")));
                            maindex.put(match, maindex.get(match).subtract(new BigInteger("2")));
                        } else if (finalexp.get(change).compareTo(maindex.get("1")) < 0) {
                            flag = 2;
                            index.get(i).put(name.get(i),
                                    index.get(i).get(name.get(i)).subtract(new BigInteger("2")));
                            maindex.put("1", maindex.get("1").subtract(finalexp.get(change)));
                            index.get(i).put("1", finalexp.get(change));
                            finalexp.remove(change);
                            String newterm = joinMap(index.get(i));
                            addFinal(newterm, index.get(i), finalexp);
                            name.remove(i);
                            index.remove(i);
                            break;
                        } else {
                            maindex.put(match, maindex.get(match).subtract(new BigInteger("2")));
                            index.get(i).put(name.get(i),
                                    index.get(i).get(name.get(i)).subtract(new BigInteger("2")));
                            finalexp.remove(change);
                            name.remove(i);
                            index.remove(i);
                            break;
                        }
                    } else {
                        flag = 0;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public boolean match(ArrayList<String> name, ArrayList<HashMap<String, BigInteger>> index,
                         HashMap<String, BigInteger> maindex, String match) {
        boolean flag = false;
        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equals(match)) {
                if (triMapEqual(index.get(i), name.get(i), maindex, match)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean triMapEqual(HashMap<String, BigInteger> map1, String remove1,
                               HashMap<String, BigInteger> map2, String remove2) {
        HashMap<String, BigInteger> cmap1 = new HashMap<>();
        HashMap<String, BigInteger> cmap2 = new HashMap<>();
        for (String next : map1.keySet()) {
            cmap1.put(next, map1.get(next));
        }
        for (String next : map2.keySet()) {
            cmap2.put(next, map2.get(next));
        }
        cmap1.put(remove1, cmap1.get(remove1).subtract(new BigInteger("2")));
        cmap2.put(remove2, cmap2.get(remove2).subtract(new BigInteger("2")));
        return joinMap(cmap1).equals(joinMap(cmap2));
    }

    public void addFinal(String term, HashMap<String, BigInteger> map,
                         HashMap<String, BigInteger> finalexp) {
        if (map.size() > 1) {
            if (finalexp.containsKey(term)) {
                finalexp.put(term, finalexp.get(term).add(map.get("1")));
            } else {
                finalexp.put(term, map.get("1"));
            }
        } else {
            finalexp.put("1", finalexp.get("1").add(map.get("1")));
        }
    }

    public String joinMap(HashMap<String, BigInteger> map) {
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        TreeMap<String, BigInteger> sort = new TreeMap<>(map);
        for (String next : sort.keySet()) {
            if (map.get(next).equals(new BigInteger("0"))) {
                sb.append("");
            } else if (!next.equals("1")) {
                if (flag == 1) {
                    sb.append("*");
                }
                flag = 1;
                sb = join(sb, map, next);
            }
        }
        if (sb.length() == 0) {
            sb.append("1");
        }
        return sb.toString();
    }

    public ArrayList<String> getTerm(String exp) {
        ArrayList<String> term = new ArrayList<>();
        int last = 0;
        int count = 0;
        for (int i = 0; i < exp.length(); i++) {
            if (count == 0 && exp.charAt(i) == '+' && i > 0) {
                term.add(exp.substring(last, i));
                last = i + 1;
            } else if (exp.charAt(i) == '(') {
                count--;
            } else if (exp.charAt(i) == ')') {
                count++;
            }
        }
        term.add(exp.substring(last));
        return term;
    }

    public void getFactor(String s, HashMap<String, BigInteger> map) {
        ArrayList<String> factor = new ArrayList<>();
        int last = 0;
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (count == 0 && s.charAt(i) == '*' && i > 0) {
                factor.add(s.substring(last, i));
                last = i + 1;
            } else if (s.charAt(i) == '(') {
                count--;
            } else if (s.charAt(i) == ')') {
                count++;
            }
        }
        factor.add(s.substring(last));
        map.put("1", new BigInteger("1"));
        for (String value : factor) {
            if ("xyzsc".indexOf(value.charAt(0)) != -1) {
                if (map.containsKey(value)) {
                    map.put(value, map.get(value).add(new BigInteger("1")));
                } else {
                    map.put(value, new BigInteger("1"));
                }
            } else {
                map.put("1", map.get("1").multiply(new BigInteger(value)));
            }
        }
    }

    public StringBuilder join(StringBuilder sb, HashMap<String, BigInteger> map, String str) {
        sb.append(str);
        if (!map.get(str).equals(new BigInteger("1"))) {
            if (map.get(str).equals(new BigInteger("2")) && "sc".indexOf(str.charAt(0)) == -1) {
                sb.append("*");
                sb.append(str);
            } else {
                sb.append("**");
                sb.append(map.get(str));
            }
        }
        return sb;
    }

    public String merge(HashMap<String, BigInteger> finalexp) {
        StringBuilder sb = new StringBuilder();
        TreeMap<String, BigInteger> sort = new TreeMap<>(finalexp);
        Iterator<String> iterator = sort.keySet().iterator();
        int flag = 0;
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
                        flag = 1;
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
                        flag = 1;
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
        return finalstr;
    }

    public String reverse(String src) {
        int flag = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            if (flag == 0 && src.charAt(i) == '-') {
                if (i > 0) {
                    sb.append("+");
                }
            } else if (src.charAt(i) == '(') {
                sb.append("(");
                flag--;
            } else if (src.charAt(i) == ')') {
                sb.append(")");
                flag++;
            } else {
                sb.append(src.charAt(i));
            }
        }
        return sb.toString();
    }

    public boolean isExprFactor(String str) {
        boolean flag = false;
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (count == 0 && ("+-*".indexOf(str.charAt(i)) != -1) && i > 0) {
                flag = true;
                return flag;
            } else if (str.charAt(i) == '(') {
                count--;
            } else if (str.charAt(i) == ')') {
                count++;
            }
        }
        return flag;
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

package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleCharMatchAnti {
    private static void checkMatches(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.matches()) {
            System.out.println(context + "\tmatches: " + regex);
        } else {
            System.out.println(context + "\tnot matches: " + regex);
        }
    }

    public static void main(String[] args) {
        checkMatches("[^abc]", "a"); // a	not matches: [abc]
        checkMatches("[^abc]", "d"); // d matches: [abc]
        checkMatches("[^0-9]", "7"); // 7	not matches: [0-9]
        checkMatches("[^A-Z]", "B"); // B	not matches: [A-Z]
        checkMatches("[^a-zA-Z]", "y"); // y not matches: [a-zA-Z]
    }
}

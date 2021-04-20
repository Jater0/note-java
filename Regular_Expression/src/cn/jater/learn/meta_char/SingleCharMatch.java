package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleCharMatch {
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
        checkMatches("[abc]", "a"); // a	matches: [abc]
        checkMatches("[abc]", "d"); // d	not matches: [abc]
        checkMatches("[0-9]", "7"); // 7	matches: [0-9]
        checkMatches("[A-Z]", "B"); // B	matches: [A-Z]
        checkMatches("[a-zA-Z]", "y"); // y	matches: [a-zA-Z]
    }
}

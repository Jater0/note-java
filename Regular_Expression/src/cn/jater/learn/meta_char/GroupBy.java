package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupBy {
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
        checkMatches("(play|end)(ing|ed)", "ended"); // ended	matches: (play|end)(ing|ed)
        checkMatches("(play|end)(ing|ed)", "endings"); // endings	not matches: (play|end)(ing|ed)
        checkMatches("(play|end)(ing|ed)(s)", "endings"); // endings matches: (play|end)(ing|ed)(s)
        checkMatches("(play|end)(ing|ed)", "playing"); // playing	matches: (play|end)(ing|ed)
        checkMatches("(play|end)(ing|ed)", "played"); // played matches: (play|end)(ing|ed)
    }
}

package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LimitStringStartEnd {
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
        checkMatches("^app[a-z]{0,}", "apple"); // apple	matches: ^app[a-z]{0,}
        checkMatches("^app[a-z]{0,}", "aplause"); // aplause	not matches: ^app[a-z]{0,}

        checkMatches("[a-z]{0,}ing$", "playing"); // playing	matches: [a-z]{0,}ing$
        checkMatches("[a-z]{0,}ing$", "long"); // long	not matches: [a-z]{0,}ing$
    }
}

package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiSelect {
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
        checkMatches("yes|no", "yes"); // yes	matches: yes|no
        checkMatches("yes|no", "no"); // no matches: yes|no
        checkMatches("yes|no", "right"); // right not matches: yes|no
    }
}

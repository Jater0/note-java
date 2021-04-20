package cn.jater.learn.meta_char;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquivalentTypeChar {
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
        checkMatches(".{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_");
        checkMatches(".{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\\\");
        checkMatches(".", "\n");
        checkMatches("[^\\n]", "\n");

        checkMatches("\\w{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_");
        checkMatches("\\w{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\\\");

        checkMatches("\\W{1,}", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_");
        checkMatches("\\W{1,}", "~!@#$%^&*()+`-=[]{};:<>,./?|\\\\");

        checkMatches("\\s{1,}", "\f\r\n\t");
        checkMatches("\\S{1,}", "\f\r\n\t");
    }
}

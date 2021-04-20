package cn.jater.learn.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherCheckExample {

    private static void checkLookingAt(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.lookingAt()) {
            System.out.println(context + "\tlookingAt: " + regex);
        } else {
            System.out.println(context + "\tnot lookingAt: " + regex);
        }
    }

    private static void checkFind(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.find()) {
            System.out.println(context + "\tfind: " + regex);
        } else {
            System.out.println(context + "\tnot find: " + regex);
        }
    }

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
        String hello = "hello";
        String helloworld = "helloworld";
        String world = "world";

        checkLookingAt(hello, helloworld);
        checkLookingAt(world, helloworld);

        checkFind(hello, helloworld);
        checkFind(world, helloworld);

        checkMatches(hello, helloworld);
        checkMatches(world, helloworld);
        checkMatches(helloworld, helloworld);
    }
}

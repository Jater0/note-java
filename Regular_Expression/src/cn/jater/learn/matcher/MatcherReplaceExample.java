package cn.jater.learn.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherReplaceExample {

    private static void replaceFirstTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        System.out.println("replaceFirst: " + matcher.replaceFirst(replace));
    }

    private static void replaceAllTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        System.out.println("replaceAll: " + matcher.replaceAll(replace));
    }

    private static void appendTailNAppendReplacementTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, replace);
        }
        System.out.println("appendReplacement: " + sb);
        matcher.appendTail(sb);
        System.out.println("appendTail: " + sb);
    }

    private static void quoteReplacementTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        String replaceAll = matcher.replaceAll(Matcher.quoteReplacement(replace));
        System.out.println("quoteReplacement: " + replaceAll);
    }

    public static void main(String[] args) {
        String regex = "can";
        String replace = "can not";
        String content = "I can because i think i can";

        System.out.println("content: " + content);
        replaceFirstTest(regex, content, replace);
        replaceAllTest(regex, content, replace);
        appendTailNAppendReplacementTest(regex, content, replace);

        String regex2 = "\\$\\{.*?\\}";
        String replace2 = "${product}";
        String content2 = "product is ${productName}";
        System.out.println("\ncontent: " + content2);
        quoteReplacementTest(regex2, content2, replace2);
        replaceAllTest(regex2, content2, replace2);
    }
}

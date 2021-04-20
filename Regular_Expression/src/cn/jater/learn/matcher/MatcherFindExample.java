package cn.jater.learn.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherFindExample {
    public static void main(String[] args) {
        final String regex = "world";
        final String context = "hellworld helloworld";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        System.out.println("content: " + context);

        int i = 0;
        while (matcher.find()) {
            i++;
            System.out.println("[NO." + i + "] found");
            System.out.print("start: " + matcher.start() + ", ");
            System.out.print("end: " + matcher.end() + ", ");
            System.out.print("group: " + matcher.group() + "\n");
        }
    }
}
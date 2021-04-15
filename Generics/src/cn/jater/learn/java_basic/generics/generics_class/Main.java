package cn.jater.learn.java_basic.generics.generics_class;

public class Main {
    public static void main(String[] args) {
        Generic<Integer> genericInteger = new Generic<>(123456);
        Generic<String> genericString = new Generic<>("key_value");
        System.out.println("Generics Test: key = " + genericInteger.getKey());
        System.out.println("Generics Test: key = " + genericString.getKey());
    }
}

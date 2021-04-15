package cn.jater.learn.java_basic.generics.generics_wildcard;


import cn.jater.learn.java_basic.generics.generics_class.Generic;

public class Main {
    private static void showKeyValue(Generic<Number> obj) {
        System.out.println("Generics Test: key = " + obj.getKey());
    }

    private static void showKeyValue1(Generic<?> obj) {
        System.out.println("Generics Test: key = " + obj.getKey());
    }

    public static void main(String[] args) {
        Generic<Integer> integerGeneric = new Generic<>(123);
        Generic<Number> numberGeneric = new Generic<>(456);
        showKeyValue(numberGeneric);
//        showKeyValue(integerGeneric); 报错
        showKeyValue1(integerGeneric);
        showKeyValue1(numberGeneric);
    }
}

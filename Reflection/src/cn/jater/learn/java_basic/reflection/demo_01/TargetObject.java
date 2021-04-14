package cn.jater.learn.java_basic.reflection.demo_01;

public class TargetObject {
    private String value;

    public TargetObject() {
        value = "Jater";
    }

    void publicMethod(String str) {
        System.out.println("I am " + str);
    }

    private void privateMethod() {
        System.out.println("I am " + value);
    }
}

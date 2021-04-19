package cn.jater.learn.java_basic.keywords;

public class StaticInner {
    class InnerClass {

    }

    static class StaticInnerClass {

    }

    public static void main(String[] args) {
//        InnerClass innerClass = new InnerClass(); // 'StaticInner.class' cannot be referenced from a static context
        StaticInner staticInner = new StaticInner();
        InnerClass innerClass = staticInner.new InnerClass();
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }
}

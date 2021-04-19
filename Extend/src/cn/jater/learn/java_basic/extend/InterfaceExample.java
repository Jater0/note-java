package cn.jater.learn.java_basic.extend;

public interface InterfaceExample {
    void func1();

    default void func2() {
        System.out.println("func2");
    }

    int x = 123;

    public int z = 0;
}

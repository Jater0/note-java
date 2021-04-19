package cn.jater.learn.java_basic.extend;

public class AbstractExtendClassExample extends AbstractClassExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }

    public static void main(String[] args) {
        AbstractExtendClassExample a1 = new AbstractExtendClassExample();
        a1.func1();
        a1.func2();
    }
}

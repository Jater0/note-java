package cn.jater.learn.java_basic.extend;

public class SuperExtendExample extends SuperExample {
    private int z;

    public SuperExtendExample(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public void func() {
        super.func();
        System.out.println("SuperExtendExample.func()");
    }

    public static void main(String[] args) {
        SuperExtendExample se1 = new SuperExtendExample(1, 3, 5);
        se1.func();
    }
}
package cn.jater.learn.java_basic.keywords;

public class StaticVar {
    private int x = 10; // 实例变量
    private static int y = 20; // 静态变量

    public static void main(String[] args) {
        StaticVar staticVar = new StaticVar();
        int x = staticVar.x;
        int y = StaticVar.y;
        System.out.println(x+y);
    }
}

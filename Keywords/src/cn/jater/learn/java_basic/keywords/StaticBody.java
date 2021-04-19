package cn.jater.learn.java_basic.keywords;

public class StaticBody {
    static {
        System.out.println("123");
    }

    public static void main(String[] args) {
        StaticBody a1 = new StaticBody();
        StaticBody a2 = new StaticBody();
    }
}

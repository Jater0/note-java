package cn.jater.learn.java_basic.operation;

public class ImplicitTypeConversion {
    public static void main(String[] args) {
        short s1 = 1;
//        s1 = s1 + 1; // Incompatible types
        s1 += 1;
        s1++;
        System.out.println(s1);
        s1 = (short)(s1 + 1);
        System.out.println(s1);
    }
}

package cn.jater.learn.java_basic.object_method;

public class CloneExample {
    private int a;
    private int b;

    @Override
    protected CloneExample clone() throws CloneNotSupportedException {
        return (CloneExample)super.clone();
    }

    public static void main(String[] args) {
        CloneExample c1 = new CloneExample();
//        CloneExample c2 = c1.clone(); // 'clone()' has protected access in 'java.lang.Object'
        try {
            CloneExample c2 = c1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}

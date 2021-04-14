package cn.jater.learn.java_basic.reflection.demo_02;

import java.lang.reflect.Constructor;

public class Main {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("cn.jater.learn.java_basic.reflection.demo_02.Student");

        System.out.println("===== All Public Constructor =====");
        Constructor[] conArray = clazz.getConstructors();
        for (Constructor c: conArray) {
            System.out.println(c);
        }

        System.out.println("\n===== All Constructor =====");
        conArray = clazz.getDeclaredConstructors();
        for (Constructor constructor: conArray) {
            System.out.println(constructor);
        }

        System.out.println("\n===== Public & Non-parameter Constructor =====");
        Constructor con = clazz.getConstructor(null);
        System.out.println("con = " + con);

        Object obj = con.newInstance();
        System.out.println("obj = " + obj);
        Student stu = (Student)obj;

        System.out.println("\n===== All Private Constructor =====");
        con = clazz.getDeclaredConstructor(char.class);
        System.out.println(con);
        con.setAccessible(true);
        obj = con.newInstance('A');
        System.out.println(obj);
    }
}

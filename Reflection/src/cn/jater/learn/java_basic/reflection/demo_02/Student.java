package cn.jater.learn.java_basic.reflection.demo_02;

public class Student {
    Student(String str) {
        System.out.println("default constructor str = " + str);
    }

    public Student() {
        System.out.println("constructor");
    }

    public Student(char name) {
        System.out.println("name: " + name);
    }

    public Student(String name, int age) {
        System.out.println("name: " + name + ", age: " + age);
    }

    protected Student(boolean n) {
        System.out.println("protected Constructor n = " + n);
    }

    private Student(int age) {
        System.out.println("private Constructor age = " + age);
    }
}

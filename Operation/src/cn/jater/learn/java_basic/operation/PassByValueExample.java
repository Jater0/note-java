package cn.jater.learn.java_basic.operation;

public class PassByValueExample {
    private static void func(Dog dog) {
        dog.setName("B");
    }
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        func(dog);
        System.out.println(dog.getName()); // B
    }
}

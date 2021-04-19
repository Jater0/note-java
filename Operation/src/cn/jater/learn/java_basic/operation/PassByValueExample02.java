package cn.jater.learn.java_basic.operation;

public class PassByValueExample02 {
    private static void func(Dog dog) {
        System.out.println(dog.getObjectAddress()); // @1b6d3586
        dog = new Dog("B");
        System.out.println(dog.getObjectAddress()); // @4554617c
        System.out.println(dog.getName()); // B
    }
    public static void main(String[] args) {
        Dog dog = new Dog("A");
        System.out.println(dog.getObjectAddress()); // @1b6d3586
        func(dog);
        System.out.println(dog.getObjectAddress()); // @1b6d3586
        System.out.println(dog.getName()); // A
    }
}

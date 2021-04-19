package cn.jater.learn.java_basic.extend;

public class OverrideExample {
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        C c = new C();
        D d = new D();
        // 在A中存在show(A obj), 直接调用
        a.show(a); // A.show(A)
        // 在A中不存在show(B obj), 将B转型成其父类A
        a.show(b); // A.show(A)
        // 在B中存在从A继承来的show(C obj), 直接调用
        b.show(c); // A.show(C)
        // 在B中不存在show(D obj), 但是存在从A继承来的show(C obj), 将D转型成其父类C
        b.show(d); // A.show(C)

        // 引用的还是B对象, 所以ba和b的调用结果相同
        A ba = new B();
        ba.show(c); // A.show(C)
        ba.show(d); // A.show(C)
    }
}

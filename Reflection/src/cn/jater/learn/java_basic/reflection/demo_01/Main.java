package cn.jater.learn.java_basic.reflection.demo_01;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        /*
         * 获取TargetObject类的class对象并且创建TargetObject类实例
         */
        Class<?> targetClass = Class.forName("cn.jater.learn.java_basic.reflection.demo_01.TargetObject");
        TargetObject targetObject = (TargetObject)targetClass.newInstance();
        /*
         * 获取类中所有的方法.
         */
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method: methods) {
            System.out.println(method.getName());
        }
        /*
         * 获取指定方法并调用
         */
        Method publicMethod = targetClass.getDeclaredMethod("publicMethod", String.class);
        publicMethod.invoke(targetObject, "Jater");
        /*
         * 获取指定参数并对参数进行修改
         */
        Field field = targetClass.getDeclaredField("value");
        // 取消安全检查
        field.setAccessible(true);
        field.set(targetObject, "Jater");
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetObject);
        System.out.println();
        Class<?> targetObjectClass = TargetObject.class;
        Method publicMethod2 = targetObjectClass.getDeclaredMethod("publicMethod", String.class);
        publicMethod2.invoke(targetObject, "Jater");
    }
}

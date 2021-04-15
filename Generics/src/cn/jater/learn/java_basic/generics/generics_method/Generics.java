package cn.jater.learn.java_basic.generics.generics_method;

public class Generics {
    private static <T> T genericMethod(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object obj = genericMethod(Class.forName("cn.jater.learn.java_basic.generics.generics_method.Generics"));
        System.out.println(obj);
    }
}

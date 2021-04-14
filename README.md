# Java Basic

## Reflection(反射机制)

#### 介绍

**Java反射机制是在运行状态中,对于任意一个类,都能够知道这个类的所有属性和方法;对于任意一个对象,都能够调用它的任意一个方法和属性;这种动态获取的信息以及动态调用对象的方法的功能叫做Java的反射机制**



#### 获取Class对象的两种方式

1. **知道具体类的情况下可以使用**

``` java
Class targetObjectClass = TargetObject.class
```

2. **通过`Class.forName()`传入类的路径获取**

``` java
Class targetObjectClass  = Class.forName("cn.jater.reflection.TargetObject")
```



#### 实例

1. **`TargetObject`**

```Java
public class TargetObject {
    private String value;

    public TargetObject() {
        value = "Jater";
    }

    void publicMethod(String str) {
        System.out.println("I am " + str);
    }

    private void privateMethod() {
        System.out.println("I am " + value);
    }
}
```

2. **反射操作`TargetObject`**

```Java
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
```



#### 静态编译和动态编译

1. **静态编译**: **在编译时确定类型,绑定对象**
2. **动态编译**: **运行时确定类型,绑定对象**



#### 反射机制优缺点

##### 优点

- **运行期类型的类型判断,动态加载类,提高代码灵活性**

##### 缺点

- **性能瓶颈: 反射相当于一系列解释操作,通知JVM要做的事情,性能比直接的Java代码要慢很多**
- **安全问题: 让我们动态操作改变类的属性同时也增加了类的安全隐患**



#### 反射的应用场景

<u>***反射是框架设计的灵魂***</u>

**例如:模块化的开发,通过反射去调用对应的字节码; 动态代理设计模式也采用了反射机制,还有Spring/Hibernate等框架也大量使用了反射机制**

1. **当使用JDBC链接数据库时使用`Class.forName()`通过反射加载数据库的驱动程序**
2. **Spring框架的IoC创建对象以及AOP功能都和反射有联系**
3. **动态配置实例的属性**
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



## Annotation(注解)

#### 介绍

**Annotation是Java5引入的新特性**

**它提供了一种安全的类似注释的机制, 用来将任何的信息或元数据(`metadata`)与程序元素(Class, function, variable等)进行关联.**

**为程序的元素加上更直观和更明了的说明,这些说明信息是与程序的业务逻辑无关,并且供指定的工具或框架使用**

**Annotation像一种修饰符一样,应用于包,类型,构造方法,方法,成员变量,参数以及本地变量的声明语句中.**

-----

#### 用处

1. **生成文档: 最常见, 也是Java最早提供的注解. 常用的有@param @return等**
2. **跟踪代码依赖性, 实现替代配置文件功能. 比如Dagger 2 依赖注入**
3. **在编辑时进行格式检查. 如@Override**

-----

#### 原理

​	**注解本质是一个继承Annotation的特殊接口，其具体实现类是Java运行时生成的动态代理类。而我们通过反射获取注解时， 返回的是Java运行时生成的动态代理对象`$Proxy1`。**

​	**通过代理对象调用自定义的注解(接口)的方法，会最终调用`AnnotationInvocationHandler`的invoke方法。该方法会从`memberValues`这个Map中索引出对应的值。而`memberValues`的来源是Java常量池**

-----

#### 元注解

**`@Docuemnted`： 注解是否将包含到Java Doc中**

**`@Retention`： 什么时候使用该注解**

**`@Target`： 注解用于什么地方**

**`@Inherited`： 是否允许子类继承该注解**



1. **`@Retention`： 定义该注解的生命周期**

   - **`RetentionPolicy.SOURCE`：在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。`@Override`， `@SuppressWarning`都属于这类注解**
   - **`RetentionPolicy.CLASS`：在类加载阶段丢弃。在字节码文件的处理中有用。注解默认使用这种方式**
   - **`RetentionPolicy.RUNTIME`：始终不丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息**

2. **`@Target`： 表示该注解用于什么地方。默认值为任何元素，表示该注解用于什么地方。可用的`ElementType`参数包括**

   - **`ElementType.CONSTRUCTOR`：用于构造器**
   - **`ElementType.FIELD`： 成员变量、对象、属性(包括enum实例)**
   - **`ElementType.LOCAL_VARIABLE`：用于描述局部变量**
   - **`ElementType.METHOD`：用于描述方法**
   - **`ElementType.PACKAGE`：用于描述包**
   - **`ElementType.PARAMETER`：用于描述参数**
   - **`ElementType.TYPE`：用来描述类、接口(包括注解类型)或enum声明**

3. **`@Documented`：一个简单的Annotation标记注解，表示是否将注解信息添加在Java文档中**

4. **`@Inherited`：表示该注释和子类的关系**

   **`@Inherited` 元注解是一个标记注解，`@Inherited` 阐述了某个被标注的类型是被继承的。如果一个使用了`@Inherited` 修饰的annotation 类型被用于一个class，则这个annotation 将被用于该class 的子类。**

-----

#### 自定义注解

##### 规则

1. **Annotation型定义为@interface，所有的Annotation会自动继承java.lang.Annotation这一接口，并且不能再去继承别的类或是接口**
2. **参数成员只能用public或默认(default)这两个访问权修饰**
3. **参数成员只能用基本类型byte、short、char、int、long、float、double、boolean八种基本数据类型和String、Enum、Class、annotation等数据类型，以及这些类型的数组**
4. **要获取类方法和字段的注解信息，必须通过Java的反射技术来获取Annotation对象，因此没有别的获取注解对象的方法**
5. **注解也可以没有成员对象，不过这样注解就没啥用了**

##### 案例

###### `FruitName`

```Java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}
```

###### `FruitColor`

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {
    enum Color{
        BlUE, RED, GREEN
    }

    Color fruitColor() default Color.GREEN;
}
```

###### `FruitProvider`

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitProvider {
    int id() default -1;
    String name() default "";
    String address() default "";
}
```

###### `Apple`

```java
public class Apple {
    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

    @FruitProvider(id = 1, name = "HHH", address = "China")
    private String appleProvider;

    public String getAppleName() {
        return appleName;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    public String getAppleProvider() {
        return appleProvider;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }
}
```

###### `Main`

```java
public class Main {
    public static void main(String[] args) {
        FruitInfoUtil.getFruitInfo(Apple.class);
    }
}
```

###### `Output`

![实例输出](\images\annotation-output.png)
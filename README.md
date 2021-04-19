# Java Basic

## 一、Data Type(数据类型)

#### 基础类型

- byte
- char
- short
- int
- float
- long
- double
- boolean

boolean只有两个值：true、false，可以使用1bit来存储，但是具体大小没有明确规定。JVM会在编译时期将boolean类型的数据转换成int，使用1来表示true，0表示false。

JVM支持boolean数组，但是是通过读写byte数组来实现的

-----

#### 包装类型



-----

#### 缓存池

##### **`new Integer()`与`Integer.valueOf()`的区别在于**

- **`new Integer()`每次都会新建一个对象**
- **`Integer.valueOf()`会使用缓存池中的对象，多次调用**

``` java
Integer x = new Integer(123);
Integer y = new Integer(123);
System.out.println(x == y); // false
Integer z = Integer.valueOf(123);
Integer k = Integer.valueOf(123);
System.out.println(z == k); // true
```

##### **`valueOf()`方法的实现**

**先判断值是否在缓存池中，如果在的话就直接返回缓存池的内容。**

``` java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCahce.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

**Java8中，Integer缓存池的大小默认为-128~127**

##### Integer缓存池的实现

``` java
static final int low = -128;
static final int high;
static final Integer cache[];

static {
    int h = 127;
    String integerCacheHighPropValue = sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
    if (integerCacheHighPropValue != null) {
        try {
            int i = parseInt(integerCacheHighPropValue);
            i = Math.max(i, 127);
            // Maximum array size is Integer.MAX_VALUE
            h = Math.min(i, Integer.MAX_VALUE - (-low) - 1);
        } catch (NumberFormatException nfe) {
            // If the property cannot be parsed into an int, ignore it.
        }
    } 
    high = h;
    cache = new Integer[(high - low) + 1];
    int j = low;
    for (int k = 0; k < cache.length; k++) {
        cache[k] = new Integer(j++);
    }
    // range [-128, 127] must be interned (JLS7 5.1.7)
    assert IntegerCache.high >= 127;
}
```

**编译器会在自动装箱过程调用valueOf()方法，因此多个值相同且值在缓存池范围内的Integer实例自动装箱来创建，那么就会引用相同的对象**

``` java
Integer m = 123;
Integer n = 123;
System.out.println(m == n);
```

##### 基本类型对应的缓存池如下

- boolean values true & false
- all byte values
- short values between -128 ~ 127
- int values between -128 ~ 127
- char in the range \u0000 to \u007F

**在jdk1.8所有的数组类缓存池中，Integer的缓存池`IntegerCache`很特殊，这个缓存池的下界是-128，上界默认是127，但是这个上界是可调的，在启动JVM的时候，通过`-XX:AutoBoxCacheMax=<size>`来指定这个缓存池的大小，该选项在JVM初始化的时候会设定一个名为`java.lang.IntegerCache.high`系统属性，然后`IntegerCache`初始化的时候就会读取该系统属性来决定上界**

-----



## 二、String

#### 概览

**String被声明为final，因此它不可被继承，(Integer等包装类也不能被继承)**

**Java 8中，String内部使用char数组存储数据**

``` java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
    // The value is use for character storage
    private final char value[];
}
```

**Java 9之后，String类的实现改用byte数组存储字符串，同时使用`coder`来标识使用了哪种编码**

``` java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
    // The value is use for character storage
    private final byte[] value;
    
    // The identifier of the encoding used to encode the bytes in @{code value}
    private final byte coder;
}
```

**value数组被声明为final，这意味着value数组初始化之后就不能再引用其他数组。并且String内部没有改变value数组的方法，因此可以保证String不可变**

-----

#### 不可变的好处

1. **可以缓存hash值**

因为String的hash值经常被使用，例如String用做HashMap的key。不可变的特定可以使得hash值不变，因此只需要进行一次计算。

2. **String Pool的需要**

如果一个String对象已经被创建过了，那么就会从String Pool中取得引用。只有String是不可变的，才可能使用String Pool。

![image-20210415094617120](images\string-pool.png)

3. **安全性**

String经常作为参数，String不可变性可以保证参数不可变。Such as：在作为网络连接参数的情况下，如果String是可变的，那么在网络连接过程中，String被改变，改变String的那一方以为现在连接的是其他主机，而实际情况却不一定是。

4. **线程安全**

String不可变性天生具有线程安全，可以在多个线程中安全地使用。

-----

#### String、StringBuffer and StringBuilder

##### 可变性

- String不可变
- StringBuffer & StringBuilder可变

##### 线程安全

- StringBuilder线程不安全
- StringBuffer线程安全，内部使用synchronized 进行同步

-----

#### String Pool(字符串常量池)

**String Pool保存着所有字符串字面量(literal strings)， 这些字面量在编译时期就确定。不仅如此，还可以使用String的intern方法在运行过程中将字符串添加到String Pool中。**

**当一个字符串调用intern方法时，如果String Pool中已经存在一个人字符串和该字符串值相等(使用equals方法进行确定)，那么就会返回String Pool中字符串的引用；否则，就会在String Pool中添加一个新的字符串，并返回这个新字符串的引用。**

``` java
String s1 = new String("aaa");
String s2 = new String("aaa");
System.out.println(s1 == s2); // false
String s3 = s1.intern();
String s4 = s2.intern();
System.out.println(s3 == s4); // true
```

> **s1和s2采用了new String()的方式创建两个不同字符串，**
>
> **而s3和s4是通过`s1.intern()`和`s2.intern()`方法获取的同一个字符串的引用。**
>
> **intern()首先将"aaa"存入String Pool中，然后返回这个字符串的引用，因此s3和s4引用的是同一个字符串。**



**如果是采用“bbb”这种字面量的形式创建字符串，会自动地将字符串存入String Pool中。**

``` java
String s5 = "bbb";
String s6 = "bbb";
System.out.prinln(s5 == s6); // true
```

**在Java 7之前，String Pool被放在运行时常量池中，它属于永久代。**

**而在Java 7， String Pool被移到堆中。这是因为永久代空间有限，在大量使用字符串的场景下会导致`OutOfMemoryError`错误**

[深入解析String#intern](https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html)

-----

#### new String("abc")

**使用这种方式一共会创建两个字符串对象(前提是String Pool中还没有“abc”这个字符串对象)**

- **“abc”属于字符串字面量，因此编译时期会在String Pool中创建一个字符串对象，指向这个“abc”字符串字面量；**
- **而使用new的方式会在堆中创建一个字符串对象**

**例子**

``` java
public class StringMain {
    public static void main(String[] args) {
        String s1 = new String("aaa");
    }
}
```

**javap -verbose进行反编译**

![反编译](images\string-pool-runtime.png)

在Constant Pool中，#23存储这个字符串字面量“abc”，#3是String Pool的字符串对象，它指向#23这个字面量。

在main方法中，0:行使用new #2在堆中创建一个字符串对象，并且使用ldc #3将String Pool中的字符串对象作为String构造函数的参数。

###### String构造函数源码

在将一个字符串作为另一个字符串对象的构造函数参数时，并不会完全复制value数组，而是都会指向同一个value数组。

``` java
public String(String original) {
    this.value = original.value;
    this.hash = original.hash;
}
```

-----



## 三、Operation(运算)

#### 参数传递

Java的参数是以值传递的形式存入方法中,而不是引用传递.

以下代码中的Dog dog的dog是一个指针,存储的是对象的地址。在将一个参数传入一个方法时，本质上是将对象的地址以值的方式传递到形参中。

```Java
public class Dog {
    String name;

    Dog(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getObjectAddress() {
        return super.toString();
    }
}
```

在方法中改变对象的字段值会改变原对象该字段值，因为引用的是同一个对象

```Java
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
```

但是在方法中将指针引用了其他对象，那么此时方法里和方法外的两个指针指向了不同的对象，在一个指针改变其所指向对象的内容对另一个指针所指向的对象没有影响。

```Java
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
```

[StackOverflow: Is Java “pass-by-reference” or “pass-by-value”?](https://stackoverflow.com/questions/40480/is-java-pass-by-reference-or-pass-by-value)

-----

#### float & double

Java不能隐式执行向下转型，因为这样会使得精度降低。

1.1字面量属于double类型，不能直接将1.1赋值给float变量，因为这是向下转型。

``` java
// float f = 1.1;
```

1.1f字面量才是float类型

``` java
float f = 1.1f;
```

-----

#### 隐式类型转换

因为字面量1是Int类型，它比short类型精度更高，因此不能隐式地将int类型向下转型为short类型。

``` java
short s1 = 1;
// s1 = s1 + 1; // Incompatible types
```

但是使用+=或者是++运算符会执行隐式类型转换。

```Java
s1 += 1;
s1++;
```

上面的语句相当于将s1 + 1的计算结果进行了向下转型：

```java
s1 = (short)(s1 + 1);
```

[StackOverflow : Why don't Java's +=, -=, *=, /= compound assignment operators require casting?](https://stackoverflow.com/questions/8710619/why-dont-javas-compound-assignment-operators-require-casting)

-----

#### Switch

在java 7开始，可以在switch条件判断语句中使用String对象。

``` java
String s = "a";
switch (s) {
    case "a":
        System.out.println("aaa");
        break;
    case "B":
        System.out.println("BBB");
        break;
}
```

switch不支持long、float、double，是因为switch的设计初衷是对那些只有少数几个值的类型进行等值判断，如果值过于复杂，那么还是if比较合适。

[StackOverflow : Why can't your switch statement data type be long, Java?](https://stackoverflow.com/questions/2676210/why-cant-your-switch-statement-data-type-be-long-java)

-----



## 四、Keywords(关键字)

#### final

##### 1. 数据

声明数据为常量，可以是编译时变量，也可以是在运行时被初始化后不能被改变的常量。

- 对于基本类型，final使数值不变；
- 对于引用类型，final使引用不变，也就不用引用其他对象，但是被引用的对象本身是可以修改的。

```java
final int x = 1;
// x = 2; // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```

##### 2. 方法

声明方法不能被子类重写。

private方法隐式地被指定为final，如果在子类中定义的方法和基类中的一个private方法签名相同，此时子类的方法不能重写基类方法，而是在子类中定义了一个新方法。

##### 3. 类

声明类不允许被继承。

-----

#### static

##### 1. 静态变量

- 静态变量：又称为类变量，也就是说这个变量属于类的，类所有的实例都共享静态变量，可以直接通过类名来访问它。静态变量在内存中只存在一份。
- 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

```Java
public class StaticVar {
    private int x = 10; // 实例变量
    private static int y = 20; // 静态变量

    public static void main(String[] args) {
        StaticVar staticVar = new StaticVar();
        int x = staticVar.x;
        int y = StaticVar.y;
        System.out.println(x+y);
    }
}
```

##### 2. 静态方法

静态方法在类加载的时候就存在了，它不依赖于任何实例。所以静态方法必须有实现，也就是说它不能是抽象方法。

``` java
public abstract class A {
    public static void func1() {}
    public abstract static void func02(); // Illegal combination of modifiers: "abstract" and "static"
}
```

只能访问所属类的静态字段和静态方法，方法中不能有this和super关键字，因此这两个关键字与具体对象关联。

``` java
public class A{
    private static int x;
    private int y;
    
    public static void func1() {
        int a = x;
        // int b = y; // Non-static field 'y' cannot be referenced from a static context;
        // int b = this.y; // 'A.this' cannot be referenced from a static context
	}
}
```

##### 3. 静态语句块

静态语句块在类初始化时运行一次。

```java
public class StaticBody {
    static {
        System.out.println("123");
    }

    public static void main(String[] args) {
        StaticBody a1 = new StaticBody();
        StaticBody a2 = new StaticBody();
    }
}
```

```sh
123
```

##### 4. 静态内部类

非静态内部类依赖于外部类的实例，也就是说需要先创建外部类实例，才能用这个实例去创建非静态内部类。而静态内部类不需要。

```java
public class StaticInner {
    class InnerClass {

    }

    static class StaticInnerClass {

    }

    public static void main(String[] args) {
//        InnerClass innerClass = new InnerClass(); // 'StaticInner.class' cannot be referenced from a static context
        StaticInner staticInner = new StaticInner();
        InnerClass innerClass = staticInner.new InnerClass();
        StaticInnerClass staticInnerClass = new StaticInnerClass();
    }
}
```

静态内部类不能访问外部类的非静态的变量和方法。

##### 5. 静态导包

在使用静态变量和方法时不用再指明ClassName，从而简化代码，但可读性大大降低。

```java
import static com.xxxx.ClassName.*
```

##### 6. 初始化顺序

静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。

``` java
public static String staticField = "static field";
```

``` java
static {
    Sysout.out.println("static body")
}
```

``` java
public String field = "simple field"
```

``` java
{
    System.out.println("simple body");
}
```

最后才是构造函数的初始化

``` java
public InitialOrderTest() {
    System.out.println("constructor");
}
```

存在继承的情况下，初始化顺序为：

- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）



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

-----



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


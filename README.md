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

-----

## 五、Object通用方法

#### 概览

```java
public native int hashCode()

public boolean equals(Object obj)

protected native Object clone() throws CloneNotSupportedException

public String toString()

public final native Class<?> getClass()

protected void finalize() throws Throwable {}

public final native void notify()

public final native void notifyAll()

public final native void wait(long timeout) throws InterruptedException

public final void wait(long timeout, int nanos) throws InterruptedException

public final void wait() throws InterruptedException
```

----

#### equals()

##### 1. 等价关系

两个对象具有等价关系，需要满足以下五个条件：

- **自反性**

``` java
x.equals(x); // true
```

- **对称性**

``` java
x.equals(y) == y.equals(x); // true
```

- **传递性**

``` java
if (x.equals(y) && y.equals(z)) {
    x.equals(z); // true
}
```

- **一致性**

多次调用equals()方法结果不变

``` java
x.equals(y) == x.equals(y); // true
```

- **与null的比较**

对任何不是null的对象x调用x.equals(null)结果都为false

``` java
x.equals(null); // false
```

##### 2. 等价与相等

- **对于基本类型，== 判断两个值是否相等，基本类型没有equals()方法**
- **对于引用类型， == 判断两个值是否引用同一个对象，而equals()判断引用的对象是否等价**

``` java
Integer x = new Integer(1);
Integer y = new Integer(2);
sout(x.equals(y)); // true
sout(x == y); // false
```

##### 3. 实现

- **检查是否为同一个对象的引用，如果是，直接返回false**
- **检查是否为同一类型，如果不是，直接返回false**
- **将Object对象进行转型**
- **判断每个关键域是否相等**

``` java
public class EqualExample {
    private int x;
    private int y;
    private int z;
    
    public EqualExample(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EqualExample that = (EqualExample)obj;
        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }
}
```

-----

#### hashCode()

hashCode()返回哈希值，而equals()是用来判断两个对象是否等价。等价的两个对象散列值一定相同，但是散列值相同的两个对象不一定等价，这是因为计算哈希值具有随机性，两个值不同的对象可能计算出相同的哈希值。

在覆盖equals()方法时应当总是覆盖hashCode()方法，保证等价的两个对象哈希值也相等。

HashSet和HashMap等集合类使用了hashCode()方法来计算对象应该存储的位置，因此要将对象添加到这些集合类中，需要让对象的类实现hashCode方法。

下面的代码中，新建了两个等价的对象，并将它们添加到HashSet中。我们希望将这两个对象当初一样的，只在集合中添加一个对象。但是EqualExample没有实现hashCode方法，因此两个对象的哈希值不同，最终导致集合添加了两个等价的对象。

```Java
EqualExample e1 = new EqualExample(1, 1, 1);
EqualExample e2 = new EqualExample(1, 1, 1);
System.out.println(e1.equals(e2)); // true
HashSet<EqualExample> set = new HashSet<>();
set.add(e1);
set.add(e2);
System.out.println(set.size()); // 2
```

理想的哈希函数应当具有均匀性，即不相等的对象应当均匀分布到所有可能的哈希值上。这就要求了哈希函数要把所有域的值都考虑进去。可以将每个域当初R机制的某一位，然后组成一个R机制的整数。

R一般取31，因为它是一个寄素数，如果时偶数的话，当出现乘法溢出，信息就会丢失，因为与2相乘相当于向左移一位，最左边的位丢失。并且一个数与31相乘可以转换成移位和减法：`31 * x == (x<<5) - x`，编译器会自动进行这个优化。

``` java
@Override
public int hashCode() {
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + z;
    return result;
}
```

-----

#### toString()

默认返回ToStringExample@4554617c这种形式，其中@后面的数组为散列码的无符号十六进制表示。

``` java
public class ToStringExample {
    private int number;
    
    public ToStringExample(int number) {
        this.number = number;
    }
}
```

``` java
ToStringExample example = new ToStringExample(123);
sout(example.toString())
```

``` java
ToStringExample@4554617c
```

-----

#### Clone()

##### 1. cloneable

clone()是Object的protected方法，它不是public，一个类不显式去重写clone()，其它类就不能直接调用该类的实例的clone()方法。

``` java
public class CloneExample {
    private int a;
    private int b;
}
```

``` java
CloneExample c1 = new CloneExamle();
// CloneExample c2 = c1.clone(); // 'clone()' has protected access in 'java.lang.Object'
```

重写clone：

```java
@Override
protected CloneExample clone() throws CloneNotSupportedException {
    return (CloneExample)super.clone();
}
```

```Java
CloneExample c1 = new CloneExample();
try {
	CloneExample c2 = c1.clone();	
} catch (CloneNotSupportedException e) {
	e.printStackTrace();
}
```

``` java
java.lang.CloneNotSupportedException: CloneExample
```

以上抛出了CloneNotSupportedException，这是因为CloneExample没有实现Cloneable接口。

应该注意的是，clone方法并不是Cloneable接口的方法，而是Object的一个protected方法。Cloneable接口只是规定，如果一个类没有实现Cloneable接口又调用了clone方法，就会抛出CloneNotSupportedException。

```Java
public class CloneExample implements Cloneable {
    private int a;
    private int b;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

##### 2. 浅拷贝

拷贝对象和原始对象的引用类型引用同一个对象。

```Java
public class ShallowCloneExample implements Cloneable{
    private int[] arr;

    public ShallowCloneExample() {
        arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public void set(int index, int value) {
        arr[index] = value;
    }

    public int get(int index) {
        return arr[index];
    }

    @Override
    protected ShallowCloneExample clone() throws CloneNotSupportedException {
        return (ShallowCloneExample)super.clone();
    }

    public static void main(String[] args) {
        ShallowCloneExample e1 = new ShallowCloneExample();
        ShallowCloneExample e2 = null;

        try {
            e2 = e1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        e1.set(2, 222);
        System.out.println(e2.get(2)); // 222
    }
}
```

##### 3. 深拷贝

拷贝对象和原始对象的引用类型引用不同对象。

```Java
public class DeepCloneExample implements Cloneable {
    private int[] arr;

    public DeepCloneExample() {
        arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public void set(int index, int value) {
        arr[index] = value;
    }

    public int get(int index) {
        return arr[index];
    }

    @Override
    protected DeepCloneExample clone() throws CloneNotSupportedException {
        DeepCloneExample result = (DeepCloneExample)super.clone();
        result.arr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result.arr[i] = arr[i];
        }
        return result;
    }

    public static void main(String[] args) {
        DeepCloneExample e1 = new DeepCloneExample();
        DeepCloneExample e2 = null;
        try {
            e2 = e1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        e1.set(2, 222);
        System.out.println(e2.get(2)); // 2
    }
}
```

##### 4. clone()的替代方案

使用clone()方法来拷贝一个对象即复杂又有风险，它会抛出异常，并且还需要类型转换。Effective Java书上讲到，最好不要去使用clone()，可以使用拷贝构造函数或者拷贝工厂来拷贝一个对象。

```Java
public class CloneConstructorExample {
    private int[] arr;

    public CloneConstructorExample() {
        arr = new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public CloneConstructorExample(CloneConstructorExample original) {
        arr = new int[original.arr.length];
        for (int i = 0; i < original.arr.length; i++) {
            arr[i] = original.arr[i];
        }
    }

    public void set(int index, int value) {
        arr[index] = value;
    }

    public int get(int index) {
        return arr[index];
    }

    public static void main(String[] args) {
        CloneConstructorExample e1 = new CloneConstructorExample();
        CloneConstructorExample e2 = new CloneConstructorExample(e1);
        e1.set(2, 222);
        System.out.println(e2.get(2)); // 2
    }
}
```

-----



## 六、Extends(继承)

#### 访问权限

Java中有三个访问权限修饰符：private、protected以及public，如果不加访问修饰符，表示包级可见。

可以对类或类中的成员(字段和方法)加上访问修饰符

- 类可见表示其他类可以用这个类创建实例对象。
- 成员可见表示其他类可以用这个类的实例对象访问到该成员。

protected用于修饰成员，表示在继承体系中成员对于子类可见，但是这个访问修饰符对于类没有意义。

设计良好的模块会隐藏所有的实现细节，把它的 API 与它的实现清晰地隔离开来。模块之间只通过它们的 API 进行通信，一个模块不需要知道其他模块的内部工作情况，这个概念被称为信息隐藏或封装。因此访问权限应当尽可能地使每个类或者成员不被外界访问。

如果子类的方法重写了父类的方法，那么子类中该方法的访问级别不允许低于父类的访问级别。这是为了确保可以使用父类实例的地方都可以使用子类实例去代替，也就是确保满足里氏替换原则。

字段决不能是公有的，因为这么做的话就失去了对这个字段修改行为的控制，客户端可以对其随意修改。例如下面的例子中，AccessExample 拥有 id 公有字段，如果在某个时刻，我们想要使用 int 存储 id 字段，那么就需要修改所有的客户端代码。

``` java
public class AccessExample {
    public String id;
}
```

可以使用公有的 getter 和 setter 方法来替换公有字段，这样的话就可以控制对字段的修改行为。

```java
public class AccessExample {

    private int id;

    public String getId() {
        return id + "";
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }
}
```

但是也有例外，如果是包级私有的类或者私有的嵌套类，那么直接暴露成员不会有特别大的影响。

``` java
public class AccessWithInnerClassExample {
    private class InnerClass {
        int x;
    }
    
    private InnerClass innerClass;
    
    public AccessWithInnerClassExample() {
        innerClass = new InnerClass();
    }
    
    public int getValue() {
        return innerClass.x; // 直接访问
    }
}
```

-----

#### 抽象类和接口

##### 抽象类

抽象类和抽象方法都使用abstract关键字进行声明。如果一个类中包含抽象方法，那么这个类必须声明为抽象类。

抽象类和普通类最大的区别是，抽象类不能被实例化，只能被继承

``` java
public abstract class AbstractClassExample {
    protected int x;
    private int y;
    public abstract void func1();
    
    public void func2() {
        sout("func2");
    }
}
```

``` java
public class AbstractExtendClassExample extends AbstractClassExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }

    public static void main(String[] args) {
        AbstractExtendClassExample a1 = new AbstractExtendClassExample();
        a1.func1();
        a1.func2();
    }
}
```

##### 接口

接口是抽象类的延伸，在Java 8之前，它可以看出一个完全抽象的类，也就是说它不能有任何的方法实现。

从Java 8开始，接口也可以拥有默认的方法实现，这是因为不支持默认方法的接口的维护成本很高。在Java 8之前，如果一个接口需要添加新的方法，那么需要修改所有实现了该接口的类，让他们呢都实现新增的方法。

接口的成员(字段 + 方法)默认为public的，并且不允许定义为private和protected。从Java 9开始，允许将方法定义为private，这样就能定义某些复用的代码又不会把方法暴露出去。

接口的字段默认都是static和final的。

```java
public interface InterfaceExample {
    void func1();

    default void func2() {
        System.out.println("func2");
    }

    int x = 123;

    public int z = 0;
}
```

```Java
public class InterfaceImplementExample implements InterfaceExample {
    @Override
    public void func1() {
        System.out.println("func1");
    }

    public static void main(String[] args) {
        InterfaceImplementExample ie2 = new InterfaceImplementExample();
        ie2.func1();
        ie2.func2();
        System.out.println(InterfaceExample.x);
        System.out.println(InterfaceExample.z);
    }
}
```

##### 比较

- 从设计层面上看，抽象类提供了一种IS-A关系，需要满足里氏替换原则，即子类对象必须能够替换掉所有父类的对象。而接口更像一种LIKE-A关系，它只是提供一种方法实现契约，并不要求接口的实现接口的类具有IS-A关系。
- 从使用上看，一个类可以实现多个接口，但不能继承多个抽象类。
- 接口的字段只能是static和final类型的，而抽象类的字段没有限制。
- 接口的成员只能是public的，而抽象类的成员可以有多种访问权限。

##### 使用选择

使用接口：

- 需要让不相关的类都实现一个方法，例如不相关的类都可以实现Comparable接口中的compareTo()方法；
- 需要使用多重继承

使用抽象类：

- 需要在几个相关的类中共享代码
- 需要能控制继承来的成员的访问权限，而不是都为public
- 需要继承非静态和非常量字段

在很多情况下，接口优先于抽象类。因为接口没有抽象类严格的类层次结构要求，可以灵活地为一个类添加行为。并且从 Java 8 开始，接口也可以有默认的方法实现，使得修改接口的成本也变的很低。

- [Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- [深入理解 abstract class 和 interface](https://www.ibm.com/developerworks/cn/java/l-javainterface-abstract/)
- [When to Use Abstract Class and Interface](https://dzone.com/articles/when-to-use-abstract-class-and-intreface)
- [Java 9 Private Methods in Interfaces](https://www.journaldev.com/12850/java-9-private-methods-interfaces)

-----

#### Super

- 访问父类的构造函数：可以使用super()函数访问父类的构造函数，从而委托父类完成一些初始化工作。应该注意到，子类一定会调用父类的构造函数来完成初始化工作，一般是调用父类的默认构造函数，如果子类需要调用父类其他构造函数，那么就可以使用super()函数
- 访问父类的成员：如果子类重写了父类的某个方法，可以通过使用super关键字来引用父类的方法实现

```Java
public class SuperExample {
    protected int x;
    protected int y;

    public SuperExample(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void func() {
        System.out.println("SuperExample.func()");
    }
}
```

```Java
public class SuperExtendExample extends SuperExample {
    private int z;

    public SuperExtendExample(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    @Override
    public void func() {
        super.func();
        System.out.println("SuperExtendExample.func()");
    }

    public static void main(String[] args) {
        SuperExtendExample se1 = new SuperExtendExample(1, 3, 5);
        se1.func();
    }
}
```

[Using the Keyword super](https://docs.oracle.com/javase/tutorial/java/IandI/super.html)

-----

#### 重写和重载

##### 重写 --- override

存在于继承体系中，指子类实现了一个与父类在方法声明上完全相同的一个方法。

为了满足里氏替换原则，重写有以下三个限制：

- 子类方法的访问权限必须大于等于父类方法
- 子类方法的返回类型必须是父类方法返回类型或为其子类型
- 子类方法抛出的异常必须是父类抛出异常类型或为其子类型

使用@Override注解，可以让编译器帮忙检查是否满足上面的三个限制条件。

下面的示例中，SubClass 为 SuperClass 的子类，SubClass 重写了 SuperClass 的 func() 方法。其中：

- 子类方法访问权限为 public，大于父类的 protected。
- 子类的返回类型为 ArrayList<Integer>，是父类返回类型 List<Integer> 的子类。
- 子类抛出的异常类型为 Exception，是父类抛出异常 Throwable 的子类。
- 子类重写方法使用 @Override 注解，从而让编译器自动检查是否满足限制条件。

```Java
class SuperClass {
    protected List<Integer> func() throws Throwable {
        return new ArrayList<>();
    }
}

class SubClass extends SuperClass {
    @Override
    public ArrayList<Integer> func() throws Exception {
        return new ArrayList<>();
    }
}
```

在调用一个方法时，先从本类中查找看是否有对应的方法，如果没有再到父类中查看，看是否从父类继承来。否则就要对参数进行转型，转成父类之后看是否有对应的方法。总的来说，方法调用的优先级为：

- this.func(this)
- super.func(this)
- this.func(super)
- super.func(super)

```java
/*
  A
  |
  B
  |
  C
  |
  D
*/
public class A {
    public void show(A obj) {
        System.out.println("A.show(A)");
    }

    public void show(C obj) {
        System.out.println("A.show(C)");
    }
}

public class B extends A {
    @Override
    public void show(A obj) {
        System.out.println("B.show(A)");
    }
}

public class C extends B {}

public class D extends C {}
```

```java
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
```

##### 重载 --- overload

存在于同一类中，指一个方法与已存在的方法名称上相同，但是参数类型、个数、顺序至少有一个不同。

返回值不同，其他都相同不算是重载。

``` java
class OverloadingExample {
    public void show(int x) {
        sout(x);
    }
    
    public void show(int x, String y) {
        sout(x + " " + y);
    }
    
    main() {
        OverloadingExample example = new OverloadingExample();
        example.show(1);
        example.show(1, "2")
    }
}
```



## Reflection(反射机制)

#### 介绍

**Java反射机制是在运行状态中,对于任意一个类,都能够知道这个类的所有属性和方法;对于任意一个对象,都能够调用它的任意一个方法和属性;这种动态获取的信息以及动态调用对象的方法的功能叫做Java的反射机制**

-----

#### 获取Class对象的两种方式

1. **知道具体类的情况下可以使用**

``` java
Class targetObjectClass = TargetObject.class
```

2. **通过`Class.forName()`传入类的路径获取**

``` java
Class targetObjectClass  = Class.forName("cn.jater.reflection.TargetObject")
```

-----

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

-----

#### 静态编译和动态编译

1. **静态编译**: **在编译时确定类型,绑定对象**
2. **动态编译**: **运行时确定类型,绑定对象**

-----

#### 反射机制优缺点

##### 优点

- **运行期类型的类型判断,动态加载类,提高代码灵活性**

##### 缺点

- **性能瓶颈: 反射相当于一系列解释操作,通知JVM要做的事情,性能比直接的Java代码要慢很多**
- **安全问题: 让我们动态操作改变类的属性同时也增加了类的安全隐患**

-----

#### 反射的应用场景

<u>***反射是框架设计的灵魂***</u>

**例如:模块化的开发,通过反射去调用对应的字节码; 动态代理设计模式也采用了反射机制,还有Spring/Hibernate等框架也大量使用了反射机制**

1. **当使用JDBC链接数据库时使用`Class.forName()`通过反射加载数据库的驱动程序**
2. **Spring框架的IoC创建对象以及AOP功能都和反射有联系**
3. **动态配置实例的属性**

-----

- [Trail: The Reflection API](https://docs.oracle.com/javase/tutorial/reflect/index.html)
- [深入解析 Java 反射（1）- 基础](http://www.sczyh30.com/posts/Java/java-reflection-1/)

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


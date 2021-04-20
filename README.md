# Java Advanced

## 一、Regular Expression(正则表达式)

### 1. 正则简介

#### 1.1 正则表达式是什么

正则表达式(Regular Expression)是一个用正则符号写出的公式，程序对这个公司进行语法分析，简历一个语法分析树，在根据这个分析树结合正则表达式的引擎生成执行程序(这个执行程序我们把它称作状态机，也叫做状态自动机)，用于字符匹配

-----



### 2. 正则工具类

JDK中的`java.util.regex`包提供了对正则表达式的支持。

`java.util.regex`有三个核心类：

- **Pattern类**： `Pattern`是一个正则表达式的编译表示。
- **Matcher类**： `Matcher`是对输入字符串进行解释和匹配操作的引擎。
- **PatternSyntaxException**：`PatternSyntaxException`是一个非强制异常类，它表示一个正则表达式模式中的语法错误。

**PS** 需要格外注意一点，在Java中使用反斜杠“\”时必须写成`"\\"`。所以文本的代码出现形如`String regex = "\\$\\{.*?\\}"`其实就是`\$\{.\*?\}`。

-----

#### 2.1 Pattern类

`Pattern`类没有公共构造方法。要创建一个`Pattern`对象，你必须首先调用其**静态方法**`Compile`，加载正则规则字符串，如何返回一个Pattern对象。

与`Pattern`类一样，`Matcher`类也没有公共构造方法。你需要调用`Pattern`对象的`matcher`方法来获取一个`Matcher`对象。

##### Pattern和Matcher初始化

``` java
Pattern p = Pattern.compile(regex);
Matcher m = p.matcher(context);
```

-----

#### 2.2 Matcher类

`Matcher`类可以说是`java.util.regex`中的核心类，它有三类功能：检验、查找、替换。

##### 检验

为了检验文本是否与正则规则匹配，Matcher提供了以下几个返回值为Boolean的方法。

| 序号 | 方法及说明                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | **public boolean lookingAt()** 尝试将从区域开头开始的输入序列与该模式匹配 |
| 2    | **public boolean find()** 尝试查找与该模式匹配的输入序列的下一个子序列 |
| 3    | **public boolean find(int start)** 重置此匹配器，然后尝试匹配该模式，从指定索引开始的输入序列的下一个子序列 |
| 4    | **public boolean matches()** 尝试将整个区域与模式匹配        |

```java
public class MatcherCheckExample {

    private static void checkLookingAt(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.lookingAt()) {
            System.out.println(context + "\tlookingAt: " + regex);
        } else {
            System.out.println(context + "\tnot lookingAt: " + regex);
        }
    }

    private static void checkFind(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.find()) {
            System.out.println(context + "\tfind: " + regex);
        } else {
            System.out.println(context + "\tnot find: " + regex);
        }
    }

    private static void checkMatches(String regex, String context) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        if (matcher.matches()) {
            System.out.println(context + "\tmatches: " + regex);
        } else {
            System.out.println(context + "\tnot matches: " + regex);
        }
    }

    public static void main(String[] args) {
        String hello = "hello";
        String helloworld = "helloworld";
        String world = "world";

        checkLookingAt(hello, helloworld); // helloworld  lookingAt: hello
        checkLookingAt(world, helloworld); // helloworld  not lookingAt: world

        checkFind(hello, helloworld); // helloworld	find: hello
        checkFind(world, helloworld); // helloworld	find: world

        checkMatches(hello, helloworld); // helloworld	not matches: hello
        checkMatches(world, helloworld); // helloworld	not matches: world
        checkMatches(helloworld, helloworld); // helloworld	matches: helloworld
    }
}
```

**说明**

`regex = "world"`表示的正则规则是以world开头的字符串。

- `lookingAt`方法从头部开始，检查content字符串是否有子字符串与正则规则匹配
- `find`方法检查content字符串是否有子字符串与正则规则匹配，不管字符串所在位置
- `matches`方法检查content字符串整体是否与正则规则匹配

##### 查找

为了查找文本正则规则的位置，`Matcher`提供了以下方法：

| 序号 | 方法及说明                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | **public int start()** 返回以前匹配的初始索引                |
| 2    | **public int start(int group)** 返回在以前的匹配操作期间，由给定组所捕获的子序列的初始索引 |
| 3    | **public int end()** 返回最后匹配字符之后的偏移量            |
| 4    | **public int end(int group)** 返回在一千的匹配操作期间，由给定组所捕获子序列的最后字符之后的偏移量 |
| 5    | **public String group()** 返回前一个符合匹配条件的子序列     |
| 6    | **public String group(int group)** 返回指定的符合匹配条件的子序列 |

```java
public class MatcherFindExample {
    public static void main(String[] args) {
        final String regex = "world";
        final String context = "hellworld helloworld";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        System.out.println("content: " + context);

        int i = 0;
        while (matcher.find()) {
            i++;
            System.out.println("[NO." + i + "] found");
            System.out.print("start: " + matcher.start() + ", ");
            System.out.print("end: " + matcher.end() + ", ");
            System.out.print("group: " + matcher.group() + "\n");
        }
    }
}
```

**output**

![image-20210420113337758](images\mathers-find.png)

##### 替换

替换方法是替换输入字符串里文本的方法：

|      | 方法与说明                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | **public Matcher appendReplacement(StringBuffer sb, String replacement)** 实现非终端添加和替换步骤 |
| 2    | **public StringBuffer appendTail(StringBuffer sb)** 实现终端添加和替换步骤 |
| 3    | **public String replaceAll(String replacement)** 替换模式与给定字符串相匹配的输入序列的每个子序列 |
| 4    | **public String replaceFirst(String replacement)** 替换模式与给定字符串相匹配的输入序列的第一个子序列 |
| 5    | **public static String quoteReplacement(String s)** 返回指定字符串的字面替换字符串 |

###### replaceFirst 

替换第一个匹配正则规则的子序列

```java
public class MatcherReplaceExample {

    private static void replaceFirstTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        System.out.println("replaceFirst: " + matcher.replaceFirst(replace));
    }

    public static void main(String[] args) {
        String regex = "can";
        String replace = "can not";
        String content = "I can because i think i can";
        replaceFirstTest(regex, content, replace); // replaceFirst: I can not because i think i can
    }
}
```

###### replaceAll 

替换全部匹配正则规则的子序列

```java
public class MatcherReplaceExample {

    private static void replaceAllTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        System.out.println("replaceAll: " + matcher.replaceAll(replace));
    }

    public static void main(String[] args) {
        String regex = "can";
        String replace = "can not";
        String content = "I can because i think i can";
        replaceAllTest(regex, content, replace); // replaceAll: I can not because i think i can not
    }
}
```

###### appendReplacement & appendTail

```java
public class MatcherReplaceExample {

    private static void appendTailNAppendReplacementTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, replace);
        }
        System.out.println("appendReplacement: " + sb);
        matcher.appendTail(sb);
        System.out.println("appendTail: " + sb);
    }

    public static void main(String[] args) {
        String regex = "can";
        String replace = "can not";
        String content = "I can because i think i can";
        appendTailNAppendReplacementTest(regex, content, replace);
        // appendReplacement: I can not because i think i can not
        // appendTail: I can not because i think i can not
    }
}
```

**说明**

从输出结果可以看出，`appendReplacement`+`appendTail`组合使用，功能和`replaceAll`相同。

`replaceAll`的源码就是使用`appendReplacement`+`appendTail`组合实现的。

###### replaceAll的实现(source code)

``` java
public String replaceAll(String replacement) {
    reset();
    boolean result = find();
    if (result) {
        StringBuffer sb = new StringBuffer();
        do {
            appendReplacement(sb, replacement);
            result = find();
        } while (result);
        appendTail(sb);
        return sb.toString();
    }
    return text.toString();
}
```

###### quoteReplacement 

```java
public class MatcherReplaceExample {

    private static void quoteReplacementTest(String regex, String content, String replace) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        String replaceAll = matcher.replaceAll(Matcher.quoteReplacement(replace));
        System.out.println("quoteReplacement: " + replaceAll);
    }

    public static void main(String[] args) {
        String regex2 = "\\$\\{.*?\\}";
        String replace2 = "${product}";
        String content2 = "product is ${productName}";
        System.out.println("\ncontent: " + content2);
        quoteReplacementTest(regex2, content2, replace2); // quoteReplacement: product is ${product}
        replaceAllTest(regex2, content2, replace2); // Exception: java.lang.IllegalArgumentException: No group with name {product}
    }
}
```

**说明**

`String regex = "\\$\\{.*?\\}";`表示匹配类似`${name}`这样的字符串。由于`$`、`{`、`}`都是特殊字符，需要用反义字符`\`来修饰才能被当作一个字符串字符来处理。

使用`replaceAll`把`${productName}`替换成`${product}`，会将字符串中的`$`当作特殊字符处理。结果报出异常。

*how to fix?*

JDK 1.5引入了`quoteReplacement`方法。它可以用来转换特殊字符。其实源码很简单，就是判断字符串中如果有`\`or`$`，就为它加一个转义字符`\`

###### quoteReplacement源码

``` java
public static String quoteReplacement(String s) {
    if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1)) return s;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
		char c = s.charAt(i);
        if (c == '\\' || c == '$') {
            sb.append('\\');
        }
        sb.append(c);
    }
    return sb.toString();
}
```



### 3. 元字符



### 4. 分组构造



### 5. 贪婪与懒惰



### 正则附录



### 正则实战



### 正则表达式的性能



### 参考资料
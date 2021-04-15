package cn.jater.learn.java_basic.generics.generics_interface;

import java.util.Random;

public class FruitGenerator2 implements Generator<String> {
    private String[] fruits = new String[] {"Apple", "Banana", "Pear"};

    @Override
    public String next() {
        Random rand = new Random();
        return fruits[rand.nextInt(3)];
    }
}

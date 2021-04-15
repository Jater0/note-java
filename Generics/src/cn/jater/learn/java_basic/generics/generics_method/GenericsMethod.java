package cn.jater.learn.java_basic.generics.generics_method;

public class GenericsMethod {
    public class Generic<T> {
        private T key;

        public Generic(T key) {
            this.key = key;
        }

        public T getKey() {
            return key;
        }
    }

    public <T> T showKeyName(Generic<T> container) {
        System.out.println("container key : " + container.getKey());
        return container.getKey();
    }


}

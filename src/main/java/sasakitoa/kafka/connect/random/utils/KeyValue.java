package sasakitoa.kafka.connect.random.utils;

/**
 *
 */
public class KeyValue {

    private Object key;

    private Object value;

    public KeyValue(Object key, Object value) {
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}

package sasakitoa.kafka.connect.random.params;

/**
 * Parameters used by RandomString
 */
public class RandomStringParams {

    public static final String KEY_LENGTH = "random.string.key.length";
    public static final int KEY_LENGTH_DEFAULT = 10;
    public static final String KEY_LENGTH_DESCRIBE = "length of Key";

    public static final String VALUE_LENGTH = "random.string.value.length";
    public static final int VALUE_LENGTH_DEFAULT = 10;
    public static final String VALUE_LENGTH_DESCRIBE = "length of value";

}

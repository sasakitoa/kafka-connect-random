package sasakitoa.kafka.connect.random.params;

import org.apache.kafka.common.config.ConfigDef;

/**
 * Parameters used by RandomInt
 */
public class RandomIntParams implements Params {

    public static final String MIN_KEY = "random.int.key.min";
    public static final int MIN_KEY_DEFAULT = 0;
    public static final String MIN_KEY_DESCRIBE = "minimum key which will be generated";

    public static final String MAX_KEY = "random.int.key.max";
    public static final int MAX_KEY_DEFAULT = 100;
    public static final String MAX_KEY_DESCRIBE = "maximum key which will be generated";

    public static final String MIN_VALUE = "random.int.value.min";
    public static final int MIN_VALUE_DEFAULT = 0;
    public static final String MIN_VALUE_DESCRIBE = "minimum value which will be generated";

    public static final String MAX_VALUE = "random.int.value.max";
    public static final int MAX_VALUE_DEFAULT = 100;
    public static final String MAX_VALUE_DESCRIBE = "maximum value which will be generated";

    @Override
    public ConfigDef setConfig(ConfigDef configDef) {
        return configDef
            .define(RandomIntParams.MIN_KEY, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MIN_KEY_DESCRIBE)
            .define(RandomIntParams.MAX_KEY, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MAX_KEY_DESCRIBE)
            .define(RandomIntParams.MIN_VALUE, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MIN_VALUE_DESCRIBE)
            .define(RandomIntParams.MAX_VALUE, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MAX_VALUE_DESCRIBE);
    }
}

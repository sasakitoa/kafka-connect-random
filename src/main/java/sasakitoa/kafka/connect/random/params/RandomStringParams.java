package sasakitoa.kafka.connect.random.params;

import org.apache.kafka.common.config.ConfigDef;

/**
 * Parameters used by RandomString
 */
public class RandomStringParams implements Params {

    public static final String KEY_LENGTH = "random.string.key.length";
    public static final int KEY_LENGTH_DEFAULT = 10;
    public static final String KEY_LENGTH_DESCRIBE = "length of Key";

    public static final String VALUE_LENGTH = "random.string.value.length";
    public static final int VALUE_LENGTH_DEFAULT = 10;
    public static final String VALUE_LENGTH_DESCRIBE = "length of value";

    @Override
    public ConfigDef setConfig(ConfigDef configDef) {
        return configDef
            .define(RandomStringParams.KEY_LENGTH, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomStringParams.KEY_LENGTH_DESCRIBE)
            .define(RandomStringParams.VALUE_LENGTH, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomStringParams.VALUE_LENGTH_DESCRIBE);
    }
}

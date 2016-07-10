package sasakitoa.kafka.connect.random.generator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import sasakitoa.kafka.connect.random.utils.KeyValue;
import sasakitoa.kafka.connect.random.params.RandomIntParams;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Implement Generator which generate random integers.
 *
 * Generate Value
 * ------------------
 * Key: Integer(random)
 * Value: Integer(random)
 *
 * Partition
 * ------------------
 * This generator does not specified, and depends on KafkaProducer's Partitioner
 * (if you want to specify Partitioner class, you can do same way when use Producer APIs.)
 *
 */
public class RandomInt extends Generator {

    private Random rand;

    private int minKey, maxKey, minValue, maxValue;

    @Override
    public void setConfigDef(ConfigDef configDef) {
        configDef
            .define(RandomIntParams.MIN_KEY, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MIN_KEY_DESCRIBE)
            .define(RandomIntParams.MAX_KEY, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MAX_KEY_DESCRIBE)
            .define(RandomIntParams.MIN_VALUE, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MIN_VALUE_DESCRIBE)
            .define(RandomIntParams.MAX_VALUE, ConfigDef.Type.INT, ConfigDef.Importance.MEDIUM, RandomIntParams.MAX_VALUE_DESCRIBE);
    }

    @Override
    public Map<String, String> setTaskConfigs(Map<String, String> props) {
        Map<String, String> value = new HashMap<>();

        this.minKey = getValueAndParse(props, RandomIntParams.MIN_KEY, RandomIntParams.MIN_KEY_DEFAULT);
        this.maxKey = getValueAndParse(props, RandomIntParams.MAX_KEY, RandomIntParams.MAX_KEY_DEFAULT);
        if(minKey > maxKey) {
            throw new ConnectException(RandomIntParams.MAX_KEY + " must be greater than " + RandomIntParams.MIN_KEY);
        }
        this.minValue = getValueAndParse(props, RandomIntParams.MIN_VALUE, RandomIntParams.MIN_VALUE_DEFAULT);
        this.maxValue = getValueAndParse(props, RandomIntParams.MAX_VALUE, RandomIntParams.MAX_VALUE_DEFAULT);
        if(minValue > maxValue) {
            throw new ConnectException(RandomIntParams.MAX_VALUE + " must be greater than " + RandomIntParams.MIN_VALUE);
        }

        value.put(RandomIntParams.MIN_KEY, Integer.toString(this.minKey));
        value.put(RandomIntParams.MAX_KEY, Integer.toString(this.maxKey));
        value.put(RandomIntParams.MIN_VALUE, Integer.toString(this.minValue));
        value.put(RandomIntParams.MAX_VALUE, Integer.toString(this.maxValue));
        return value;
    }

    @Override
    public Schema getKeySchema() {
        return Schema.INT32_SCHEMA;
    }

    @Override
    public Schema getValueSchema() {
        return Schema.INT32_SCHEMA;
    }

    @Override
    public void start(Map<String, String> props) throws ConnectException {
        this.minKey = getValueAndParse(props, RandomIntParams.MIN_KEY, RandomIntParams.MIN_KEY_DEFAULT);
        this.maxKey = getValueAndParse(props, RandomIntParams.MAX_KEY, RandomIntParams.MAX_KEY_DEFAULT);
        this.minValue = getValueAndParse(props, RandomIntParams.MIN_VALUE, RandomIntParams.MIN_VALUE_DEFAULT);
        this.maxValue = getValueAndParse(props, RandomIntParams.MAX_VALUE, RandomIntParams.MAX_VALUE_DEFAULT);
        rand = new Random();
    }

    @Override
    public KeyValue generate() {
        int key = getRandomValue(minKey, maxKey);
        int value = getRandomValue(minValue, maxValue);
        return new KeyValue(key, value);
    }

    private int getValueAndParse(Map<String, String> props, String propName, int defaultValue) {
        String valueStr = props.get(propName);
        if(valueStr == null) {
            // use default since propName(MIN_VALUE or MAX_VALUE) is not set.
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(valueStr);
            } catch(NumberFormatException ex) {
                throw new ConnectException(propName + " must be integer, but " + valueStr + " was specified.");
            }
        }
    }

    private int getRandomValue(int min, int max) {
        long value = (long)rand.nextInt();
        return (int)((value - (long)(Integer.MIN_VALUE)) % (max - min) + min);
    }
}

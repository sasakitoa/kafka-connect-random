package sasakitoa.kafka.connect.random.generator;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import sasakitoa.kafka.connect.random.params.RandomStringParams;
import sasakitoa.kafka.connect.random.utils.KeyValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Implement Generator which generate random strings.
 *
 * Generate Value
 * ------------------
 * Key: String(random, length is random.string.key.length)
 * Value: String(random, length is random.string.value.length)
 *
 * Key and Value contains a-z and A-Z characters.
 *
 * Partition
 * ------------------
 * This generator does not specified, and depends on KafkaProducer's Partitioner
 * (if you want to specify Partitioner class, you can do same way when use Producer APIs.)
 *
 */
public class RandomString extends Generator {

    private int keyLength, valueLength;

    @Override
    public Map<String, String> setTaskConfigs(Map<String, String> props) {
        this.params = new RandomStringParams();
        Map<String, String> value = new HashMap<>();

        keyLength = getValueAndValidate(props, RandomStringParams.KEY_LENGTH, RandomStringParams.KEY_LENGTH_DEFAULT);
        if(keyLength <= 0) {
            throw new ConnectException(RandomStringParams.KEY_LENGTH + " should be greater than 0, but specified " + keyLength);
        }
        valueLength = getValueAndValidate(props, RandomStringParams.VALUE_LENGTH, RandomStringParams.VALUE_LENGTH_DEFAULT);
        if(valueLength <= 0) {
            throw new ConnectException(RandomStringParams.VALUE_LENGTH + " should be greater than 0, but specified " + valueLength);
        }

        value.put(RandomStringParams.KEY_LENGTH, new Integer(keyLength).toString());
        value.put(RandomStringParams.VALUE_LENGTH, new Integer(valueLength).toString());
        return value;
    }

    @Override
    public Schema getKeySchema() {
        return Schema.STRING_SCHEMA;
    }

    @Override
    public Schema getValueSchema() {
        return Schema.STRING_SCHEMA;
    }

    @Override
    public void start(Map<String, String> props) throws ConnectException {
        keyLength = getValueAndValidate(props, RandomStringParams.KEY_LENGTH, RandomStringParams.KEY_LENGTH_DEFAULT);
        valueLength = getValueAndValidate(props, RandomStringParams.VALUE_LENGTH, RandomStringParams.VALUE_LENGTH_DEFAULT);
    }

    @Override
    public KeyValue generate() {
        String key = RandomStringUtils.randomAlphabetic(keyLength);
        String value = RandomStringUtils.randomAlphabetic(valueLength);
        return new KeyValue(key, value);
    }

    private int getValueAndValidate(Map<String, String> props, String propName, int defaultValue) {
        String valueStr = props.get(propName);
        if(valueStr == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(valueStr);
            } catch(NumberFormatException ex) {
                throw new ConnectException(propName + " must be integer, but " + valueStr + " was specified.");
            }
        }
    }
}

package sasakitoa.kafka.connect.random.generator;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.*;
import sasakitoa.kafka.connect.random.RandomSourceConnector;
import sasakitoa.kafka.connect.random.RandomSourceTask;
import sasakitoa.kafka.connect.random.params.Params;
import sasakitoa.kafka.connect.random.params.RandomStringParams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RandomStringTest {

    /**
     * Number of loop
     * NOTE: In this generator, KEY and VALUE will be generated random,
     *       so it will be tested generating many times.
     */
    private static final int numTestLoop = 10000;

    private Map<String, String> baseProps;

    @Before
    public void init() {
        baseProps = new HashMap<>();
        baseProps.put(Params.TOPIC, "test");
        baseProps.put(Params.NUM_MESSAGES, Long.toString(-1L));
        baseProps.put(Params.GENERATOR_CLASS, RandomString.class.getName());
    }

    @Test
    public void testNormalLifeCycle() throws InterruptedException {
        testRandomStringGenerate(10, 10);
        testRandomStringGenerate(100, 20);
        testRandomStringGenerate(42, 101);
    }

    private void testRandomStringGenerate(int keyLength, int valueLength) throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomStringParams.KEY_LENGTH, Integer.toString(keyLength));
        props.put(RandomStringParams.VALUE_LENGTH, Integer.toString(valueLength));
        RandomSourceTask task = new RandomSourceTask();
        task.start(props);

        for(int i = 0; i < numTestLoop; i++) {
            List<SourceRecord> records = task.poll();
            for(SourceRecord record : records) {
                String key = (String)record.key();
                if(key.length() != keyLength) {
                    Assert.fail("Generate KEY length is not equal to " + RandomStringParams.KEY_LENGTH);
                }

                String value = (String)record.value();
                if(value.length() != valueLength) {
                    Assert.fail("Generate VALUE length is not equal to " + RandomStringParams.VALUE_LENGTH);
                }
            }
        }
    }

    @Test
    public void testIllegalKeyValueLength() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomStringParams.KEY_LENGTH, Integer.toString(-10));
        props.put(RandomStringParams.VALUE_LENGTH, Integer.toString(10));
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomStringParams.KEY_LENGTH + " is negative, but not thrown exception.");
        } catch(ConnectException ex) {
            //Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomStringParams.KEY_LENGTH, Integer.toString(10));
        props.put(RandomStringParams.VALUE_LENGTH, Integer.toString(-10));
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomStringParams.VALUE_LENGTH + " is negative, but not thrown exception.");
        } catch(ConnectException ex) {
            //Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomStringParams.KEY_LENGTH, "foo");
        props.put(RandomStringParams.VALUE_LENGTH, Integer.toString(10));
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomStringParams.KEY_LENGTH + " not number, but not thrown exception.");
        } catch(ConnectException ex) {
            //Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomStringParams.KEY_LENGTH, Integer.toString(10));
        props.put(RandomStringParams.VALUE_LENGTH, "foo");
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomStringParams.VALUE_LENGTH + " not number, but not thrown exception.");
        } catch(ConnectException ex) {
            //Expected
        }
    }
}

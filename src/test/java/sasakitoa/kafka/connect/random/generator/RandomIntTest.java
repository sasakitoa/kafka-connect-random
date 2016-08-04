package sasakitoa.kafka.connect.random.generator;

import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sasakitoa.kafka.connect.random.RandomSourceConnector;
import sasakitoa.kafka.connect.random.RandomSourceTask;
import sasakitoa.kafka.connect.random.params.CommonParams;
import sasakitoa.kafka.connect.random.params.RandomIntParams;


public class RandomIntTest {

    /**
     * Number of loop
     * NOTE: In this generator, KEY and VALUE will be generated random,
     *       so it will be tested generating many times.
     */
    private static final int numTestLoop = 10000;

    private Map<String, String> baseProps = new HashMap<>();

    @Before
    public void init() {
        baseProps.put(CommonParams.TOPIC, "test");
        baseProps.put(CommonParams.NUM_MESSAGES, new Long(-1L).toString());
        baseProps.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());
    }

    @Test
    public void testNormalLifeCycle() throws InterruptedException {
        testRandomIntegerGenerate(100, 300, 50000, 50500);
        testRandomIntegerGenerate(-3000, -2500, -400, -150);
        testRandomIntegerGenerate(-100, 200, -50, 70);
    }

    private void testRandomIntegerGenerate(int minKey, int maxKey, int minValue, int maxValue) throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, new Integer(minKey).toString());
        props.put(RandomIntParams.MAX_KEY, new Integer(maxKey).toString());
        props.put(RandomIntParams.MIN_VALUE, new Integer(minValue).toString());
        props.put(RandomIntParams.MAX_VALUE, new Integer(maxValue).toString());
        RandomSourceTask task = new RandomSourceTask();
        task.start(props);

        for(int i = 0; i < numTestLoop; i++) {
            List<SourceRecord> records = task.poll();
            for(SourceRecord record : records) {
                int key = (int)record.key();
                int value = (int)record.value();

                if(key < minKey) Assert.fail("Generate KEY is less than " + RandomIntParams.MIN_KEY);
                if(key > maxKey) Assert.fail("Generate KEY is larger than " + RandomIntParams.MAX_KEY);
                if(value < minValue) Assert.fail("Generate VALUE is less than " + RandomIntParams.MIN_VALUE);
                if(value > maxValue) Assert.fail("Generate VALUE is larger than " + RandomIntParams.MAX_VALUE);
            }
        }
    }

    @Test
    public void testIllegalKeyValueRange() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, new Integer(100).toString());
        props.put(RandomIntParams.MAX_KEY, new Integer(-20).toString());
        props.put(RandomIntParams.MIN_VALUE, new Integer(10).toString());
        props.put(RandomIntParams.MAX_VALUE, new Integer(100).toString());
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomIntParams.MIN_KEY + " is larger than " + RandomIntParams.MAX_KEY + ", but not thrown Exception.");
        } catch(ConnectException ex) {
            // Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, new Integer(10).toString());
        props.put(RandomIntParams.MAX_KEY, new Integer(20).toString());
        props.put(RandomIntParams.MIN_VALUE, new Integer(10).toString());
        props.put(RandomIntParams.MAX_VALUE, new Integer(-100).toString());
        try {
            RandomSourceConnector connector = new RandomSourceConnector();
            connector.start(props);
            Assert.fail(RandomIntParams.MIN_VALUE + " is larger than " + RandomIntParams.MAX_VALUE + ", but not thrown Exception.");
        } catch(ConnectException ex) {
            // Expected
        }
    }

    @Test
    public void testNotNumberParameter() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, "foo");
        props.put(RandomIntParams.MAX_KEY, "10");
        props.put(RandomIntParams.MIN_VALUE, "1");
        props.put(RandomIntParams.MAX_VALUE, "10");
        RandomSourceTask task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(RandomIntParams.MIN_KEY + " was specified foo, but not thrown exception.");
        } catch(ConnectException ex) {
            // Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, "1");
        props.put(RandomIntParams.MAX_KEY, "foo");
        props.put(RandomIntParams.MIN_VALUE, "1");
        props.put(RandomIntParams.MAX_VALUE, "10");
        task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(RandomIntParams.MAX_KEY + " was specified foo, but not thrown exception.");
        } catch(ConnectException ex) {
            // Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, "1");
        props.put(RandomIntParams.MAX_KEY, "10");
        props.put(RandomIntParams.MIN_VALUE, "foo");
        props.put(RandomIntParams.MAX_VALUE, "10");
        task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(RandomIntParams.MIN_VALUE + " was specified foo, but not thrown exception.");
        } catch(ConnectException ex) {
            // Expected
        }

        props = new HashMap<>();
        props.putAll(baseProps);
        props.put(RandomIntParams.MIN_KEY, "1");
        props.put(RandomIntParams.MAX_KEY, "10");
        props.put(RandomIntParams.MIN_VALUE, "1");
        props.put(RandomIntParams.MAX_VALUE, "foo");
        task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(RandomIntParams.MAX_VALUE + " was specified foo, but not thrown exception.");
        } catch(ConnectException ex) {
            // Expected
        }
    }

}

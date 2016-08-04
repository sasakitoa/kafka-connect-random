package sasakitoa.kafka.connect.random;

import org.apache.kafka.connect.errors.ConnectException;
import org.junit.*;
import sasakitoa.kafka.connect.random.generator.RandomInt;
import sasakitoa.kafka.connect.random.params.CommonParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomSourceConnectorTest {
    
    @Test
    public void testDefaultSinkTasks() {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());

        RandomSourceConnector connector = new RandomSourceConnector();
        connector.start(props);

        List<Map<String, String>> configs = connector.taskConfigs(1);
        Assert.assertEquals(1L, (long)configs.size());

        Map<String, String> config = configs.get(0);
        Assert.assertEquals(RandomInt.class.getName(), config.get(CommonParams.GENERATOR_CLASS));
        Assert.assertEquals(Long.toString(CommonParams.NUM_MESSAGES_DEFAULT), config.get(CommonParams.NUM_MESSAGES));
        Assert.assertEquals(CommonParams.TOPIC_DEFAULT, config.get(CommonParams.TOPIC));
    }

    @Test
    public void testUserValueSinkTasks() {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());
        props.put(CommonParams.NUM_MESSAGES, Long.toString(100L));
        props.put(CommonParams.TOPIC, "test-topic-name");

        RandomSourceConnector connector = new RandomSourceConnector();
        connector.start(props);

        List<Map<String, String>> configs = connector.taskConfigs(1);
        Assert.assertEquals(1, configs.size());

        Map<String, String> config = configs.get(0);
        Assert.assertEquals(RandomInt.class.getName(), config.get(CommonParams.GENERATOR_CLASS));
        Assert.assertEquals(Long.toString(100L), config.get(CommonParams.NUM_MESSAGES));
        Assert.assertEquals("test-topic-name", config.get(CommonParams.TOPIC));
    }

    @Test(expected = ConnectException.class)
    public void testNotNumberNumMessages() {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());
        props.put(CommonParams.NUM_MESSAGES, "foo");
        props.put(CommonParams.TOPIC, "test-topic-name");

        RandomSourceConnector connector = new RandomSourceConnector();
        connector.start(props);
    }

    @Test(expected = ConnectException.class)
    public void testGeneratorClassNotFound() {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.GENERATOR_CLASS, "NotExistClass");

        RandomSourceConnector connector = new RandomSourceConnector();
        connector.start(props);
    }

    @Test(expected = ConnectException.class)
    public void testGeneratorClassNotExtends() {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.GENERATOR_CLASS, RandomSourceConnector.class.getName());

        RandomSourceConnector connector = new RandomSourceConnector();
        connector.start(props);
    }
}

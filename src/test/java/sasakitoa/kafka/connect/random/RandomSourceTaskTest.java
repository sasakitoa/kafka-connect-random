package sasakitoa.kafka.connect.random;

import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.*;
import sasakitoa.kafka.connect.random.generator.RandomInt;
import sasakitoa.kafka.connect.random.params.CommonParams;
import sasakitoa.kafka.connect.random.utils.ConstructorNeedArgsClass;

import java.util.*;

public class RandomSourceTaskTest {

    private static final int TEST_SECONDS_FOR_LIMITED = 10;

    @Test
    public void testUnlimitedSendMessages() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.TOPIC, "ExpectedTopic");
        props.put(CommonParams.NUM_MESSAGES, Long.toString(-1L));
        props.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());

        RandomSourceTask task = new RandomSourceTask();
        task.start(props);

        List<SourceRecord> records = task.poll();
        Assert.assertEquals(RandomSourceTask.NUM_MESSAGES_ONE_SHOT_WHEN_UNLIMITED, records.size());

        SourceRecord record = records.get(0);
        Assert.assertEquals("ExpectedTopic", record.topic());

        RandomInt randomInt = new RandomInt();
        Assert.assertEquals(randomInt.getKeySchema(), record.keySchema());
        Assert.assertEquals(randomInt.getValueSchema(), record.valueSchema());
    }

    @Test
    public void testLimitedSendMessages() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.TOPIC, "topic");
        long numMessagesPerSeconds = 20L;
        props.put(CommonParams.NUM_MESSAGES, Long.toString(numMessagesPerSeconds));
        props.put(CommonParams.GENERATOR_CLASS, RandomInt.class.getName());

        RandomSourceTask task = new RandomSourceTask();
        task.start(props);

        Date startTime = new Date();
        long passedSeconds;
        int numSendMessages = 0;
        do {
            List<SourceRecord> records = task.poll();
            // Time lag from task$poll() to get passedSeconds causes to fail this test.
            // -200 is to avoid this fail.
            passedSeconds = ((new Date().getTime() - startTime.getTime()) - 200) / 1000;

            numSendMessages += records.size();
        } while(passedSeconds < TEST_SECONDS_FOR_LIMITED);

        Assert.assertEquals(passedSeconds * numMessagesPerSeconds, numSendMessages);
    }

    @Test
    public void tesTopicIsNotValid() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        RandomSourceTask task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(CommonParams.TOPIC + " is null, but exception was not thrown.");
        } catch (ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains(CommonParams.TOPIC + " must not be null and empty"));
        }

        props.put(CommonParams.TOPIC, "");
        try {
            task.start(props);
            Assert.fail(CommonParams.TOPIC + " is empty, but exception was not thrown.");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains(CommonParams.TOPIC + " must not be null and empty"));
        }
    }

    @Test
    public void testNumMessagesIsNotValid() throws InterruptException {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.TOPIC, "foo");
        props.put(CommonParams.NUM_MESSAGES, "foo");
        RandomSourceTask task = new RandomSourceTask();
        try {
            task.start(props);
            Assert.fail(CommonParams.NUM_MESSAGES + " is not long, but exception was not thrown");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains(CommonParams.NUM_MESSAGES + " must be long"));
        }
    }

    @Test
    public void testClassIsNotValid() throws InterruptedException {
        Map<String, String> props = new HashMap<>();
        props.put(CommonParams.TOPIC, "foo");
        RandomSourceTask task = new RandomSourceTask();

        try {
            task.start(props);
            Assert.fail(CommonParams.GENERATOR_CLASS + " is null, but exception was not thrown");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains(CommonParams.GENERATOR_CLASS + " must be set"));
        }

        props.put(CommonParams.GENERATOR_CLASS, "");
        try {
            task.start(props);
            Assert.fail(CommonParams.GENERATOR_CLASS + " is empty, but exception was not thrown");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains(CommonParams.GENERATOR_CLASS + " must be set"));
        }

        props.put(CommonParams.GENERATOR_CLASS, "foo.bar");
        try {
            task.start(props);
            Assert.fail(CommonParams.GENERATOR_CLASS + " is set non-exists class, but exception was not thrown.");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains("is not found."));
        }

        props.put(CommonParams.GENERATOR_CLASS, ConstructorNeedArgsClass.class.getName());
        try {
            task.start(props);
            Assert.fail(CommonParams.GENERATOR_CLASS + " has only constructor need 1 argument, but exception was not thrown.");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains("Failed to create instance of Generator class"));
        }

        props.put(CommonParams.GENERATOR_CLASS, sasakitoa.kafka.connect.random.utils.NoConstructorClass.class.getName());
        try {
            task.start(props);
            Assert.fail(CommonParams.GENERATOR_CLASS + " has no constructor, but exception was not thrown.");
        } catch(ConnectException ex) {
            Assert.assertTrue(ex.getMessage().contains("is not accessible"));
        }
    }
}

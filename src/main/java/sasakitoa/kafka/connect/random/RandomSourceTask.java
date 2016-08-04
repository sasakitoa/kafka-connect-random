package sasakitoa.kafka.connect.random;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import sasakitoa.kafka.connect.random.utils.KeyValue;
import sasakitoa.kafka.connect.random.params.CommonParams;
import sasakitoa.kafka.connect.random.generator.Generator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * RandomSourceTask sends random value created by generators.
 */
public class RandomSourceTask extends SourceTask {

    private String topic;

    private Generator generator;

    private long numMessagesPerSecond;

    private Date lastSend;

    private Schema KEY_SCHEMA, VALUE_SCHEMA;

    public static final int NUM_MESSAGES_ONE_SHOT_WHEN_UNLIMITED = 100;

    @Override
    public String version() {
        return new RandomSourceConnector().version();
    }

    @Override
    public void start(Map<String, String> props) {
        topic = props.get(CommonParams.TOPIC);
        if(topic == null || topic.isEmpty()) {
            throw new ConnectException(CommonParams.TOPIC + " must not be null and empty, but it was " + topic);
        }

        String numMessagesPerSecondSrt = props.get(CommonParams.NUM_MESSAGES);
        if(numMessagesPerSecondSrt != null) {
            try {
                numMessagesPerSecond = Long.parseLong(numMessagesPerSecondSrt);
            } catch (NumberFormatException ex) {
                throw new ConnectException(CommonParams.NUM_MESSAGES + " must be long, but it was " + numMessagesPerSecondSrt);
            }
        } else {
            numMessagesPerSecond = CommonParams.NUM_MESSAGES_DEFAULT;
        }

        String generatorStr = props.get(CommonParams.GENERATOR_CLASS);
        if(generatorStr == null || generatorStr.isEmpty()) {
            throw new ConnectException(CommonParams.GENERATOR_CLASS + " must be set, but it is null or empty.");
        }
        try {
            generator = (Generator)Class.forName(generatorStr).newInstance();
        } catch(ClassNotFoundException ex) {
            throw new ConnectException("Generator class " + generatorStr + " is not found.");
        } catch(InstantiationException ex) {
            throw new ConnectException("Failed to create instance of Generator class \"" + generatorStr + "\"");
        } catch(IllegalAccessException ex) {
            throw new ConnectException("Generator class \"" + generatorStr + "\" is not accessible.");
        }

        KEY_SCHEMA = generator.getKeySchema();
        VALUE_SCHEMA = generator.getValueSchema();
        generator.start(props);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        Date currentTime = new Date();
        if(numMessagesPerSecond > 0 && lastSend == null) {
            lastSend = currentTime;
        }

        long numMessages = NUM_MESSAGES_ONE_SHOT_WHEN_UNLIMITED;
        if(numMessagesPerSecond > 0) {
            long passedSeconds = (currentTime.getTime() - lastSend.getTime()) / 1000L;
            numMessages = passedSeconds * numMessagesPerSecond;
        }
        List<SourceRecord> send = new ArrayList<>();
        for(long i = 0L; i < numMessages; i++) {
            KeyValue message = generator.generate();
            send.add(new SourceRecord(null, null, topic, KEY_SCHEMA, message.getKey(), VALUE_SCHEMA, message.getValue()));
        }

        if(numMessages > 0) {
            lastSend = currentTime;
        }
        return send;
    }

    @Override
    public void stop() {
        generator.stop();
    }
}

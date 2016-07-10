package sasakitoa.kafka.connect.random.params;

/**
 * Common Parameters
 */
public class Params {

    public static String TOPIC = "topic";
    public static String TOPIC_DEFAULT = "random-connector-topic";
    public static String TOPIC_DESCRIBE = "Topic which is send random value.";

    public static String GENERATOR_CLASS = "generator.class";
    public static String GENERATOR_CLASS_DEFAULT = "sasakitoa.kafka.connect.random.generator.RandomInt";
    public static String GENERATOR_CLASS_DESCRIBE = "The class which generates random value.";

    public static String NUM_MESSAGES = "messages.per.second";
    public static long NUM_MESSAGES_DEFAULT = -1;
    public static String NUM_MESSAGES_DESCRIBE = "Number of messages which will send to topic in a second.";

}
